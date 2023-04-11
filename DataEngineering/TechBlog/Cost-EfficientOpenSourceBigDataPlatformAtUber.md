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

Hive table 들 특히 Kafka log 로 된 Hive tavle 들은 중첩된 열들을 많이 가지고 있다. 이 열들은 오랜기간 보관하는 것이 필요없다. 컬럼기반 저장포맷 방식이므로 압축해제, 재압축 과정없이 열을 삭제하는 것은 기술적으로 가능하다. 그래서 Uber가 이런 기능들을 Apache Parquet 코드에 기여했다.

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

> YARN
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