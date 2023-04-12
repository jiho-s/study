# Cost-Efficient Open Source Big Data Platform at Uber

## Big Data File Format Optimizations

우버에서 Apache Hadoop 파일 시스템의 대부분은 Apache Hive table 이 사용하고 Apache Parquet 형식 또는 Apach ORC 형식으로 저장되어 있다.

추후 모든 포맷을 Apache Parquet 형식으로 전환 예정이다.

Parquet, ORC 모두 block-based columnar format 이다.

우버는 Parquet format 을 기준으로 최적화를 진행

> **Apache ORC vs Apache Parquet**
>
> 둘다 컬럼기반 저장포맷 ORC 는 Hive 특화 포맷 Parquet 은 좀더 범용

### 압축 알고리즘

Parquet은 기본적으로 GZIP Level 6 를 압축알고리즘을 실행. 최근, Parquet 이 ZSTD 를 지원하면서 실험을 해보았다. ZSTD Level 9 와 Level19 가  GZIP 에 비해 Parquet file size 를 8% 12% 줄이는 것을 확인했다. 심지어 압축해제속도가 GZIP 에 비해 더빨랐다.

데이터가 1개월이 지나면 ZSTD Level 9 로 재압축하고, 3개월 뒤에는 Level19로 재압축한다. ZSTD Level9 압축속도가 ZSTD Level19 압축속도보다 3배더 빠르다.

### 열 삭제

Hive table 들 특히 Kafka log 로 된 Hive table 들은 많은 column을 가지고 있고 이중 일부는 nested 되어있다. 이 중 일부는 오랜기간 보관하는 것이 필요없다. 컬럼기반 저장포맷 방식이므로 압축해제, 재압축 과정없이 열을 삭제하는 것은 기술적으로 가능하다. 그래서 Uber가 이런 기능들을 Apache Parquet 코드에 기여했다.

### 행 재정렬

행을 재정렬 하는 것으로 Parquet files 의 압축된 사이즈를 크게 줄수 있다. Parquet format 압축 알고리즘의 기능 userId 로 정렬하고, timestamp 순으로 정렬하는 방법을 통해서도 압축된 사이즈를 크게 줄일수 있다. 

### Delta Encoding

행을 timestamp 로 정렬하면서, 델타 인코딩을 사용하면 data size 를 더 줄일수 있다는 생각이 들었다. 하지만 Hive, Presto, Spark 를 모두 사용하는 환경에서 델타 인코딩을 사용하는 것이 쉽지 않아 관련 방안을 모색중이다.

> **Delta Encoding**
>
> 열에서 서로 인접한 값 사이의 차이를 기록하여 데이터를 압축한다.

## HDFS 이레이저 코딩

이레이저 코딩은 HDFS files 의 복제본 갯수를 획기적으로 줄일수 있다. 갑작스로운 IOPS 증가에 대비해서 Uber 에서는 복제본 갯수를 3+2 또는 6+3 방식 (복제본 갯수가 1.67x, 1.5x) 을 사용하고 있다. 기본 HDFS 복제본 갯수가 3x 인것에 비해 많은 HDD 사이지를 줄일수 있다.

> **이레이저 코딩**
>
> 이레지저코드를 사용해 데이터를 인코딩하고, 데이터가 손실되면 디코딩 과정을 거쳐 원본 데이터를 복구하는 기법 중 하나

이레이저 코딩을 사용할때 여러 옵션이 있다.

### Apache Hadoop 3.0 HDFS Erasure Code

Apache Hadoop 3.0 에서 제공하는 공식 이레이저 코딩 크고 작은 파일에서 모두 작동하지만, 블록이 매우 조각화되어 있어 IO 효율이 좋지 않다.

### Client-side Erasure Code

Facebook 이 HDFS-RAID project 에서 제공, IO 효율이 좋다. 모든 블록을 사용할 수 있으면 3개의 복제본을 가진 HDFS 와 IO 속도가 같다. 하지만 큰 파일에서만 작동하고 작은 파일에서는 작동하지 않는다.

Hadoop 커뮤니티의 방향이므로 Uber 는 Apache Hadoop 3.0 HDFS Erasure Code 를 사용하기로 결정했다.

## YARN Scheduling Policy Improvements

우버에서는 빅데이터 컴퓨팅 워크로드를 YARN 을 통해관리하고, YARN 의 표준 Capacity Scheduler 를 사용하고 있었다. Capacity Scheduler에서는 각각의 큐에 대한 MIN, MAX 값 설정을 계층형 큐 구조를 구성할 수 있었다. 하지만 곧 cluster capacity 를 관리하는데 딜레마에 빠졌다.

