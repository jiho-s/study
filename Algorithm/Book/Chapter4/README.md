## 알고리즘의 시간 복잡도 분석

### 목차

1. [도입](#도입)
2. [선형 시간 알고리즘](#선형-시간-알고리즘)

### 도입

알고리즘의 속도를 측정할 수 없다면 알고리즘을 바꿨을 때 이것이 더 빨라졌는ㄴ지 더 느려졌는지도 알 수 없다.

#### 반복문이 지배한다

입력의 크기가 커지면 커질수록 반복문이 알고리즘의 수행시간을 지배하게 된다.

### 선형 시간 알고리즘

#### 다이어트 현황 파악: 이동 평균 계산하기

시간에 따라 관찰된 숫자들이 주어질 때 M-이동 평군은 마지막 M개의 좐찰 값의 평균으로 정의된다. 따라서 새 관찰 값이 나오면 M-이동 평균은 새 관찰 값을 포함하도록 바뀐다.

각 위치에서 지난 M개 측정치의 합을 구하고, 이를 M으로 나누면 된다.

```java
    List<Double> getMovingAverage1(List<Double> weights, int m) {
        List<Double> result = new ArrayList<>();
        int len = weights.size();

        for (int i = m -1; i < len; i++) {
            double partialSum = 0;
            for (int j = 0; j < m; j++) {
                partialSum += weights.get(i-j);
            }
            result.add(partialSum / m);
        }
        return result;
    }
```

이때 이 코드의 수행시간은 두개의 for문에 의해 지배된다. 전체 반복문은 `M * (N-M+1)` 번 수행된다.

좀더 빠를 프로그램을 알아보자 여기에서 중요한 아이디어는 중복된 계산을 없애는 것

```java
    List<Double> getMovingAverage2(List<Double> weights, int m) {
        List<Double> result = new ArrayList<>();
        int len = weights.size();
        double partialSum = 0;
        for (int i = 0; i < m-1; i++) {
            partialSum += weights.get(i);
        }
        for (int i = m-1; i < len; i++) {
            partialSum += weights.get(i);
            result.add(partialSum / m);
            partialSum -= weights.get(i-m+1);
        }
    }
```

이 프로그램의 반복문 수행 횟수는 `M-1+(N-M+1) = N` 이된다. 코드의 수행시간은 Nd에 정비례한다. 이런 알고리즘을 선형 시간 알고리즘이로 부른다. 선형 시간에 실행되는 알고리즘은 대개 우리가 찾을 수 있는 알고리즘 중 가장 좋은 알고리즘은 경우가 많다.

#### 선형 이하 시간 알고리즘

입력으로 주어진 자료에 대해 우리가 미리 알고 있는 지식을 활용하면 구할 수 있다.

입력 값이 정렬되어 있는 경우 log2N 갯수만 확인하면된다. 입력의 크기가 커지는 것보다 수행시간이 느리게 증가하는 알고리즘들을 선형 이하 시간 알고리즘으라고 부른다.

##### 이진 탐색

위의 예에서 사용한 알고리즘을 이진 탐색이라고 부른다.