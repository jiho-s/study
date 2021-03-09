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
86. [피자 배달 거리(DFS)](#피자-배달-거리)
87. [섬나라 아일랜드(BFS)](#섬나라 아일랜드)
88. [미로의 최단거리 통로(BFS)](#미로의-최단거리-통로)
89. [토마토(BFS)](#토마토)
90. [라이언 킹 심바(BFS)](#라이언-킹-심바)

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

### 피자 배달 거리

#### 문제

N×N 크기의 도시지도가 있습니다. 도시지도는 1×1크기의 격자칸으로 이루어져 있습니다. 각 격자칸에는 0은 빈칸, 1은 집, 2는 피자집으로 표현됩니다. 각 격자칸은 좌표(행번호, 열 번호) 로 표현됩니다. 행번호는 1번부터 N번까지이고, 열 번호도 1부터 N까지입니다.

 도시에는 각 집마다 “피자배달거리”가 았는데 각 집의 피자배달거리는 해당 집과 도시의 존재 하는 피자집들과의 거리 중 최소값을 해당 집의 “피자배달거리”라고 한다.

집과 피자집의 피자배달거리는 |x1-x2|+|y1-y2| 이다.

최근 도시가 불경기에 접어들어 우후죽순 생겼던 피자집들이 파산하고 있습니다. 도시 시장은 도시에 있는 피자집 중 M개만 살리고 나머지는 보조금을 주고 폐업시키려고 합니다.
 시장은 살리고자 하는 피자집 M개를 선택하는 기준으로 도시의 피자배달거리가 최소가 되는 M개의 피자집을 선택하려고 합니다.

도시의 피자 배달 거리는 각 집들의 피자 배달 거리를 합한 것을 말합니다.

#### 풀이

```java
public String solution86(int [][] table, int m) {
    List<Coordinate> homes = new ArrayList<>();
    List<Coordinate> pizzas = new ArrayList<>();
    int [] selects = new int[m];
    IntStream.range(0, table.length).forEach(i -> {
        IntStream.range(0, table.length).forEach(j -> {
            if (table[i][j] == 1) {
                homes.add(new Coordinate(i, j));
            } else if (table[i][j] == 2) {
                pizzas.add(new Coordinate(i,j));
            }
        });
    });
    int result = solution86Recursive(homes, pizzas, selects, 0, 0);
    return String.valueOf(result);
}

private int solution86Recursive(List<Coordinate> homes, List<Coordinate> pizzas, int [] selects, int current, int count) {
    if (current > pizzas.size()) {
        return Integer.MAX_VALUE;
    } else if (count == selects.length) {
        return homes.stream().mapToInt(home -> {
            return Arrays.stream(selects).map(select -> {
                return home.getDistance(pizzas.get(select));
            }).min().orElse(Integer.MAX_VALUE);
        }).sum();
    }
    selects[count] = current;
    int a = solution86Recursive(homes, pizzas, selects, current + 1, count + 1);
    int b = solution86Recursive(homes, pizzas, selects, current + 1, count);
    return min(a, b);
}
static class Coordinate {
    int x;
    int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getDistance(Coordinate coordinate) {
        return abs(this.x - coordinate.x) + abs(this.y - coordinate.y);
    }
}
```

### 섬나라 아일랜드

#### 문제

섬나라 아일랜드의 지도가 격자판의 정보로 주어집니다. 각 섬은 1로 표시되어 상하좌우와 대 각선으로 연결되어 있으며, 0은 바다입니다. 섬나라 아일랜드에 몇 개의 섬이 있는지 구하는 프로그램을 작성하세요.

#### 풀이

```java
public String solution87(int [][] table) {
    Queue<Coordinate> queue = new LinkedList<>();
    long sum = IntStream.range(0, table.length).mapToLong(i -> {
        return IntStream.range(0, table.length).filter(j -> {
            if (table[i][j] == 0) {
                return false;
            }
            queue.add(new Coordinate(i, j));
            table[i][j] = 0;
            while (!queue.isEmpty()) {
                Coordinate current = queue.poll();
                Coordinate.offsets.stream().map(offset -> offset.next(current)).forEach(next -> {
                    if (next.x >= table.length || next.x < 0 || next.y >= table.length || next.y < 0) {
                        return;
                    } else if (table[next.x][next.y] != 1) {
                        return;
                    }
                    table[next.x][next.y] = 0;
                    queue.add(next);
                });
            }
            return true;
        }).count();
    }).sum();
    return String.valueOf(sum);
}

static class Coordinate {
    static final List<Coordinate> offsets = List.of(new Coordinate(1,0),
            new Coordinate(-1, 0),
            new Coordinate(0 ,1),
            new Coordinate(0, -1),
            new Coordinate(1,1),
            new Coordinate(1, -1),
            new Coordinate(-1, 1),
            new Coordinate(-1, -1)
    );
    int x;
    int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate next(Coordinate coordinate) {
        return new Coordinate(this.x + coordinate.x, this.y + coordinate.y);
    }
}
```

### 미로의 최단거리 통로

#### 문제

7*7 격자판 미로를 탈출하는 최단경로의 경로수를 출력하는 프로그램을 작성하세요. 

 경로수는 출발점에서 도착점까지 가는데 이동한 횟수를 의미한다. 출발점은 격자의 (1, 1) 좌표이고, 탈 출 도착점은 (7, 7)좌표이다. 격자판의 1은 벽이고, 0은 도로이다.

 격자판의 움직임은 상하좌우로만 움직인다. 

#### 풀이

```java
public String solution88(int [][] table) {
    boolean marks [][] = new boolean[table.length][table.length];
    Coordinate target = new Coordinate(table.length - 1, table.length - 1);
    Queue<Coordinate> queue = new LinkedList<>();
    marks[0][0] = true;
    queue.add(new Coordinate(0,0));
    while (!queue.isEmpty()) {
        Coordinate current = queue.poll();
        if (current.equals(target)) {
            return String.valueOf(current.cost);
        }
        Coordinate.offsets.stream().map(offset -> {
            return offset.next(current);
        }).filter(next -> {
            return next.x >= 0 && next.x <= target.x && next.y >= 0 && next.y <= target.y && !marks[next.x][next.y];
        }).forEach(next -> {
            marks[next.x][next.y] = true;
            queue.add(next);
        });
    }
    return String.valueOf(-1);
}

static class Coordinate {
    int x;
    int y;
    int cost;

    static final List<Coordinate> offsets = List.of(new Coordinate(1,0, 1),
            new Coordinate(-1, 0, 1),
            new Coordinate(0 ,1, 1),
            new Coordinate(0 , -1, 1)
    );

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
        this.cost = 0;
    }

    public Coordinate(int x, int y, int cost) {
        this.x = x;
        this.y = y;
        this.cost = cost;
    }

    public Coordinate next(Coordinate coordinate) {
        return new Coordinate(this.x + coordinate.x, this.y + coordinate.y, this.cost + coordinate.cost);
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

### 토마토

#### 문제

현수의 토마토 농장에서는 토마토를 보관하는 큰 창고를 가지고 있다. 토마토는 아래의 그림과 같이 격자 모양 상자의 칸에 하나씩 넣어서 창고에 보관한다.

창고에 보관되는 토마토들 중에는 잘 익은 것도 있지만, 아직 익지 않은 토마토들도 있을 수 있다. 보관 후 하루가 지나면, 익은 토마토들의 인접한 곳에 있는 익지 않은 토마토들은 익은 토마토의 영향을 받아 익게 된다. 하나의 토마토의 인접한 곳은 왼쪽, 오른쪽, 앞, 뒤 네 방향 에 있는 토마토를 의미한다. 대각선 방향에 있는 토마토들에게는 영향을 주지 못하며, 토마토 가 혼자 저절로 익는 경우는 없다고 가정한다. 현수는 창고에 보관된 토마토들이 며칠이 지나 면 다 익게 되는지, 그 최소 일수를 알고 싶어 한다.

토마토를 창고에 보관하는 격자모양의 상자들의 크기와 익은 토마토들과 익지 않은 토마토들 의 정보가 주어졌을 때, 며칠이 지나면 토마토들이 모두 익는지, 그 최소 일수를 구하는 프로 그램을 작성하라. 단, 상자의 일부 칸에는 토마토가 들어있지 않을 수도 있다.

 첫 줄에는 상자의 크기를 나타내는 두 정수 M, N이 주어진다. M은 상자의 가로 칸의 수, N 은 상자의 세로 칸의 수를 나타낸다. 단, 2 ≤ M, N ≤ 1,000 이다.

 둘째 줄부터는 하나의 상자에 저장된 토마토들의 정보가 주어진다. 즉, 둘째 줄부터 N개의 줄 에는 상자에 담긴 토마토의 정보가 주어진다. 하나의 줄에는 상자 가로줄에 들어있는 토마토 의 상태가 M개의 정수로 주어진다. 정수 1은 익은 토마토, 정수 0은 익지 않은 토마토, 정수 -1은 토마토가 들어있지 않은 칸을 나타낸다.

#### 풀이

```java
public String solution89(int [][] table, int m, int n) {
    Queue<Coordinate> queue = new LinkedList<>();
    int [][] days = new int[n][m];
    IntStream.range(0, n).forEach(i -> {
        IntStream.range(0, m).forEach(j -> {
            if (table[i][j] == 1) {
                queue.add(new Coordinate(i, j));
                days[i][j] = 0;
            } else {
                days[i][j] = -1;
            }
        });
    });

    while (!queue.isEmpty()) {
        Coordinate current = queue.poll();
        Coordinate.offsets.stream()
                .map(offset -> offset.next(current))
                .forEach(next -> {
                    if (next.x >= n || next.x < 0 || next.y >= m || next.y < 0 ) {
                        return;
                    } else if (table[next.x][next.y] != 0) {
                        return;
                    }
                    days[next.x][next.y] = days[current.x][current.y] + 1;
                    queue.add(next);
                    table[next.x][next.y] = 1;
                });
    }
    boolean haveRaw = Arrays.stream(table).noneMatch(row -> {
        return Arrays.stream(row).anyMatch(num -> num == 0);
    });
    if (!haveRaw) {
        return String.valueOf(-1);
    }
    long max = Arrays.stream(days).mapToLong(row -> {
        return Arrays.stream(row).max().orElse(0);
    }).max().orElse(0);
    return String.valueOf(max);
}

static class Coordinate {
    int x;
    int y;
    static final List<Coordinate> offsets = List.of(new Coordinate(1,0),
            new Coordinate(-1, 0),
            new Coordinate(0 ,1),
            new Coordinate(0 , -1)
    );

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate next (Coordinate coordinate) {
        return new Coordinate(this.x + coordinate.x, this.y + coordinate.y);
    }
}
```

### 라이언 킹 심바

#### 문제

N×N 크기의 정글에 토끼 M마리와 어린 사자 심바가 있다. 정글은 1×1 크기의 격자로 이루 어져 있다. 각 격자칸에는 토끼 1한마리가 있거나 또는 없을 수 있다. 어린 사자 심바는 주어 진 정글에서 토끼를 잡아먹고 덩치를 키워 삼촌 스카에게 복수를 하러 갈 예정이다.
 어린 사자 심바와 토끼는 모두 몸 크기를 가지고 있고, 이 크기는 자연수이다. 가장 처음에 어 린 사자 심바의 크기는 2이고, 심바는 1초에 인접한 상하좌우 격자칸으로 이동할 수 있다. 어린 사자 심바는 자신보다 크기가 큰 토끼가 있는 칸은 지나갈 수 없고, 나머지 칸은 모두 지 나갈 수 있다. 심바는 자신보다 크기가 작은 토끼만 잡아먹을 수 있다. 크기가 같은 토끼는 먹 을 수는 없지만, 그 토끼가 있는 칸은 지날 수 있다.

어린 사자 심바가 토끼를 잡아먹기 위한 이동규칙은 다음과 같다.

1. 더 이상 먹을 수 있는 토끼가 정글에 없다면 이제 심바는 삼촌 스카에게 복수하러 갈 수 있다.
2. 먹을 수 있는 토끼가 딱 한마리라면, 그 토끼를 먹으러 간다.
3. 먹을 수 있는 토끼가 2마리 이상이면, 거리가 가장 가까운 토끼를 먹으러 간다.
   - 거리는 심바가 있는 칸에서 먹으려고 하는 토끼가 있는 칸으로 이동할 때, 지나야하는 칸 의 개수의 최소값이다.
   - 가장 가까운 토끼가 많으면, 그 중 가장 위쪽에 있는 토끼, 그러한 토끼가 여러 마리라 면, 가장 왼쪽에 있는 토끼를 잡아먹는다.

심바가 격자칸 하나를 이동하는데 1초 걸리고, 토끼를 먹는데 걸리는 시간은 없다고 가정한다. 심바가 해당 격자칸의 토끼를 먹으면, 그 칸은 빈 칸이 된다.

심바는 자신의 몸 크기와 같은 마리수 만큼 잡아먹으면 몸의 크기가 1증가한다.
 만약 심바의 몸크기가 5라면 자신보다 작은 토끼 5마리를 잡아먹으면 심바의 몸 크기는 6으로 변한다.

정글의 상태가 주어졌을 때, 심바가 몇 초 동안 토끼를 잡아먹고 삼촌 스카에게 복수를 하러 갈 수 있는지 구하는 프로그램을 작성하시오.

#### 풀이