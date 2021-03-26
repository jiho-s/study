## 분할 정복

### 목차

1. [도입](#도입)
2. [쿼드 트리 뒤집기](#쿼드-트리-뒤집기)

### 도입

분할 정복은 가장 유명한 알고리즘 디자인 패러다임이다. 주어진 문제를 둘 이상의 부분 문제로 나눈 뒤 각 문제에 대한 답을 재귀 호출을 이용해 계산하고, 각 부분 문제의 답으로부터 전체 문제의 답을 계산해 낸다. 분할 정복이 일반적인 재귀 호출과 다른 점은 문제를 한 조각과 나머지 전체로 나누는 대신 거의 같은 크기의 부분 문제로 나누는 것이다. 

분할 정복을 사용하는 알고리즘들은 세가지의 구성 요소를 갖고 있다.

- 문제를 더 작은 문제로 분할하는 과정
- 각 문제에 대해 구한 답을 원래 문제에 대한 답으로 병합하는 과정
- 더이상 답을 분할하지 않고 곧장 풀 수 있는 매우 작은 문제

분할정복을 적용해 문제를 해결하기 위해서는 문제를 둘 이상으로 나누는 자연스로운 방법이 있어야 하며, 부분 문제의 답을 조합해 원래 문제의 답을 계산하는 효율적인 방법이 있어야 한다.

#### 예제: 수열의 빠른 합과 행렬의 빠른 제곱

##### 수열의 빠른 합

분할 정복을 이용해 1 + 2 + ... + n 의 합을 계산하는 함수를 만들어 보자
$$
fastSum(n) = 1 + 2 + ... + n\\
					= (1 + 2 + ... {n \over 2}) + (({n \over 2} + 1) + ... n)
$$

$$
fastSum(n) = 2 \times fastSum({n \over 2}) + {n^2 \over 4}
$$

```java
    public static int fastNum(int n) {
        if (n == 1) {
            return 1;
        }
        if (n % 2 == 1) {
            return fastNum(n-1) + n;
        }
        return 2*fastNum(n/2) + (n/2)*(n/2);
    }
```

##### 행렬의 거듭제곱

행렬의 곱셈에는 *O(n<sup>3</sup>)* 의 시간이 들기 떄문에 곧이곧대로 *m-1* 번의 곱셈을 통해 *A<sup>m</sup>* 을 구하려면 모두 *O(mn<sup>3</sup>)* 의 연산이 필요하다. 분할정복을 이용하면 빠르게 구할 수 있다.

#### 예제: 병합 정렬과 퀵정렬

이 두 알고리즘은 모두 분할 정복 페러다임을 기반으로 해서 만들어 졌다.

병합 정렬은 주어진 수열을 가운데에서 쪼개 비슷한 크기의 수열 두개로 만든 뒤 이들을 재귀 호출을 이용해 각각 정렬, 그 후 정렬된 배열을 하나로 합침으로써 정렬된 전체 수열을 얻는다. 퀵 정렬 알고리즘은 배열을 단순하게 가운데에서 쪼개는 대신 병합과정이 필요 없도록 한 쪽 의 배열에 포함된 수가 다른쪽 배열으 수보다 항상 작도록 배열을 분할한다.

이 두 알고리즘은 같은 아이디어로 정렬을 수행하지만 시간이 많이 걸리는 작업을 분할 단계에서 하느냐, 병합 단계에서 하느냐가 다르다.

### 쿼드 트리 뒤집기

#### 문제

쿼드 트리는 2<sup>n</sup> * 2<sup>n</sup> 크기의 흑백 그림을 다음과 같은 과정을 거쳐 문자열로 압축한다.

- 이 그림의 모든 픽셀이 검은 색일 경우 이 그림의 쿼드 트리 압축결과는 그림의 크기에 관계없이 b가 된다.
- 이 그림의 모든 픽셀이 흰 색일 경우 이 그림의 쿼드 트리 압축 결과는 그림의 크기에 관계없이 w가 된다.
- 모든 픽셀이 같은 색이 아니라면, 쿼드 트리는 이 그림을 가로 세로 각각 2 등분해 4개의 조각으로 쪼갠 뒤 각각을 쿼드 트리 압축한다. 이때 전체 그림의 압축 결과는 x(왼쪽 위 부분의 압축결과)(오른쪽 위 부분의 압축 결과)(왼쪽 아래 부분의 압축 결과)(오른쪽 아래 부분의 압축 결과)가 된다.

쿼드 트리로 압축된 흑백 그림이 주어졌을 떄 이그림을 상하로 뒤집은 그림을 쿼드 트리 압축해서 출력하는 프로그램을 작성하세요

##### 시간 및 메모리 제한

프로그램은 1초 안에 실행되어야 하며, 64MB 이하의 메모리를 사용해야 한다.

##### 입력

첫 줄에 테스트 케이스의 개수 가 주어진다. 그후 한 줄에 하나씩 쿼드 트리로 압축한 그림이 주어진다. 

##### 출력

각 테스트 케이스 당 한 줄에 주어진 그림을 상하로 뒤집은 결과를 쿼드 트리 압축해서 출력한다. 

##### 예제 입력

```tex
4
w
xbwwb
xbwxwbbwb
xxwwwbxwxwbbbwwxxxwwbbbwwwwbb
```

```
w
xwbbw
xxbwwbbw

```
