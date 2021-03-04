## 재귀, DFS, BFS

### 목차

56. [재귀함수 분석](#재귀함수-분석)
57. [재귀함수 이진수 출력](#재귀함수-이진수-출력)
58. [이진트리 깊이우선탐색](#이진트리-깊이우선탐색)
59. [부분집합(DFS)](#부분집합)
60. [합이 같은 부분집합(DFS)](#합이-같은-부분집합)
61. [특정 수 만들기(DFS)](#특정-수-만들기)
62.  
63. 
64. [경로 탐색(DFS)](#경로-탐색)
65. [미로탐색(DFS)](#미로탐색)
66.  
67. [최소비용(DFS)](#최소비용)
68.  
69. [이진트리 넓이우선탐색(BFS)](#이진트리-넓이우선탐색)
70. [그래프 최단거리(BFS)](#그래프 최단거리)

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

### 특정 수 만들기

#### 문제

N개의 원소로 구성된 자연수 집합이 주어지면, 집합의 원소와 ‘+’, ‘-’ 연산을 사용하여 특정 수인 M을 만드는 경우가 몇 가지 있는지 출력하는 프로그램을 작성하세요. 각 원소는 연산에 한 번만 사용합니다.
 예를 들어 {2, 4, 6, 8}이 입력되고, M=12이면
$$
2+4+6=12\\
 4+8=12\\
 6+8-2=12\\
 2-4+6+8=12
$$

 로 총 4가지의 경우가 있습니다. 만들어지는 경우가 존재하지 않으면 -1를 출력한다.

#### 풀이

```java
public String solution61(int num, List<Integer> list) {
    int answer = solution61_recursive(num, list, 0, 0);
    return answer != 0 ? String.valueOf(answer) : "-1";
}
private int solution61_recursive(int num, List<Integer> list, int index, int sum) {
    int result = 0;
    if (list.size() == index) {
        if (sum == num) {
            result++;
        }
        return result;
    }
    result += solution61_recursive(num, list, index + 1, sum + list.get(index));
    result += solution61_recursive(num, list, index + 1, sum - list.get(index));
    result += solution61_recursive(num, list, index + 1, sum);
    return result;
}
```

### 경로 탐색

#### 문제

방향그래프가 주어지면 1번 정점에서 N번 정점으로 가는 모든 경로의 가지 수를 출력하는 프로그램을 작성하세요

#### 풀이

```java
public String solution64(List<List<Integer>> table) {
    boolean [] mark = new boolean[table.size()];
    mark[0] = true;
    int answer = solution64Recursive(table, mark, 0);
    return String.valueOf(answer);
}

private int solution64Recursive(List<List<Integer>> table, boolean [] mark, int current) {
    int result = 0;
    if (current == mark.length - 1) {
        return 1;
    }
    result += table.get(current).stream().filter(next -> !mark[next]).mapToInt(next -> {
        mark[next] = true;
        int i = solution64Recursive(table, mark, next);
        mark[next] = false;
        return i;
    }).sum();
    return result;
};
```

### 미로탐색

#### 문제

7*7 격자판 미로를 탈출하는 경로의 가지수를 출력하는 프로그램을 작성하세요. 출발점은 격 자의 (1, 1) 좌표이고, 탈출 도착점은 (7, 7)좌표이다. 격자판의 1은 벽이고, 0은 통로이다.

#### 풀이

```java
public String solution65(int [][] table) {
    int answer = solution65Recursive(table, new Coordinate(0,0), new Coordinate(table.length-1, table.length-1));
    return String.valueOf(answer);
}

private int solution65Recursive(int [][] table, Coordinate current, Coordinate target) {
    if (current.x < 0 || current.x >= table.length || current.y < 0 || current.y >= table.length) {
        return 0;
    }
    int result = 0;
    if (current.equals(target)) {
        return 1;
    }
    result += Coordinate.offsets.stream().mapToInt(offset -> {
        return solution65Recursive(table, current.next(offset), target);
    }).sum();
    return result;
}

static class Coordinate {
    final int x;
    final int y;

    public static List<Coordinate> offsets = List.of(new Coordinate(1, 0), new Coordinate(-1, 0), new Coordinate(0,1), new Coordinate(0,-1));

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Coordinate next(Coordinate offset) {
        return new Coordinate(this.x + offset.x, this.y + offset.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
```

### 최소비용

#### 문제

가중치 방향그래프가 주어지면 1번 정점에서 N번 정점으로 가는 최소비용을 출력하는 프로그램을 작성하세요.

#### 풀이

```java
public String solution67(List<List<Integer>> table) {
    boolean [] mark = new boolean[table.size()];
    mark[0] = true;
    int answer = solution67_recursive(table, mark, 0, 0, mark.length - 1);
    return String.valueOf(answer);
}

private int solution67_recursive(List<List<Integer>> table, boolean [] mark, int current, int sum, int target) {
    if (current == target) {
        return sum;
    }
    return IntStream.range(0, table.get(current).size())
            .filter(i -> !table.get(current).get(i).equals(0) && !mark[i])
            .map(next -> {
                Integer weight = table.get(current).get(next);
                mark[next] = true;
                int result = solution67_recursive(table, mark, next, sum + weight, target);
                mark[next] = false;
                return result;
            }).min().orElse(Integer.MAX_VALUE);

}
```

### 이진트리 넓이우선탐색

#### 문제

아래 그림과 같은 이진트리를 넓이우선탐색해 보세요. 간선 정보 6개를 입력받아 처리해보세요.

#### 풀이

```java
public String solution69(List<Integer> [] edges, int n) {
    Queue<Integer> queue = new LinkedList<>();
    List<Integer> result = new ArrayList<>();
    queue.add(0);
    boolean [] mark = new boolean[7];
    mark[0] = true;
    result.add(0);
    while (!queue.isEmpty()) {
        Integer current = queue.poll();
        edges[current].stream()
                .filter(next -> !mark[next])
                .forEach(next -> {
                    mark[next] = true;
                    queue.add(next);
                    result.add(next);
                });
    }
    return result.stream().map(String::valueOf).collect(Collectors.joining(" "));
}
```

### 그래프 최단거리

#### 문제

1번 정점에서 각 정점으로 가는 최소 이동 간선수를 출력하세요.

#### 풀이