1. 높은 사용률: YARN 클러스터의 평균 사용률을 가능한 높게 유지하고 싶음
2. 사용자 기대치 충족: 사용자에게 클러스터에서 사용할수 있는 리소스 양에대한 정확한 기대치 제공

많은 사용자들이 까다롭지만 예측 가능한 YARN 클러스터의 리소스에 대한 요구사항을 가지고 있다. 예를들어 하나의 큐에 특정시간에 시작하여 비슷한 시간이 걸리고 비슷한 CPU/MemGB 용량을 사용하는 일일 작업이 있을 수 있다.

큐의 MIN 값을 하루 최대 사용량으로 설정하면 큐의 평균 리소스 유구사항이 MIN 값 보다 훨신 낮기 떄문에 사용률이 낮아진다.

큐의 NAX 값을 하루 최대 사용량으로 설정하면 시간이 지남에 따라 큐가 악용되어 계속 MAX 에 가까운 리소스를 사용할 가능성이 있으므로, 다른 큐에 있는 모든 작업에 영향을 줄수 있다.

이런 리소스 요구 사항을 파악하고 기대치를 올바르게 설정하기 위해 Dynamic Max 알고리즘을 사용했다.

```
Dynamic_MAX = max(MIN, MIN * 24 – Average_Usage_In_last_23_hours * 23)
```

> **YARN**
>
> 이전에는 Hadoop 내에 MapReduce 를 통해서만 작업 MapReduce 의 Master 서버 역할을 수행하는 JobTracker 에의 해 리소스 상태가 관리되서 Hadoop이 설치된 클러스터 서버들의 리소스를 사용하고자 하는 다른 컴퓨터 클러스터와 연동하는데 어려움이 있어 클러스터의 리소스관리 등을 YARN 에 의해 관리되도록 변경

## Avoid the Rush Hours

YARN 리소스 사용률의 다른 문제는 전체 클러스터 레벨에 서 일정한 패턴이 존재한다는 것이다. 많은 팀들이 ETL 을 00:00 ~ 01:00 UTC 에 돌리는데(하루의 마지막 log 가 다 준비되서) YARN 클러스터가 이시간에 제일 바쁘다.

YARN 클러스터의 머신 개수를 늘리는데신, 시간 기반 요금을 구현할 계획이다. 23시간동안 평균 사용량을 계산할때 0~4(UTC) 에는 2x 를 사용했다고 하고 다른 시간에는 0.8을 사용했다고 한다.

## Cluster of Clusters

YARN과 HDFS 클러스터가 계속 커지면서 성능 병목 현상이 나타나기 시작했다. 클러스터 규모가 계속 커지면서 HDFS NameNode와 YARN ResourceManager 모두 속도가 느려지기 시작했다. 이는 주로 확장성 문제이지만, 비용 효율성 목표에도 큰 영향을 미쳤다.

이 문제를 해결하기 위해 두가지 옵션이 있다.

1. 단일 노드의 성능을 지속적으로 개선한다.

   더 좋은 성능을 가진 머신 사용, 성능 병목 지점을 최적화

2. Cluster of Clusters(Federation)

   여러 클러스터로 구성된 가상의 클러스터 제작, 아래의 클러스터는 HDFS와 YARN 의 최적화된 성능을 가지고, 가상의 클러스터가 라우팅 로직을 처리한다.

다음과 같은 이유로 두번째 옵션을 선택함

초대형 클러스터를 실행하면 작은 규모의 클러스터에서는 나타나지 않는 알려지지 않은 버그가 발생할 가능성이 높다.

HDFS과 YARN 스케일을 우버의 클러스터 사이즈에 맞게 하면, 성능과 복잡한 기능간에 트레이드 오프를 위해 소스코드를 변경해야할수 있다. 하지만 다른 회사에서 이러한 복잡한 기능이 필요로 할수 있기 떄문에 이를 제거하기 위한 코드 변경을 Hadoop에 merge 할수 없다.

Fork 없이 Hadoop 오픈소스 ecosystemd을 활용하기 위해 cluster of clusters 를 설정했다.

특히 라우터 기반 HDFS Federation 과 YARN Federation 을 사용한다. 이거 다 Hadoop 오픈소스에서 가져왔다.

## Generalized Load Balancing

