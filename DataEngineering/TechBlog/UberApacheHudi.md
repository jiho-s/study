# Setting Uber’s Transactional Data Lake in Motion with Incremental ETL Using Apache Hudi

우버에서 데이터 레이크는 전반의 데이터 엔지니어링, 데이터 사이언스, 머신러닝과 리포트를 지원한다. 따라서, 이러한 테이블을 계산하는 ETA 파이프라인은 Uber의 앱과 서비스에서 크리티컬한 역할을한다. Uber 에서 최신의 데이터를 유지하는 것은 비즈니스 핵심 키이다. Uber에서는 실제 세계에서 일어나는 일을 최신 상태로 유지하는 엔지어링 노력에 대해 많은 투자를 하고 있다.

ETL 파이프라인에서 이러한 데이터 최신성을 달성하기 위해서 새로운 ETL 을 실행할 때마다 모든 데이터를 다시 계산하는 대신 증분 업데이트 하는것이 핵심과제이다. Uber 의 엄청난 규모에서 이러한 파이프라인을 비용효율적으로 운영하기 위해서도 꼭 필요하다. Uber 에서는 이러한 문제를 해결하기 위해 Apache Hudi 프로젝트를 통해 강력한 증분 데이터 처리 기능을 가진 새로운 트랜젝션 데이터 레이크 패러다임을 도입했다. 이 글에서는 증분 데이터 처리 모델을 ETL 파이프라인으로 확장한 1년 동안의 작업을 공유한다.

## Background

스트림 프로세싱 시스템에는 이벤트 시간, 또는 처리 시간을 사용하여 늦게 도착하거나 사후에 온 데이터를. 다루기 위한 지원이 내장되어 있다. 이벤트 시간과 처리 시간은 이상적으로는 동일해야하지만, 재시도, 누락, 네트워크 지연, 비즈니스 로직 등으로 인해 달라질수 있다. 배치 데이터 처리는 대량의 데이터를 처리할수 있지만, 늦게 도착하는 데이터에 대해 제대로 처리하지 못하며, 늦게 도착한 데이터에 대해 효과적으로 처리할수 없다. 이를 해결하기 위해 배치 프로세싱 파이프라인은 데이터가 몇시간동안 안정된 후에 트리거 되거나 전체 테이블 또는 time window 를 하루에 여러번 반복적으로 계산한다. 즉, 늦게 도착하거나 사후에 온 데이터를 처리하기 위해서는 실제 변경되는 데이터가 작더라도 전체 파이티션을 다시 계산해야된다.

증분처리의 아이디어는 간단하다. 증분 처리는 각각 실행될때 새로운 데이터만 처리한후 새로운 결과를 업데이트 하는 방식으로 스트리밍 데이터 처리의 의미를 배치 데이터 처리로 확장한다. 이를 통해 배치 파이프라인이 훨씬 짧아져 비용을 크게 줄일수 있을 뿐 아니라 훨씬 자주 실행할 수 있어 데이터 새로 고침 속도도 빨라진다. Apache Hudi 는 데이터 레이크에서 점진적인 데이터 처리를 제공하도록 설계되었다. 스트리밍 시스템은 포인트 조회 기능이 있는 버전이 있는 상태 저장소를 통해 늦게 도착하는 데이터를 처리한다. 마찬가지로, Apache Hudi 는 특정 포인트 조회, 강력한 인덱싱, 최적화된 Merge-On-Read 스토리지 포맷, 인덱싱된 메다데이터를 지원하여, 테이블에 대한 빈번한 변경을 처리한다. 기존의 데이터 레이크에서는  늦은 데이터 처리를 위해 재계산하는 것이 영향을 받는 모든 파티션을 재계산하고 모든 다운스트림 테이블에 대해 이 처리를 반영해야한다. Apache Hudi 는 강력한 change data capture 를 지원하여 데이터 처리를 점진적인 체이닝을 할수 있도록한다.

오랫동안 기존 데이터 레이크의 데이터는 불변하는 것으로 여겨졌다. 레이크 하우스 아키텍쳐가 데이터 웨어하우시와 유사한 트랜잭션/업데이트/삭제 기능을 추가함으로써 이에 도전하고 있지만, 스트림 프로세싱의 스테이트 스토어 같은 데이터베이스와 같은 유사한 기능이 증분 데이터 모델의 이점을 완전히 실현하기 위해서 필요하다.

## Use Cases

### Driver and Courier Earnings

데이터 세트에 모든 드라이버의 하루 수입이 포함되어 있다고 가정하겠다. 예를 들어 드라이버가 배달이 완료된 후 몇시간이후에 팁을 받게되면, 하루 수입 정보가 들어 있는 record 에서 늦게 도착한 업데이트가 있게 된다. 

