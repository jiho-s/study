## 분할 정복

### 목차

1. [도입](#도입)

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
fastSum() = 1 + 2 + ... + n\\
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
