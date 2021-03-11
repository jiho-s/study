## 동적 계획법

### 목차

1. [네트워크 선 자르기(Bottom-Up)](#네트워크-선-자르기-bottom-up)
2. [네트워크 선 자르기 (Top-Down)](#네트워크-선-자르기-top-down)
3. [돌다리 건너기](#돌다리-건너기)
4. [최대 부분 증가수열](#최대-부분-증가수열)
5. [최대 선 연결하기](#최대-선-연결하기)

### 네트워크 선 자르기 Bottom-Up

#### 문제

현수는 네트워크 선을 1m, 2m의 길이를 갖는 선으로 자르려고 합니다. 예를 들어 4m의 네트워크 선이 주어진다면

1. 1m+1m+1m+1m
2. 2m+1m+1m
3. 1m+2m+1m
4. 1m+1m+2m
5. 2m+2m

의 5가지 방법을 생각할 수 있습니다. (2)와 (3)과 (4)의 경우 왼쪽을 기준으로 자르는 위치가 다르면 다른 경우로 생각한다.

그렇다면 네트워크 선의 길이가 Nm라면 몇 가지의 자르는 방법을 생각할 수 있나요?

#### 풀이

```java
public String solution1(int n) {
    int [] list = new int[n+1];
    list[1] = 1;
    list[2] = 2;
    IntStream.rangeClosed(3, n).forEach(i -> {
        list[i] = list[i-2] + list[i-1];
    });
    return String.valueOf(list[n]);

}
```

### 네트워크 선 자르기 Top-Down

#### 문제

현수는 네트워크 선을 1m, 2m의 길이를 갖는 선으로 자르려고 합니다. 예를 들어 4m의 네트워크 선이 주어진다면

1. 1m+1m+1m+1m
2. 2m+1m+1m
3. 1m+2m+1m
4. 1m+1m+2m
5. 2m+2m

의 5가지 방법을 생각할 수 있습니다. (2)와 (3)과 (4)의 경우 왼쪽을 기준으로 자르는 위치가 다르면 다른 경우로 생각한다.

그렇다면 네트워크 선의 길이가 Nm라면 몇 가지의 자르는 방법을 생각할 수 있나요?

#### 풀이

```java
public String solution2(int n) {
    int [] list = new int[n+1];
    list[1] = 1;
    list[2] = 2;
    int answer = solution2Recursion(list, n);
    return String.valueOf(answer);
}
private int solution2Recursion(int [] list, int n) {
    if (list[n] != 0) {
        return list[n];
    }
    list[n] = solution2Recursion(list, n -1) + solution2Recursion(list, n - 2);
    return list[n];
}
```

### 돌다리 건너기

#### 문제

철수는 학교에 가는데 개울을 만났습니다. 개울은 N개의 돌로 다리를 만들어 놓았습니다. 철 수는 돌 다리를 건널 때 한 번에 한 칸 또는 두 칸씩 건너뛰면서 돌다리를 건널 수 있습니다. 철수가 개울을 건너는 방법은 몇 가지일까요?

#### 풀이

```java
public String solution3(int n) {
    int [] list = new int[n+2];
    list[1] = 1;
    list[2] = 2;
    IntStream.rangeClosed(3, n+1).forEach(i -> {
        list[i] = list[i-2] + list[i-1];
    });
    return String.valueOf(list[n+1]);
}
```

### 최대 부분 증가 수열

#### 문제

N개의 자연수로 이루어진 수열이 주어졌을 때, 그 중에서 가장 길게 증가하는(작은 수에서 큰 수로) 원소들의 집합을 찾는 프로그램을 작성하라. 예를 들어, 원소가 2, 7, 5, 8, 6, 4, 7, 12, 3 이면 가장 길게 증가하도록 원소들을 차례대로 뽑아내면 2, 5, 6, 7, 12를 뽑아내어 길 이가 5인 최대 부분 증가수열을 만들 수 있다.

#### 풀이

```java
public String solution4(List<Integer> list) {
    List<Integer> table = new ArrayList<>();
    table.add(1);
    int result = IntStream.range(1, list.size()).map(i -> {
        int max = IntStream.range(0, table.size())
                .filter(j -> list.get(j) < list.get(i))
                .map(table::get).max().orElse(0);
        max++;
        table.add(max);
        return max;
    }).max().orElse(0);
    return String.valueOf(result);
}
```

### 최대 선 연결하기

#### 문제

왼쪽의 번호와 오른쪽의 번호가 있는 그림에서 같은 번호끼리 선으로 연결하려고 합니다. 왼쪽번호는 무조건 위에서부터 차례로 1부터 N까지 오름차순으로 나열되어 있습니다. 오른쪽의 번호 정보가 위부터 아래 순서로 주어지만 서로 선이 겹치지 않고 최대 몇 개의 선 을 연결할 수 있는 지 구하는 프로그램을 작성하세요.

#### 풀이

```java
public String solution4(List<Integer> list) {
    List<Integer> table = new ArrayList<>();
    table.add(1);
    int result = IntStream.range(1, list.size()).map(i -> {
        int max = IntStream.range(0, table.size())
                .filter(j -> list.get(j) < list.get(i))
                .map(table::get).max().orElse(0);
        max++;
        table.add(max);
        return max;
    }).max().orElse(0);
    return String.valueOf(result);
}
```