일반적은 배치 ETL 환경에서는 입력 데이터가 어떻게 변경되었는지에 대한 정보가 없다. 며칠전 시간으로 돌아가 모든 파티션을 재처리하여 운전자 수입을 목표 테이블에 업데이트 해야한다. 전체 파티션에서 업데이트 해야할 record 수는 적을수 있기 때문에 이 프로세스는 시간과 리소스를 매우 많이 소모하는 것으로 나타났다. 아래 도표에서 한 번의 실행으로 변경되는 모든 날짜 파티션과 특정 날짜에 대해 업데이트해야 하는 이벤트 수를 확인할수 있다.

증분 ETL 접근 방식에서는 증분 파이프라인을 실행할 때마다 이러한 모든 업데이트가 소비될수 있으며, 대상 Apache Hudi 테이블에 record 수준에서 반영될 수 있다. 이 프로세스를 통해 정기적인 재처리 없이 데이터 세트의 정확도와 완전성을 100% 달성 할 수 있다.

### Frequent Menu Updates for Uber Eats Merchants

다른 예는 짧은 SLA 로 모델링된 테이블의 데이터를 가져와야 할 때이다. 배치에서 이를 해결하기 위해서는 하루의 전체 변경을 가져와 모든 엔티티에 대해 최신 정보를 얻은다음 다른 모든 테이블을 채운다.  배치 접근 방식의 문제는 다운스트림에 대한 SLA 가 증가한다는 거이다. 한번 파이프라인이 완료되는 데 14시간이 걸리고 한번의 장애가 발생하면 SLA 가 다시 몇시간씩 늘어난다. 증분 ELT 에서는 훨씬더 짧은 파이프라인 실행이 가능하기 때문에 데이터 최신성 SLA 에 대한 장애의 영향이 크게 줄어든다.

## Incremental ETL Model

### Read and Join Strategies

ETL 파이프라인에서는 Apache Hudi 로 처리해야할 다양한 유형의 read 와 Join 이 있다. 단일소스에 대한 증분 읽기 뿐아니라, 여러 raw 데이터, 파생된 데이터 및 lock up 테이블에 대한 증분 읽기 및 조인을 포함한다. 또한, 단일 또는 여러 테이블에 대한 backfile 을 위한 스냅샷 읽기도 처리해야한다.

| **Scenario**                                                 | **How is it Handled?**                                       |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| Incremental read on a single source                          | Use Apache Hudi’s incremental reader and upsert to the target table |
| Incremental read + join with multiple raw data tables        | Use Apache Hudi’s incremental read on the main table and perform left outer join on other raw data tables with T-24 hr incremental pull data |
| Incremental read + join with multiple derived and lookup tables | Use Apache Hudi’s incremental read on the main table and perform left outer join on other derived tables fetching only the affected partitions |
| Backfills use case                                           | Use snapshot read on single or multiple tables within etl_start_date and etl_end_date |



### Writes Strategies

파티션된 테이블과 파티션 되지 않은 테이블에 증분 업데이트를 하는 다양한 방법을 살펴본다.

| Type of Table   | How is it Handled                                            |
| --------------- | ------------------------------------------------------------ |
| Partitioned     | – Use upsert to apply only the incremental updates<br/>– Use insert overwrite to update all affected partitions when performing backfill operation<br/>– Use targeted merge/update statements for non-incremental columns using Apache Spark SQL |
| Non-partitioned | – Use upsert to apply only the incremental updates<br />– Use insert overwrite when joining incremental rows with full outer join on target table to update both the incremental and non-incremental columns (to avoid DQ issues on non-incremental columns) |

### Backfill Strategies

스트림 처리 파이프라인과 마찬가지로, 증분 데이터 파이프라인도 비즈니스 로직이 변경되었을때 테이블을 Backfill 하는 방법이 플요하다. Apache Hudi 는 insert_overwrite 와 같은 배치 쓰기 operation 도 지원하고, 소스 테이블에서 스냅샷 읽기로 읽은후 같은 테이블에 concurrent 쓰기하는 방식으로 다룬다. Apache Hudi 는 record Key 와 pre-combine Key 를 지원하는데, 이를 통해 최신 write 가 backfill 에 의해ㅔ 덮어쓰는 것과 같은 사이드 이펙트 없이 backfill 을 간편하게 수행할 수 있다. Apach Hudi 는 증분, backfill write 를 block 없이 제공을해 테이블을 최적화하고 관리하는 테이블 서비스를 동시에 실행할수 있는 기능을 제공한다.

