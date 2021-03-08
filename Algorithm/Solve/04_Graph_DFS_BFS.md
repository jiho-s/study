## 그래프, DFS, BFS 관련 보충문제

### 목차

76. [이항계수(메모이제이션)](#이항계수)
77. [친구인가?(Union&Find)](#친구인가?)
78. [원더랜드(Kruskal Union&Find 활용)](#원더랜드-kruskal)
79. [원더랜드(Prim priority_queue활용)](#원더랜드-prim)
80. [다익스트라 알고리즘](#다익스트라-알고리즘)
81. [벨만 포드 알고리즘](#벨만-포드-알고리즘)
82. [순열구하기(DFS)](#순열구하기)
83. [복면산](#복면산)
84. [휴가](#휴가)
85. [수식만들기(DFS)](#수식만들기)

### 이항계수

#### 문제

이항계수는 N개의 원소를 가지는 집합에서 R개의 원소를 뽑아 부분집합을 만드는 경우의 수 를 의미한다. 공식은 *<sub>n</sub>C<sub>r</sub>* 로 표현된다.

 N과 R이 주어지면 이항계수를 구하는 프로그램을 작성하세요.

#### 풀이

```java
public String solution76(int n, int r) {
    int [][] table = new int[n+1][r+1];
    int result = solution76_recursive(table, n, r);
    return String.valueOf(result);
}

public int solution76_recursive(int [][] table, int n, int r) {
    if (table[n][r] != 0) {
        return table[n][r];
    } else if (n == r) {
        return 1;
    } else if (r == 0) {
        return 1;
    }
    return table[n][r] = solution76_recursive(table, n - 1, r) + solution76_recursive(table, n-1, r-1);
}
```

### 친구인가?

#### 문제

오늘은 새 학기 새로운 반에서 처음 시작하는 날이다. 현수네 반 학생은 N명이다. 현수는 각 학생들의 친구관계를 알고 싶다.

 모든학생은1부터N까지번호가부여되어있고, 현수에게는각각두명의학생은친구관계 가 번호로 표현된 숫자쌍이 주어진다. 만약 (1, 2), (2, 3), (3, 4)의 숫자쌍이 주어지면 1번 학 생과 2번 학생이 친구이고, 2번 학생과 3번 학생이 친구, 3번 학생과 4번 학생이 친구이다. 그리고 1번 학생과 4번 학생은 2번과 3번을 통해서 친구관계가 된다.

학생의 친구관계를 나타내는 숫자쌍이 주어지면 특정 두 명이 친구인지를 판별하는 프로그램 을 작성하세요. 두 학생이 친구이면 “YES"이고, 아니면 ”NO"를 출력한다.

#### 풀이

```java
public String solution77(int [][] friends, int a, int b, int n) {
    int [] table = new int[n+1];
    Arrays.stream(friends).forEach(friend -> {
        int rootA = findRoot(table, friend[0]);
        int rootB = findRoot(table, friend[1]);
        if (rootA == rootB) {
            return;
        }
        table[friend[0]] = rootB;
    });
    return findRoot(table, a) == findRoot(table, b) ? "Yes" : "No";
}

public int findRoot(int [] table, int a) {
    if (table[a] != 0) {
        return table[a];
    }
    return findRoot(table, table[a]);
}
```

### 원더랜드 Kruskal

#### 문제

원더랜드에 문제가 생겼다. 원더랜드의 각 도로를 유지보수하는 재정이 바닥난 것이다. 원더랜드는 모든 도시를 서로 연결하면서 최소의 유지비용이 들도록 도로를 선택하고 나머지 도로는 폐쇄하려고 한다. 어떤 도로는 도로를 유지보수하면 재정에 도움이 되는 도로도 존재 한다. 재정에 도움이 되는 도로는 비용을 음수로 표현했다.

첫째 줄에 도시의 개수 V(1≤V≤100)와 도로의 개수 E(1≤E≤1,000)가 주어진다. 다음 E개의 줄에는 각 도로에 대한 정보를 나타내는 세 정수 A, B, C가 주어진다. 이는 A번 도시와 B번 도시가 유지비용이 C인 도로로 연결되어 있다는 의미이다. C는 음수일 수도 있으며, 절댓값이 1,000,000을 넘지 않는다.

#### 풀이

```java
public String solution72(int [][] costs, int n) {
    int [] table = new int[n+1];
    LinkedList<Edge> collect = Arrays.stream(costs).map(Edge::new)
            .sorted(Comparator.comparing(Edge::getCost))
            .collect(Collectors.toCollection(LinkedList::new));
    int answer = IntStream.range(1, n).map(i -> {
        while (!collect.isEmpty()) {
            Edge edge = collect.removeFirst();
            int rootA = findRoot(table, edge.a);
            int rootB = findRoot(table, edge.b);
            if (rootA != rootB) {
                table[edge.a] = rootB;
                return edge.cost;
            }
        }
        return Integer.MAX_VALUE;
    }).sum();
    return String.valueOf(answer);
}
static class Edge {
    int a;
    int b;
    int cost;


    public Edge(int [] cos) {
        this.a = cos[0];
        this.b = cos[1];
        this.cost = cos[2];
    }

    public int getCost() {
        return cost;
    }
}
public int findRoot(int [] table, int a) {
    if (table[a] != 0) {
        return table[a];
    }
    return findRoot(table, table[a]);
}
```

### 원더랜드 Prim

#### 문제

원더랜드에 문제가 생겼다. 원더랜드의 각 도로를 유지보수하는 재정이 바닥난 것이다. 원더랜드는 모든 도시를 서로 연결하면서 최소의 유지비용이 들도록 도로를 선택하고 나머지 도로는 폐쇄하려고 한다. 어떤 도로는 도로를 유지보수하면 재정에 도움이 되는 도로도 존재 한다. 재정에 도움이 되는 도로는 비용을 음수로 표현했다.

첫째 줄에 도시의 개수 V(1≤V≤100)와 도로의 개수 E(1≤E≤1,000)가 주어진다. 다음 E개의 줄에는 각 도로에 대한 정보를 나타내는 세 정수 A, B, C가 주어진다. 이는 A번 도시와 B번 도시가 유지비용이 C인 도로로 연결되어 있다는 의미이다. C는 음수일 수도 있으며, 절댓값이 1,000,000을 넘지 않는다.

#### 풀이

```java
public String solution79(int [][] costs, int n) {
    List<List<Edge>> table = new ArrayList<>();
    int answer = 0;
    boolean [] mark = new boolean[n+1];
    IntStream.range(0, n+1).forEach(i -> table.add(new ArrayList<>()));
    PriorityQueue<Edge> priorityQueue = new PriorityQueue<>(Comparator.comparing(Edge::getCost));
    Arrays.stream(costs).forEach(cost -> {
        table.get(cost[0]).add(new Edge(cost[1], cost[2]));
        table.get(cost[1]).add(new Edge(cost[0], cost[2]));
    });
    priorityQueue.add(new Edge(1, 0));
    mark[1] = true;
    
    while (!priorityQueue.isEmpty()) {
        Edge currentEdge = priorityQueue.poll();
        int currentNode = currentEdge.node;
        if (!mark[currentNode]) {
            mark[currentNode] = true;
            answer += currentEdge.cost;
            table.get(currentNode).stream().filter(nextEdge -> {
                return !mark[nextEdge.node];
            }).forEach(priorityQueue::add);
        }
    }
    return String.valueOf(answer);
}

static class Edge {
    final int node;
    final int cost;

    public Edge(int node, int cost) {
        this.node = node;
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }
}
```

### 다익스트라 알고리즘

#### 문제

아래의 가중치 방향그래프에서 1번 정점에서 모든 정점으로의 최소 거리비용을 출력하는 프로 그램을 작성하세요. (경로가 없으면 Impossible를 출력한다)

#### 풀이

```java
public String solution80(List<List<Integer>> table, int n) {
    boolean [] mark = new boolean[n+1];
    List<Integer> distances = new ArrayList<>();
    IntStream.rangeClosed(0, n).forEach(i -> {
        distances.add(Integer.MAX_VALUE);
    });
    distances.set(1, 0);

    IntStream.rangeClosed(1, n).forEach(i -> {
        int min = 0;
        for (int j = 0; j < distances.size(); j++) {
            if (!mark[j] && distances.get(min) < distances.get(j)) {
                min = j;
            }
        }
        mark[min] = true;
        int finalMin = min;
        IntStream.rangeClosed(0, n).forEach(j -> {
            Integer integer = table.get(finalMin).get(j);
            if (distances.get(j) > distances.get(finalMin) + integer) {
                distances.set(j, distances.get(finalMin) + integer);
            }
        });

    });
    return IntStream.rangeClosed(2, n).mapToObj(i -> {
        Integer distance = distances.get(i);
        return distance == Integer.MAX_VALUE ? "impossible" : i " : "+ String.valueOf(distance));
    }).collect(Collectors.joining("\n"));
}
```

### 벨만 포드 알고리즘

#### 문제

N개의 도시가 주어지고, 각 도시들을 연결하는 도로와 해당 도로를 통행하는 비용이 주어질 때 한 도시에서 다른 도시로 이동하는데 쓰이는 비용의 최소값을 구하는 프로그램을 작성하세요.

#### 풀이

```java
public String solution81(int [][] list, int n, int target) {
    List<Edge> collect = Arrays.stream(list).map(Edge::new).collect(Collectors.toList());
    List<Integer> table = new ArrayList<>();
    IntStream.rangeClosed(0, n).forEach(i -> table.add(Integer.MAX_VALUE));
    table.set(1, 0);
    IntStream.range(1, n).forEach(i -> {
        collect.forEach(edge -> {
            if (table.get(edge.end) != Integer.MAX_VALUE && table.get(edge.end) > table.get(edge.start) + edge.cost) {
                table.set(edge.end, table.get(edge.start) + edge.cost);
            }
        });
    });
    boolean noneMatch = collect.stream()
            .noneMatch(edge -> (table.get(edge.end) != Integer.MAX_VALUE && table.get(edge.end) > table.get(edge.start) + edge.cost));
    if (noneMatch) {
        return String.valueOf(-1);
    }
    return String.valueOf(table.get(target));
}

static class Edge {
    int start;
    int end;
    int cost;

    public Edge(int [] list) {
        this.start = list[0];
        this.end = list[1];
        this.cost = list[2];
    }


}
```

### 순열구하기

#### 문제

자연수 N과 R이 주어지면 서로 다른 N개의 자연수 중 R개를 뽑아 일렬로 나열하는 프로그램을 작성하세요.

#### 풀이

```java
public String solution82(int n, int r, List<Integer> numbers) {
    boolean [] mark = new boolean[n];
    StringBuilder stringBuilder = new StringBuilder();
    int answer = solution82Recursive(mark, numbers, r, new LinkedList<>(), stringBuilder);
    stringBuilder.append(answer);
    return stringBuilder.toString();

}

private int solution82Recursive(boolean[] mark, List<Integer> numbers, int r, LinkedList<Integer> list, StringBuilder stringBuilder) {
    if (r == list.size()) {
        String collect = list.stream().map(String::valueOf).collect(Collectors.joining(" "));
        stringBuilder.append(collect).append("\n");
        return 1;
    }
    return IntStream.range(0, numbers.size()).filter(i -> !mark[i]).map(i -> {
        mark[i] = true;
        list.add(numbers.get(i));
        int result = solution82Recursive(mark, numbers, r, list, stringBuilder);
        list.removeLast();
        mark[i] = false;
        return result;
    }).sum();
}
```

### 복면산

#### 문제

SEND+MORE=MONEY 라는 유명한 복면산이 있습니다. 이 복면산을 구하는 프로그램을 작성하세요.

#### 풀이

```java
public String solution83() {
    boolean [] marks = new boolean[10];
    MyList myList = new MyList();
    StringBuilder stringBuilder = new StringBuilder();
    solution83Recursive(marks, myList, stringBuilder);
    return stringBuilder.toString();
}

private void solution83Recursive(boolean [] marks, MyList myList, StringBuilder stringBuilder) {
    if (myList.a.size() == 8) {
        if (!(myList.a.get(2) == 0 || myList.a.get(6) == 0)) {
            if (myList.isTrue()) {
                stringBuilder.append(myList.send()).append("+").append(myList.more()).append("=").append(myList.money()).append("\n");
            }
        }
        return;
    }
    IntStream.range(0, marks.length).filter(i -> !marks[i]).forEach(i -> {
        marks[i] = true;
        myList.a.add(i);
        solution83Recursive(marks, myList, stringBuilder);
        marks[i] = false;
        myList.a.removeLast();

    });
}

static class MyList {
    LinkedList<Integer> a = new LinkedList<Integer>();

    boolean isTrue() {
        return send() + more() == money();
    }
    int send() {
        return a.get(6) *1000+ a.get(1) *100+ a.get(3) *10+ a.get(0);
    }
    int more() {
        return a.get(2) *1000+ a.get(4) *100+ a.get(5) *10+ a.get(1);
    }
    int money() {
        return a.get(2) *10000+ a.get(4) *1000+ a.get(3) *100+ a.get(1) *10+ a.get(7);
    }
}
```

### 휴가

#### 문제

카운셀러로 일하고 있는 현수는 오늘부터 N+1일째 되는 날 휴가를 가기 위해서, 남은 N일 동 안 최대한 많은 상담을 해서 휴가비를 넉넉히 만들어 휴가를 떠나려 한다.

 현수가 다니는 회사에 하루에 하나씩 서로 다른 사람의 상담이 예약되어 있다.

각각의 상담은 상담을 완료하는데 걸리는 날수 T와 상담을 했을 때 받을 수 있는 금액 P로 이 루어져 있다.

현수가 휴가를 가기 위해 얻을 수 있는 최대 수익을 구하는 프로그램을 작성하시오.

#### 풀이

```java
public String solution84(List<List<Integer>> list) {
    int answer = solution84Recursive(list, 0, 0, 0);
    return String.valueOf(answer);
}

public int solution84Recursive(List<List<Integer>> list, int remain, int current, int sum) {
    if (current == list.size()) {
        return sum;
    }
    int result = 0;
    if (remain == 0) {
        int a = solution84Recursive(list, list.get(current).get(0) - 1, current + 1, sum + list.get(current).get(1));
        int b = solution84Recursive(list, 0, current + 1, sum);
        result = max(a,b);
    }else {
        result = solution84Recursive(list, remain - 1, current + 1, sum);
    }
    return result;
}
```

### 수식만들기

#### 문제

길이가 N인 자연수로 이루어진 수열이 주어집니다. 수열의 각 항 사이에 끼워넣을 N-1개의 연산자가 주어집니다. 연산자는 덧셈(+), 뺄셈(-), 곱셈(×), 나눗셈(÷)으로만 이루어져 있습니 다.
 수열의 순서는 그대로 유지한 채 각 항사이에 N-1개의 연산자를 적절히 배치하면 다양한 수 식이 나옵니다.

 예를 들면수열이 1 2 3 4 5이고 덧셈(+) 1개, 뺄셈(-) 1개, 곱셈(×) 1개, 나눗셈(÷) 1개인 일 때
 만들 수 있는 수식은 많은 경우가 존재한다.
 그 중 1+2*3-4/5와 같이 수식을 만들었다면 수식을 계산하는 결과는 연산자 우선순위를 따지 지 않고 맨 앞쪽 연산자부터 차례로 계산한다. 수식을 계산한 결과는 1이다.
 N길이의 수열과 N-1개의 연산자가 주어지면 만들 수 있는 수식들 중에서 연산한 결과가 최대 인것과 최소인것을 출력하는 프로그램을 작성하세요.

#### 풀이

```java
int max = Integer.MIN_VALUE;
int min = Integer.MAX_VALUE;
public String solution85(List<Integer> list, List<Integer> operators) {
    solution85Recursive(list, operators, 1, list.get(0));
    return max + "\n" + min;
}

public void solution85Recursive(List<Integer> list, List<Integer> operators, int current, int sum) {
    if (current == list.size()) {
       if (max < sum) {
           max = sum;
       } else if (min > sum) {
           min = sum;
       }
       return;
    }
    if (operators.get(0) > 0) {
        operators.set(0, operators.get(0) -1);
        solution85Recursive(list, operators, current + 1, sum + list.get(current));
        operators.set(0, operators.get(0) + 1);
    }
    if (operators.get(1) > 0) {
        operators.set(1, operators.get(1) -1);
        solution85Recursive(list, operators, current + 1, sum - list.get(current));
        operators.set(1, operators.get(1) + 1);
    }
    if (operators.get(2) > 0) {
        operators.set(2, operators.get(2) -1);
        solution85Recursive(list, operators, current + 1, sum * list.get(current));
        operators.set(2, operators.get(2) + 1);
    }
    if (operators.get(3) > 0) {
        operators.set(3, operators.get(3) -1);
        solution85Recursive(list, operators, current + 1, sum / list.get(current));
        operators.set(3, operators.get(3) + 1);
    }
}
```