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
70. [그래프 최단거리(BFS)](#그래프-최단거리)
71. [송아지 찾기(BFS)](#송아지-찾기)
72. [공주 구하기](#공주-구하기)
73. [최대 힙(우선순위 큐)](#최대-힙)
74. [최소 힙(우선순위 큐)](#최소-힙)
75. [최대 수입 스케줄(우선순위 큐)](#최대-수입-스케줄)

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

```java
public String solution70(List<Integer> [] table) {
    int [] distances = new int[table.length];
    Queue<Integer> queue = new LinkedList<>();
    queue.add(0);
    while (!queue.isEmpty()) {
        Integer current = queue.poll();
        table[current].stream().filter(next ->  next != 0 && distances[next] == 0).forEach(next -> {
            distances[next] = distances[current] + 1;
          	queue.add(next);
        });
    }
    return IntStream.range(1,table.length)
            .mapToObj(i -> String.valueOf(distances[i]))
            .collect(Collectors.joining(" "));
}
```

### 송아지 찾기

#### 문제

현수는 송아지를 잃어버렸다. 다행히 송아지에는 위치추적기가 달려 있다. 현수의 위치와 송아 지의 위치가 직선상의 좌표 점으로 주어지면 현수는 현재 위치에서 송아지의 위치까지 다음과 같은 방법으로 이동한다

 현수는 스카이 콩콩을 타고 가는데 한 번의 점프로 앞으로 1, 뒤로 1, 앞으로 5를 이동할 수 있다. 최소 몇 번의 점프로 현수가 송아지의 위치까지 갈 수 있는지 구하는 프로그램을 작성 하세요.

#### 풀이

```java
public String solution71(int start, int end) {
    List<Integer> offsets = List.of(1, -1, 5);
    int [] table = new int[end + 5];
    Queue<Integer> queue = new LinkedList<>();
    queue.add(start);
    while (!queue.isEmpty()) {
        Integer current = queue.poll();
        offsets.stream()
                .map(offset -> current + offset)
                .filter(next -> next != start && table[next] == 0)
                .filter(next -> next > 0 && next < end + 5)
                .forEach(next -> {
                    table[next] = table[current] + 1;
                    queue.add(next);
                });
    }
    return String.valueOf(table[end]);
}
```

### 공주 구하기

#### 문제

정보 왕국의 이웃 나라 외동딸 공주가 숲속의 괴물에게 잡혀갔습니다. 정보 왕국에는 왕자가 N명이 있는데 서로 공주를 구하러 가겠다고 합니다. 정보왕국의 왕은 다음과 같은 방법으로 공주를 구하러 갈 왕자를 결정하기로 했습니다.

 왕은 왕자들을 나이 순으로 1번부터 N번까지 차례로 번호를 매긴다. 그리고 1번 왕자부터 N 번 왕자까지 순서대로 시계 방향으로 돌아가며 동그랗게 앉게 한다. 그리고 1번 왕자부터 시 계방향으로 돌아가며 1부터 시작하여 번호를 외치게 한다. 한 왕자가 K(특정숫자)를 외치면 그 왕자는 공주를 구하러 가는데서 제외되고 원 밖으로 나오게 된다. 그리고 다음 왕자부터 다시 1부터 시작하여 번호를 외친다.

 이렇게 해서 마지막까지 남은 왕자가 공주를 구하러 갈 수 있다.

#### 풀이

```java
public String solution72(int n, int k) {
    LinkedList<Integer> list = new LinkedList<>();
    IntStream.rangeClosed(1, n).forEach(list::add);
    while (list.size() != 1) {
        IntStream.range(0, k).forEach(i -> list.add(list.removeFirst()));
        list.removeFirst();
    }
    return list.getFirst().toString();
}
```

### 최대 힙

#### 문제

최대힙 자료를 이용하여 다음과 같은 연산을 하는 프로그램을 작성하세요.

1. 자연수가 입력되면 최대힙에 입력한다.
2. 숫자 0 이 입력되면 최대힙에서 최댓값을 꺼내어 출력한다.(출력할 자료가 없으면 -1를 출력한다.)
3. -1이 입력되면 프로그램 종료한다.

#### 풀이

```java
public String solution73(List<Integer> commands) {
    PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(Comparator.reverseOrder());
    List<Integer> result = new ArrayList<>();
    for (Integer commend : commands) {
        if (commend.equals(-1)) {
            break;
        } else if (commend.equals(0)) {
            if (priorityQueue.isEmpty()) {
                result.add(-1);
            } else {
                result.add(priorityQueue.poll());
            }
        } else {
            priorityQueue.add(commend);
        }
    }
    return result.stream().map(String::valueOf).collect(Collectors.joining(" "));
}
```

### 최소 힙

#### 문제

최소힙 자료를 이용하여 다음과 같은 연산을 하는 프로그램을 작성하세요.

1. 자연수가 입력되면 최대힙에 입력한다.
2. 숫자 0 이 입력되면 최소힙에서 최소값을 꺼내어 출력한다.(출력할 자료가 없으면 -1를 출력한다.)
3. -1이 입력되면 프로그램 종료한다.

#### 풀이

```java
public String solution73(List<Integer> commands) {
    PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
    List<Integer> result = new ArrayList<>();
    for (Integer commend : commands) {
        if (commend.equals(-1)) {
            break;
        } else if (commend.equals(0)) {
            if (priorityQueue.isEmpty()) {
                result.add(-1);
            } else {
                result.add(priorityQueue.poll());
            }
        } else {
            priorityQueue.add(commend);
        }
    }
    return result.stream().map(String::valueOf).collect(Collectors.joining(" "));
}
```

### 최대 수입 스케줄

#### 문제

현수는 유명한 강연자이다. N개이 기업에서 강연 요청을 해왔다. 각 기업은 D일 안에 와서 강 연을 해 주면 M만큼의 강연료를 주기로 했다.

 각 기업이 요청한 D와 M를 바탕으로 가장 많을 돈을 벌 수 있도록 강연 스케쥴을 짜야 한다. 단 강연의 특성상 현수는 하루에 하나의 기업에서만 강연을 할 수 있다.

#### 풀이

```java
public String solution75(List<List<Integer>> list) {
    var ref = new Object() {
        int max = 0;
    };
    LinkedList<Pair> collect = list.stream().map(i -> {
        if (ref.max < i.get(1)) {
            ref.max = i.get(1);
        }
        return new Pair(i);
    }).sorted(Comparator.comparing(Pair::getMoney)
            .thenComparing(Pair::getTime)
            .reversed()).collect(Collectors.toCollection(LinkedList::new));
    int sum = IntStream.iterate(ref.max, i -> i > 0, i -> --i).map(i -> {
        Pair pair = collect.removeFirst();
        if (pair.time < i) {
            return 0;
        }
        return pair.getMoney();
    }).sum();

    return String.valueOf(sum);
}

static class Pair {
    int money;
    int time;

    public Pair(List<Integer> list) {
        this.money = list.get(0);
        this.time = list.get(1);
    }

    public int getMoney() {
        return money;
    }

    public int getTime() {
        return time;
    }
}
```