## Implementation

이 섹션에서는 Apache Hudi, Apache Spark 와 Uper 의 워크 플로우 관리 시스템인 Piper 을 사용하여 증분식 파이프라인을 구축하고 관리하는데 필요한 기본요소를 살펴본다. 우버에서는 대규모로 ETL 파이프라인을 관리하고 운영할 수 있도록 Apache Spark ETL 프레임워크를 구축했으며, 이는 Piper를 통해 스케줄되어 있다. 이 프레임 워크는 Apache Hudi의 증분 데이터 처리 도구인 DeltaStreamer 를 기반으로 구축되었다.


> **Piper**
>
> Uber 에서 만든 Airflow 기반 워크플로우 시스템

### Table Definition

```sql
CREATE EXTERNAL TABLE IF NOT EXISTS {{table_name}}
(
  `_hoodie_commit_time`								string,
  `_hoodie_commit_seqno`							string,
  `_hoodie_record_key`								string,
  `_hoodie_partition_path`						string,
  `_hoodie_file_name`								  string,
  ...
)
PARTITIONED BY (datestr string)
ROW FORMAT SERDE 'org.apach.hadoop.hive.ql.io.parquet.serde.PARquetHiveSerDe'
STORED AS INPUTFORMAT 'com.uber.hoodie.hadoop.HoodieInputFormat'
OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.parquet.MapredParquetOutputFormat';
```

### YAML for Defining DeltaStreamer Config

Apache Spark DeltaStreamer 애플리케이션 구성

```yaml
# delta-streamer params
use_delta_streamer: true
drogon_conf:
	conf:
		spark.dynamicAllocation.initialExecutors: "200"
		spark.dynamicAllocation.maxExecutors: "1000"
		spark.executor.memoryOverhead: "4096"   
		spark.sql.hive.filecommit.dynamicPartition0verwrite: "true"
  variables:
		queve: data-warehouse
		source_table: database.trips_table
		source_partition_path: ""
		source_ordering_field: update_timestamp
		hoodie_conf:
			hoodie.datasource.write.recordkey.field: trip_uuid
			hoodie.datasource.write.precombine.field: update_timestamp
			hoodie.datasource.write.partitionpath.field: ""
			hoodie.datasource.write.operation: upsert
			hoodie.datasource.write.keygenerator.class: org.apache.hudi.keygen.SimpleKeyGenerator
```

몇 가지 중요한 구성을 살표보겠다.

- `hoodie.datasource.write.recordkey.field`

  대상 테이블의 기본키, 기본 키에 대해 중복 제거가 실행되며, 중복레코드가 있는경우 `hoodie.datasource.write.precombine.field` 로 식별되는 열의 가장 큰 값을 기준으로 레코드가 단일 레코드로 축소된다.

- `hoodie.datasource.write.operation`

  Upsert는 SQL 변환에서 생성된 페이로드를 사용하여 대상 테이블에서 레코드 수준 업데이트를 수행해야 함을 나타낸다.

### SQL-Based Transformation

파일에는 DeltaStreamer 가 Apache Spark SQL 을 사용하여 실행할 비즈니스 로직이 들어 있다. 최종 페이로드는 대상 테이블에 대한 레코드 수준 업데이트를 수행하는데 사용된다.

```sql
CACHE TABLE tmp_trips AS
SELECT
	*
FROM 
	<SRC>;
...
SELECT
	* 
FROM 
	tmp_trips;
```

`<SRC>`는 증분 읽기 작업이 수행되는 증분 소스를 나타낸다. 새로운 실행될 때마다 DeltaStreamer는 대상 테이블의 Apache Hudi-커밋 메타데이터에 저장된 최신 체크포인트를 확인하고 해당 체크포인트부터 다시 시작하여 업스트림 증분 소스 테이블에서 새 데이터를 읽는다.

### Code Transformations

고급 사용자의 경우 SQL 파일 대신 또는 SQL 파일에 추가하여 사용자 지정 Scala/Java Apache Spark RDD 기반 트랜스포머를 제공하도록 선택할 수 있으며, 이 트랜스포머는 델타스트리머에서 런타임에 실행된다. 이렇게 하려면 Apache Hudi 유틸리티 번들에 있는 트랜스포머 인터페이스를 구현하기만 하면 된다.