HDFS 와 YARN 모두 적용되는 일반화된 로드 밸런싱 문제 해결에 대해 설명한다.

- HDFS 데이터노드 디스크 공간 사용률 밸런싱

  각 데이터 노드마다 디스크 공간 사용률이 다를 수 있다. 데이터 노드 안에서도 디스크 공간 사용률이 다를수 있따. 평균 공간 사용률을 높이려면 이러한 모든 요소의 균형을 맞춰야 한다.

- YARN NodeManager 사용률 밸런싱

  어느 순간에든 YARN의 각 머신은 CPU 와 MemGB 할당 및 사용률이 서로 다를 수 있다. 다시 말하지만 높은 평균 사용률을 위해서는 할당과 사용률 사이의 균형을 맞춰야 한다.

위 두 문제의 해결방법의 유사성은 마이크로서비스 로드 벨런싱, 기본 스토리지 로드 벨런싱 등 빅데이터 플랫폼 뿐 아니라 더 많은 사례에 적용되든 일반화된 로드 밸런싱 아이디어로 이어진다. 이 모든 문제의 해결방법의 공통점은 P99 와 평균 간의 격차를 줄이는 것이 목표다.

## Query Engines

Uber 에서는 쿼리엔진으로 Hive-on-Spark, Spark, Presto 를 사용한다. Parquet 과 ORC 등의 파일 포멧과 쿼리엔진을 사용해서 비용 효율성을 위한 트레이드 오프 기반을 만들었다. SparkSQL, Hive-on-Tez 등을 사용하면서 이러한 결정을 더 어렵게 한다.

다음은 쿼리 엔진의 비용 효율성을 개선하기 위한 주요 노력이다.

> **Hive on Tez**
>
> YARN 기반의 데이터 처리를 위한 프레임워크 MapReduce 대체 Map 단계 의 결과를 메모리에 저장후 Reduce로 넘겨 MapReduce 보다 빠르다.

- Focus on Parquet File Format

  Parquet 과 ORC 파일 포맷은 row groups, columnar storage, block-level, 파일 레벨 통계와 같은 공통된 설계원칙을 공유한다. 하지만 구현은 완전히 달라서 다른 소유 시스템과의 호환성이 다르다. 시간이 지남에 따라 Spark 에서는 Parquet 지원이 개선되었고, Presto 에서는 ORC 지원이 개선되었다. 파일 포맷에 기능을 추가하려는 요구가 증가함에 따라 major 한 file 포맷을 결정해야 했고, Parquet 을 선택했다. 하나의 포맷을 선택한 결과 하나의 코드베이스에 리소스를 집중시키고 전문성을 키울수 있었다.

- Nested Column Pruning

  Uber의 빅데이터 테이블에는 많은 nested data 가 존재한다. 많은 업스트림 데이터 세트가 JSON 형식으로 저장되어 있고 (Designing Schemaless), 이러한 데이터에 Avro 스키마를 적용하고 있기 떄문이다. 그결과 Nested Column Pruning 을 지원하는 것을 Uber 에서 사용하는 쿼리 엔진의 핵심기능이며, 지원하지 않으면 Nested Column 데이터중 일부만 읽는 경우에도 Parquet 파일에서 모든 Nested Column 데이터를 읽어야 한다. 그래서 Nested Column Pruning 기능을 Spark와 Presto에 모두 추가했다. 이를 통해 전반적인 쿼리 성능이 크게 개선되었으며, 오픈소스 커뮤니티에 기여하고 있다.

- Common Query Pattern Optimization

  천줄에 가까운 SQL 쿼리를 보는 것은 드문 일이 아니다. 쿼리엔진에는 모두 쿼리 최적화 도구가 있지만, Uber 에서 흔히 사용하는 패턴을 모두 처리해주지는 않는다. Presto 와 같은 엔진은 내장 함수를 사용하도록 쿼리를 재작성하면 훨씬 빠르게 실행할 수 있다.

> **Avro 스키마**
>
> json 형식으로 스키마를 지정

경험상 어떤 엔진이 특정 쿼리에 더 적합한지 예측하기 어렵다. Hive-on-Spark 는 대량의 데이터에 대해 확장성이 뛰어나고, Presto는 일반적으로 작은양의 데이터를 다루는 쿼리에서 매우 빠르다. 오픈 소스 빅데이터 쿼리 엔진의 개선 사항을 주시하고 있으며, 비용 효율성을 위해 계속 워크로드를 여러 쿼리 엔진 사이에서 전환할 것이다.

