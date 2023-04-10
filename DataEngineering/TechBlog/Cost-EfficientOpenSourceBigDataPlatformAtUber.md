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