```java
public class CustomTransformerExample implements Transformer {

    @Override
    public Dataset<Row> apply(JavaSparkContext javaSparkContext, SparkSession sparkSession,
        Dataset<Row> dataset, TypedProperties typedProperties) {
        Dataset<Row> cityTable = null;
        Dataset<Row> driverData = dataset.select("city_vuid","car tvpe")
        		.where("city_uuid IS NOT NULL AND car_type IS NOT NULL");
				// Example of running SQL using spak session
        cityTable = sparkSession.sql("SELECT city_id, city_name FROM database. city_dimension_table");
        // example of simple group by and aggregation
        Dataset<Row> aggDriverData = driverData.groupBy("city_uuid", "car_type") 
          .agg(count(s:"*").as("num_drivers"));
        // Example of left join of two datafranes to get city name from city dimension table
        aggDriverData = aggDriverData
        		.join(citylable, aggDriverData.col(colName:"city _vuid")           								
                  .equalTo(cityTable.col("city_uuid")), "left")
						.select(aggDriverData.col("*"),cityTable.col("city_name").as("city_name"));
				// Example of casting certain columns to specific data types.
        Dataset<Row> finalData = aggDriverData.selectExpr(
            "cast (city _vid as bigint) as city_uuid",
            "city_name"
            "car_type",
            "cast(num_drivers as bigint) as num_drivers");
        return finalData;
    }
}                               
                              

```

## Impact

### Performance and Cost Savings

아래 표를 보면 배치 ETL 파이프라인을 증분 읽기 및 업서트를 사용하도록 Apache Hudi의 DeltaStreamer로 변환하여 관찰한 큰 성능 향상을 확인할 수 있다. 이 접근 방식을 통해 파이프라인 실행 시간을 50% 단축하고 SLA도 60% 단축할 수 있었다.

### Strong Data Consistency Across Active-Active Data Centers

Uber 는 여러 데이터 센터에 걸쳐 active-active 아키텍처를 가지고 있다. 데이터 불일치에 대한 걱정 없이 워크로드를 실행하기 위해서는 여러 데이터 센터의 테이블 간에 100%의 강력한 데이터 일관성을 달성하는 것이 중요하다.

Apache Hudi로 전환함으로써 여러 데이터 센터간의 데이터 레이크에 걸쳐 강력하고 일관성있는 복제를 구축할 수 있었다. 이를 위해 기본 데이터 센터에서 테이블을 한 번 계산한 다음 Apache Hudi 메타데이터를 사용하여 점진적으로 변경된 파일만 이동하는 리플리케이터 서비스를 사용하여 테이블을 복제했다.

### Improved Data Quality

Apache Hudi는 데이터를 게시하기 전에 사전 로드 데이터 품질 검사를 수행할 수 있는 WAP(쓰기-감사-게시) 패턴을 지원하며, 이 패턴을 사용하면 잘못된 데이터가 프로덕션 데이터 세트에 들어가는 것을 방지할 수 있습니다. Apache Hudi는 사전 커밋 유효성 검사기를 제공하므로, 이를 구성하면 데이터를 게시하기 전에 데이터에 대해 여러 SQL 기반 품질 검사를 실행할 수 있다.

### Improved Observability

Apache Hudi의 DeltaStreamer는 진행 중인 커밋 수, 소비된 커밋 수, 삽입/업데이트/삭제된 총 레코드 수 등과 같이 ETL 실행에서 일어나는 일에 대한 자세한 인사이트를 제공하는 여러 가지 주요 메트릭을 생성한다.

## 결론

Apache Hudi와 증분 ETL의 도움으로 업데이트를 증분적으로 읽고 델타 변경에 대해서만 계산 로직을 실행한 다음 레코드를 Apache Hudi 테이블에 업서트할 수 있다. 증분 데이터 처리 모델이 데이터 엔지니어링 커뮤니티에 가져다주는 리소스 절약, 데이터 최신성 향상, 최적의 데이터 완전성 등 많은 이점이 있으며, 다운스트림 워크로드도 이를 따라갈 수 있습니다. 사실, 이것은 시스템 비용을 절감하는 동시에 향상된 성능을 누릴 수 있는 몇 안 되는 기회 중 하나이다. 

증분 처리가 데이터 레이크하우스의 사실상의 모델이 되어야 한다는 데는 의심의 여지가 없지만, 이를 실현하기 위해서는 아직 해야 할 일이 많이 남아 있다. 예를 들어, Hudi 구현에서는 단일 스트림, 다중 테이블 조인으로 제한했으며, 증분 스캔 또는 전체 스냅샷 스캔으로 사용할 테이블을 선택할 때 비즈니스 도메인에 대한 어느 정도의 이해에 의존했다. Apache Hudi 및 오픈 소스 커뮤니티와 협력하여 프레임워크의 기존 SQL 기능을 최대한 활용하고 Apache Spark 및 Flink와 같은 엔진에서 범용 증분 SQL을 실현할 수 있기를 기대한다.
