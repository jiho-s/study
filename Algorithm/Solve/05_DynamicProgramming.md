## 동적 계획법

### 목차

1. [네트워크 선 자르기(Bottom-Up)](#네트워크-선-자르기-bottom-up)
2. [네트워크 선 자르기 (Top-Down)](#네트워크-선-자르기-top-down)
3. [돌다리 건너기](#돌다리-건너기)
4. [최대 부분 증가수열](#최대-부분-증가수열)
5. [최대 선 연결하기](#최대-선-연결하기)
6. [가장 높은 탑 쌓기](#가장-높은-탑-쌓기)
7. [알리바바와 40인의 도둑(Bottom-Up)](#알리바바와-40인의-도둑-bottom-up)
8. [알리바바와 40인의 도둑(Top-Down)](#알리바바와-40인의-도둑-top-down)
9. [가방문제(냅색 알고리즘)](#가방문제-냅색-알고리즘)
10. [동전교환](#동전교환)

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

### 가장 높은 탑 쌓기

#### 문제

밑면이 정사각형인 직육면체 벽돌들을 사용하여 탑을 쌓고자 한다. 탑은 벽돌을 한 개씩 아래 에서 위로 쌓으면서 만들어 간다. 아래의 조건을 만족하면서 가장 높은 탑을 쌓을 수 있는 프 로그램을 작성하시오.

1. 벽돌은 회전시킬 수 없다. 즉, 옆면을 밑면으로 사용할 수 없다.
2. 밑면의 넓이가 같은 벽돌은 없으며, 또한 무게가 같은 벽돌도 없다. 
3. 벽돌들의 높이는 같을 수도 있다.
4. 탑을 쌓을 때 밑면이 좁은 벽돌 위에 밑면이 넓은 벽돌은 놓을 수 없다.
5. 무게가 무거운 벽돌을 무게가 가벼운 벽돌 위에 놓을 수 없다.

#### 풀이

```java
public String solution6(int [][] input) {
    List<Brick> list = Arrays.stream(input).map(Brick::new)
            .sorted(Comparator.comparing(Brick::getBottom))
            .collect(Collectors.toList());
    List<Integer> result = new ArrayList<>();
    result.add(list.get(0).height);
    int answer = IntStream.range(1, list.size()).map(i -> {
        int max = IntStream.range(0, result.size())
                .filter(j -> list.get(j).weight < list.get(i).weight)
                .map(result::get).max().orElse(0);
        max = max + list.get(i).height;
        result.add(max);
        return max;
    }).max().orElse(0);
    return String.valueOf(answer);
}
static class Brick {
    int bottom;
    int height;
    int weight;

    public Brick(int [] l) {
        this.bottom = l[0];
        this.height = l[1];
        this.weight = l[2];
    }

    public int getBottom() {
        return bottom;
    }
}
```

### 알리바바와 40인의 도둑 Bottom-Up

#### 문제

알리바바는 40인의 도둑으로부터 금화를 훔쳐 도망치고 있습니다.

알리바바는 도망치는 길에 평소에 잘 가지 않던 계곡의 돌다리로 도망가고자 한다. 계곡의 돌다리는 N×N개의 돌들로 구성되어 있다. 각 돌다리들은 높이가 서로 다릅니다.

해당 돌다리를 건널때 돌의 높이 만큼 에너지가 소비됩니다. 이동은 최단거리 이동을 합니다. 즉 현재 지점에서 오른쪽 또는 아래쪽으로만 이동해야 합니다.
 N*N의 계곡의 돌다리 격자정보가 주어지면 (1, 1)격자에서 (N, N)까지 가는데 드는 에너지의 최소량을 구하는 프로그램을 작성하세요.

#### 풀이

```java
public String soltuion7(int [][] table) {
    for (int i = 1; i < table.length; i++) {
        table[0][i] += table[0][i-1];
        table[i][0] += table[i-1][0];
    }
    for (int i = 1; i < table.length; i++) {
        for (int j = 1; j < table.length; j++) {
            table[i][j] += min(table[i-1][j], table[i][j-1]);
        }
    }
    return String.valueOf(table[table.length-1][table.length-1]);
}
```

### 알리바바와 40인의 도둑 Top-Down

#### 문제

알리바바는 40인의 도둑으로부터 금화를 훔쳐 도망치고 있습니다.

알리바바는 도망치는 길에 평소에 잘 가지 않던 계곡의 돌다리로 도망가고자 한다. 계곡의 돌다리는 N×N개의 돌들로 구성되어 있다. 각 돌다리들은 높이가 서로 다릅니다.

해당 돌다리를 건널때 돌의 높이 만큼 에너지가 소비됩니다. 이동은 최단거리 이동을 합니다. 즉 현재 지점에서 오른쪽 또는 아래쪽으로만 이동해야 합니다.
 N*N의 계곡의 돌다리 격자정보가 주어지면 (1, 1)격자에서 (N, N)까지 가는데 드는 에너지의 최소량을 구하는 프로그램을 작성하세요.

#### 풀이

```java
public String solution8(int [][] table) {
    int [][] result = new int[table.length][table.length];
    int answer = solution8DFS(result, table, table.length - 1, table.length - 1);
    return String.valueOf(answer);
}

private int solution8DFS(int [][] result, int [][] table, int x, int y) {
    if (result[x][y] != 0) {
        return result[x][y];
    } else if (x == 0 && y == 0) {
        return table[0][0];
    }
    if (x == 0) {
        result[x][y] = solution8DFS(result, table, x, y -1) + table[x][y];
    } else if (y == 0) {
        result[x][y] = solution8DFS(result, table, x -1, y) + table[x][y];
    } else {
        result[x][y] = min(solution8DFS(result, table, x -1, y), solution8DFS(result, table, x, y-1)) + table[x][y];
    }
    return result[x][y];
}
```

### 가방문제 냅색 알고리즘

#### 문제

최고 17kg의 무게를 저장할 수 있는 가방이 있다. 그리고 각각 3kg, 4kg, 7kg, 8kg, 9kg의 무게를 가진 5종류의 보석이 있다. 이 보석들의 가치는 각각 4, 5, 10, 11, 13이다.

 이 보석을 가방에 담는데 17kg를 넘지 않으면서 최대의 가치가 되도록 하려면 어떻게 담아야 할까요? 각 종류별 보석의 개수는 무한이 많다. 한 종류의 보석을 여러 번 가방에 담을 수 있 다는 뜻입니다.

첫 번째 줄은 보석 종류의 개수와 가방에 담을 수 있는 무게의 한계값이 주어진다. 두 번째 줄부터 각 보석의 무게와 가치가 주어진다.
 가방의 저장무게는 1000kg을 넘지 않는다. 보석의 개수는 30개 이내이다.

#### 풀이

```java
public String solution9(int maxWeight, int [][] gems) {
    int [] table = new int[maxWeight+1];
    Arrays.stream(gems).forEach(gem -> {
        int weight = gem[0];
        int value = gem[1];
        IntStream.rangeClosed(weight, maxWeight).forEach(i -> {
            table[i] = max(table[i-weight] + value, table[i]);
        });
    });
    return String.valueOf(table[maxWeight]);
}
```

### 동전교환

#### 문제

다음과 같이 여러 단위의 동전들이 주어져 있을때 거스름돈을 가장 적은 수의 동전으로 교환 해주려면 어떻게 주면 되는가? 각 단위의 동전은 무한정 쓸 수 있다.

첫 번째 줄에는 동전의 종류개수 N(1<=N<=12)이 주어진다. 두 번째 줄에는 N개의 동전의 종 류가 주어지고, 그 다음줄에 거슬러 줄 금액 M(1<=M<=500)이 주어진다.
 각 동전의 종류는 100원을 넘지 않는다.

#### 풀이

```java
public String solution10(int remain, int [] coins) {
    List<Integer> table = IntStream.rangeClosed(0, remain).mapToObj(i -> Integer.MAX_VALUE).collect(Collectors.toList());
    table.set(0, 0);
    Arrays.stream(coins).forEach(coin -> {
        IntStream.rangeClosed(coin, remain).forEach(i -> {
            table.set(i, min(table.get(i - coin) + 1, table.get(i)));
        });
    });
    return String.valueOf(table.get(remain));
}
```