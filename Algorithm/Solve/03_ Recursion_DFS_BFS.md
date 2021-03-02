## 재귀, DFS, BFS

### 목차

56. [재귀함수 분석](#재귀함수-분석)
57. [재귀함수 이진수 출력](#재귀함수-이진수-출력)
58. [이진트리 깊이우선탐색](#이진트리-깊이우선탐색)
59. [부분집합(DFS)](#부분집합)
60. [합이 같은 부분집합(DFS)](#합이-같은-부분집합)

### 재귀함수 분석

#### 문제

자연수 N이 주어지면 아래와 같이 출력하는 프로그램을 작성하세요. 재귀함수를 이용해서 출력해야 합니다.

첫 번째 줄에 자연수 N(1<=N<=20)이 주어집니다.

첫 번째 줄에 재귀함수를 이용해서 출력하세요.

#### 풀이

```java
public String solution56(int num) {
    StringBuilder stringBuilder = new StringBuilder();
    solution56_recursive(stringBuilder, num);
    return stringBuilder.toString();
}
private void solution56_recursive(StringBuilder stringBuilder, int num) {
    if (num == 0) {
        return;
    }
    solution56_recursive(stringBuilder, num-1);
    stringBuilder.append(num).append(" ");
}
```

### 재귀함수 이진수 출력

#### 문제

10진수 N이 입력되면 2진수로 변환하여 출력하는 프로그램을 작성하세요. 단 재귀함수를 이용해서 출력해야 합니다.

#### 풀이

```java
public String solution57(int num) {
    return solution57_recursive(num);
}

private String solution57_recursive(int num) {
    if (num == 0) {
        return "";
    }
    return solution57_recursive(num / 2) + num % 2;
}
```

### 이진트리 깊이우선탐색

#### 문제

이진트리를 전위순회와 후위순회를 연습해보세요.

#### 풀이

```java
public String solution58() {
    return solution58_recursive(1);
}

private String solution58_recursive(int num) {
    if (num > 7) {
        return "";
    }
    return num + solution58_recursive(num*2) + solution58_recursive(num*2+1);
}
```

### 부분집합

#### 문제

자연수 N이 주어지면 1부터 N까지의 원소를 갖는 집합의 부분집합을 모두 출력하는 프로그램을 작성하세요.

#### 풀이

```java
public String solution59(int num) {
    boolean [] mark = new boolean[num+1];
    StringBuilder stringBuilder = new StringBuilder();
    solution59_recursive(stringBuilder, 1, num, mark);
    return stringBuilder.toString();
}

private void solution59_recursive(StringBuilder stringBuilder, int n, int num, boolean [] mark) {
    if (n == num + 1) {
        String collect = IntStream.rangeClosed(1, num).filter(i -> mark[i]).mapToObj(String::valueOf).collect(Collectors.joining(" "));
        stringBuilder.append(collect).append("\n");
        return;
    }
    mark[n] = true;
    solution59_recursive(stringBuilder, n + 1, num, mark);
    mark[n] = false;
    solution59_recursive(stringBuilder, n + 1, num, mark);
}
```

### 합이 같은 부분집합

#### 문제

N개의 원소로 구성된 자연수 집합이 주어지면, 이 집합을 두 개의 부분집합으로 나누었을 때 두 부분집합의 원소의 합이 서로 같은 경우가 존재하면 “YES"를 출력하고, 그렇지 않으면 ”NO"를 출력하는 프로그램을 작성하세요.

 예를 들어 {1, 3, 5, 6, 7, 10}이 입력되면 {1, 3, 5, 7} = {6, 10} 으로 두 부분집합의 합이 16으로 같은 경우가 존재하는 것을 알 수 있다.

#### 풀이

```java
public String solution60(List<Integer> list) {
    int total = list.stream().mapToInt(Integer::intValue).sum();
    boolean result = solution60_recursive(list, 0, 0, total / 2);
    return result ? "YES" : "NO";
}

private boolean solution60_recursive(List<Integer> list, int index, int sum, int want) {
    if (sum == want) {
        return true;
    } else if (index == list.size()) {
        return false;
    }
    return solution60_recursive(list, index+1, sum, want) || solution60_recursive(list, index +1, sum + list.get(index), want);
}
```