## Apache Hudi

빅데이터 플랫폼에서 가장 크게 비용 효율성을 높이는 방법 중 하나는 효과적인 증분 처리이다. 많은 실제 데이터 세트들이 늦게 도착하거나 바뀔수 있다.

효율적인 증분 처리 프레임워크가 없으면, 빅데이터 사용자는 쿼리 결과를 최신화하기 위해 매일 많은 이전 날짜의 데이터를 스캔해야 한다. 훨씬더 효율적인 방법은 매일 증분 변경만 처리하는 것이다. 이러한 프로젝트가 Hudi 프로젝트이다.

대부분의 빅데이터를  HDFS 에 Hudi 포맷으로 저장하고 있다. 이를 통해 Uber 에서 필요로 하는 컴퓨팅 요량을 획기적으로 줄였다.

## Next Steps and Open Challenges

### Big Data and Online Service Same-Host Colocation

온라인 서비스가 host를 필요로 하지 않을때 Big Data Workload 가 이를 사용하기로 결정했지만, 두 워크로드를 동일한 host 에서 실행하면 많은 문제가 발생할 수 있다.

많은 연구중에 있으며, 온라인 서비스의 영향을 최소로 주기 위해 빅데이터 워크로드의 우선순위를 낮게 설정하는 방법을 사용하려 하고 있다.

### Convergence of the Online and Analytics Storage

많은 데이터 세트들이 온라인 스토리지 시스템(flash Schmaless stored Mysql) 과 분석 스토리지 시스템(하드디스크에 저장된 Hive tables stored in HDFS)에 모두 저장되어 있다. 또한 빠른 쿼리 속도를 위해 Pinot 과 같은 스토리지 엔진도 사용중이다. 이런 데이터들이 서로 다른 포맷으로 저장되어 있지만 논리적으로 동일한 데이터인 사본이다.

통합 시스템을 만들면 비용을 크게 절감 할 수 있다.... 할수 있으면

### Project HydroElectricity: 유지 관리 작업을 활용하여 여분의 컴퓨팅 파워 저장

클러스터의 컴퓨팅 파워는 전기 공급과 유사하다. 일반적으로 공급량이 고정되어 있으며 수요가 급등하거나 일정하지 않은 상황에서 어려움을 격는다.

양수발전은 잉여 전력을 물의 위치에너지로 저장했다가 수요가 급등할때 다시 전기로 변환할 수 있다.

같은 아이디어를 컴퓨팅 파워에도 적용할 수 있다. 핵심 아이디어는 백그라운드 작업인 유지 관리 작업이 언제든 처리 할 수 있다는 것이다. 일반적인 유지 관리 작업은 LSM 압축, 압축 secondary index 생성, 데이터 스크러빙, erasure code 수정, 스냅샸 유지 관리 등이 있다. SLA 가 없는 모든 우선순위가 낮은 작업은 유지 관리 작업으로 간주할 수 있다.

대부분의 시스템에서 유지관리 작업과 포그라운드 작업을 명시적으로 구분하지 않는다. 예를 들어 우버의 빅데이터 수집 시스템은 CPU 파워를 많이 사용하고 매우 압축된 파일을 생성하는 ZSTD 압축 Parquet 파일을 생성한다. 이렇게 하는 대신 디스크 공간을 조금 더 사용하지만 CPU 를 훨씬 적게 사용하는 가볍게 압축된 Parquet 파을을 생성할 수 도 있다. 그런 다음 나중에 파일을 다시 재압축하는 유지관리 작업이 있을수 있다. 이렇게 하면 포그라운드 CPU 요구량을 크게 줄일 수 있다.

유지관리작업은 돌아갈 컴퓨터 파워가 보장되지 않아도 수행할 수 있다. 이러한 리소스는 많이 있다.

### Pricing Mechanism for Big Data Usage

여러명이서 사용하는 빅데이터 플랫폼을 고려할때, 모든 고객의 리소스 요구 사항을 충족하기 어려운 상황이 많이 있다. 제한된 하드웨어 리소스의 총 효용을 최적화하는 방법을 고민 중이다. Dynamic_MAX 가 최선의 선택이 아닐꺼라고 생각한다.

이를 위해서 보다 섬세한 가격 책정 메커니즘을 마련해야한다. 각 팀이 클러스터에서 사용할 수 있는 가짜 돈 등을 고려할 수 있다