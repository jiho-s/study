## 알고리즘의 시간 복잡도 분석

### 목차

1. [도입](#도입)
2. [선형 시간 알고리즘](#선형-시간-알고리즘)
3. [선형 이하 시간 알고리즘](#선형-이하-시간-알고리즘)
4. [지수 시간 알고리즘](#지수-시간-알고리즘)

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

### 선형 이하 시간 알고리즘

입력으로 주어진 자료에 대해 우리가 미리 알고 있는 지식을 활용하면 구할 수 있다.

입력 값이 정렬되어 있는 경우 log2N 갯수만 확인하면된다. 입력의 크기가 커지는 것보다 수행시간이 느리게 증가하는 알고리즘들을 선형 이하 시간 알고리즘으라고 부른다.

#### 이진 탐색

위의 예에서 사용한 알고리즘을 이진 탐색이라고 부른다.

### 지수 시간 알고리즘

#### 다항시간 알고리즘

반복문의 수행 횟수를 입력 크기의 다항식으로 표현할 수 있는 알고리즘을 다항 시간 알고리즘이라고 부른다. 이 장에서 다루는 대다수의 알고리즘들은 다항 시간이 된다.

#### 지수 시간 알고리즘

```java
    int m;
    int selectMenu(Stack<Integer> menu, int food) {
        if (food == m) {
            if (!canEverybodyEat()){
                return -1;
            }
            return menu.size();
        }
        // 이 음식을 만들지 않는 경우
        int exceptCurrent = selectMenu(menu, food + 1);
        menu.push(food);
        int containCurrent = selectMenu(menu, food + 1);
        menu.pop();
        return min(containCurrent, containCurrent);


    }
```

위 프로그램은 모든 답을 한 번씩 다 확인하기 때문에, 전체 걸리는 시간은 만들 수 있는 답의 수에 비례하게된다. 만들수 있는 답은 모두 2<sup>M</sup>가지이다. 이 알고리즘의 수행시간은  2<sup>M</sup>에 `canEverybodyEaty()`의 수행시간을 곱한것이 된다.

이와 같이 *N*이 하나 증가할 때마다 걸리는 시간이 배로 증가하는 알고리즘을 지수 시간에 동작한다고 말한다.

이런 문제는 다항시간 알고리즘이 존재한다는 증거도 존재하지 않는 다는 증거도 발견되지 않았다.

##### 소인수 분해의 수행시간

입력으로 주어지는 숫자의 개수가 아니라 그 크기에 따라 수행시간이 달라지는 알고리즘들 또한 지수 수행 시간을 가질 수 있다.

```

```

