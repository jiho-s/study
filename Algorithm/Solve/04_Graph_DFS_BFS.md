## 그래프, DFS, BFS 관련 보충문제

### 목차

76. [이항계수(메모이제이션)](#이항계수)
77. [친구인가?(Union&Find)](#친구인가?)
78. [원더랜드(Kruskal Union&Find 활용)](#원더랜드-kruskal)
79. [원더랜드(Prim priority_queue활용)](#원더랜드-prim)
80. [다익스트라 알고르즘](#다익스트라-알고리즘)

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