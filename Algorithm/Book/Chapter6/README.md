## 무식하게 풀기

### 목차

1. [도입](#도입)
2. [재귀 호출과 완전탐색](#재귀-호출과-완전탐색)
3. [소풍](#소풍)
4. [게임판 덮기](#게임판-덮기)

### 도입

가능한 방법을 전부 만들어 보는 알고리즘을 가리켜 흔히 완전탐색이라고 부른다.

### 재귀 호출과 완전탐색

#### 재귀호출

재귀함수란 자신이 수행할 작업을 유사한 형태의 여러 조각으로 쪼갠 뒤 그 중 한 조각을 수행하고, 나머지를 자기자신이 호출해 실행하는 함수

```java
    int sum(int n) {
        int result = 0;
        for (int i = 1; i <= n; i++) {
            result += i;
        }
        return result;
    }
    
    int recursiveSum(int n) {
        if (n == 1) {
            return 1;
        }
        return recursiveSum(n-1) + n;
    }
```

문제의 특성에 따라 재귀 호출은 코딩을 훨씬 간편하게 해 줄 수 있는 강력한 무기가된다.

#### 예제: 중첩 반복문 대체하기

0번부터 차례대로 번호 매겨진 *n*개의 원소 중 4개를 고르는 모든 경우를 출력하는 경우를 작성해 보자

```java
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                for(int k = j+1; k < n; k++) {
                    for (int l = k+1; l < n; l++) {
                        System.out.println(i + " " + j + " " + k + " " + l);
                    }
                }
            }
        }
```

위와 같은 중첩 for문은 골라야 할 원소의 수가 늘어날수록 코드가 길고 복잡해지는데다, 골라야 할 원소의 수가 입력에 따라 달라질 수 있는 경우에는 사용할 수 없다는 문제가 있다. 재귀호출은 이런 경우에 단순한 반복문보다 간결하고 유연한 코드를 작성할 수 있도록해준다.

```java
    void pick(int n, List<Integer> picked, int toPick) {
        if (toPick == 0) {
            System.out.println(picked);
            return;
        }
        int min = picked.isEmpty() ? 0 : picked.get(picked.size() - 1);
        for (int i = min; i < n; i++) {
            picked.add(i);
            pick(n, picked, toPick -1);
            picked.remove(picked.size() - 1);
        }
    }
```

#### 예제: 보글 게임

5*5 크기의 알파벳 격자에서 상하좌우 대각선으로 인접한 칸들의 글자들을 이어서 단어를 찾아내는 것

##### 문제의 분할

각 글자를 하나의 조각으로 만드는 것

##### 기저 사례의 선택

1. 위치 (x,y)에 있는 글자가 원하는 단어의 첫글자가 아닌경우 항상 실패
2. 원하는 단어가 한글자인 경우 항상 성공

간결한 코드를 작성하는데, 입력이 잘못되거나 범위에서 벗어난 경우도 기저 사례로 택하여 맨 처음에 처리한다. 그러면 함수를 호출하는 시점에 이런 오류를 검사할 필요가 없다.

##### 구현

다음 칸의 상대 좌표 목록을 함수 내에 직접 코딩해 넣은 것이 아니라 별도의 변수로 분리해 낸 점도 참고

```java
    private final int[] dx = {-1,-1,-1,1,1,1,0,0};
    private final int[] dy = {-1,0,1,-1,0,1,-1,1};

    boolean hasWord(int x, int y, String word) {
        if (!inRange(x,y))
            return false;
        if (word.indexOf(0) == board[x][y])
            return false;
        if (word.length() == 1)
            return true;

        int len = dx.length;
        for (int i = 0; i < len; i++) {
            if (hasWord(x, y, word.substring(1)))
                return true;
        }
        return false;
    }
```

##### 시간 복잡도 분석

마지막 글자에 도달하기 전에는 주변의 모든 칸에 대해 재귀 호출을 하게 된다. 각 칸에는 최대 8개의 이웃이 있고 탐색은 단어의 길이 N에 대하여 N-1단계 진행된다. 따라서 최대 8<sup>N-1</sup>=O( 8<sup>N</sup>)이 된다.

##### 완전 탐색 레시피

1. 완전 탐색은 존재하는 모든 답을 하나씩 검사하므로, 걸리는 시간은 가능한 답의 수에 적확히 비례한다. 만약 시간안에 계산할 수 없다면 3부 다른 장에서 소개하는 설계 패러다임을 적용해야 한다.
2. 가능한 모든 답의 후보를 만드는 과정을 여러 개의 선택으로 나눈다. 각 선택은 답의 후보를 만드는 과정의 한 조각이 된다.
3. 그중 하나의 조각을 선택해 답의 일부를 만들고 나머지 답을 재귀 호출을 통해 완성한다.
4. 조각이 하나밖에 남지 않은 경우, 혹은 하나도 남지 않은 경우에는 답을 생성했으므로, 이것을 기저 사례로 선택해 처리한다.

##### 이론적 배경: 재귀 호출과 부분 문제

보글 게임에서 문제는 '게임판에서 현재 위치 (x,y) 그리고 단어 `word`가 있을 때 해당 단어를 이칸에서 부터 시작해서 찾을수 있는가?'로 정의 된다. 해당단어를 알기 위해 9가지 정보가 필요하다.

1. 핸재 위치(x,y)에 단어의 첫글자가 있는가
2. 윗칸(y-1,x)에서 시작하여 단어의 나머지 글자들을 찾을 수 있는가
3. 완쪽 윗칸(y-1,x-1)에서 시작하여 단어의 나머지 글자들을 찾을 수 있는가
4. ...
5. ...

이 중 2번 이후의 항목은 원래문제에서 한 조각을 떼어냈을 뿐 형식이 같은 또 다른 문제를 푼 결과이다. 이 문제들은 원래 문제의 일부라고 말 할 수 있고 이런 문제들을 원래 문제의 부분 문제라고 한다.

### 소풍

#### 문제

소풍 때 학생들을 두 명씩 짝을 지어 행동하게 하려고 한다. 그런데 서로 친구인 학생들끼리만 짝을 지어야한다. 각 학생들의 쌍에 대해 이들이 서로 친구인지 아닌지 여부가 주어질때, 학생들을 짝 지을 수 있는 방법의 수를 계산하는 프로그램을 작성하세요 짝이 되는 학생들이 일부만 다르더라도 다른 방법이라고 본다. 예를 들어 다음 두 가지 방법은 서로 다른 방법이다.

- (일, 이)(삼, 사)(오, 육)
- (일, 이)(삼, 육)(오, 사)

##### 시간 및 메모리 제한

프로그램은 1초 내에 실행 64MB 이하의 메모리만을 사용

##### 입력

입력의 첫 줄에는 테스트 케이스의 수 C가 주어 진다. 각 테스트 케이스의 첫 줄에는 학생의 수 n(2<=n<=10)과 친구 쌍의 수 m 이 주어 진다. 그 다음 줄에 m개의 정수 쌍으로 서로 친구인 두 학생의 번호가 주어진다. 번호는 0부터 n-1 사이의 정수이고 같은 쌍은 입력에 두번 주어지지 않는다. 학생의 수는 짝수 이다.

##### 출력

각 테스트케이스마다 한 줄에 모든 학생을 친구끼리만 짝지어 줄 수 있는 방법의 수를 출력한다.

##### 예제 입력

```tex
3
2 1
0 1
4 6
0 1 1 2 2 3 3 0 0 2 1 3
6 10
0 1 0 2 1 2 1 3 1 4 2 3 2 4 3 4 3 5 4 5
```

##### 예제 출력

```tex
1
3
4
```

#### 풀이

##### 완전탐색

가능한 조합의 수를 계산하는 문제를 푸는 가장 간단한 방법은 완전 탐색을 이용해 조합을 모두 만들어 보는것

##### 중복으로 세는 문제

```java
    public static int countPairing(ArrayList<Integer>[] table, boolean [] mark, int current, int toPick) {

        if (toPick == 0) {
            return 1;
        }
        int result = 0;
        for (Integer target : table[current]) {
            if (!mark[target]) {
                mark[target] = true;
                result += countPairing(table, mark, target, toPick-1);
                mark[target] = false;
            }
        }
        return result;
    }
```

코드를 보면 문제점을 찾을 수 있다.

- 다른 순서로 학생들을 짝지어 주는 것을 서로 다른 경우로 세고 있다. 예를 들어 (0,1) 후에 (2,3)을 짝지어 주는 것과 (2,3) 후에 (1,0)을 짝지어 주는 것은 완전히 같은 방법인데 다르게 세고 있다.

이 상황을 해결하기 위해 선택할 수 있는 좋은 방법은 항상 특정 형태를 갖는 답만을 세는 것이다. 이 속성을 강제하기 위해서는 각 단계에서 남아 있는 학생들 중 가장 번호가 빠른 학생의 짝을 찾아 주도록 한다.

```java
public static int countPairing(ArrayList<Integer>[] table, boolean [] mark, int n) {
        int small = -1;
        for (int i = 0; i < n; i++) {
            if (!mark[i]) {
                small = i;
                break;
            }
        }
        if (small == -1) {
            return 1;
        }
        int result = 0;
        mark[small] = true;
        for (Integer target : table[small]) {
            if (!mark[target]) {
                mark[target] = true;
                result += countPairing(table, mark, n);
                mark[target] = false;
            }
        }
        mark[small] = false;
        return result;
    }
```

### 게임판 덮기

#### 문제

H*W 크기의 게임판이 있다. 게임판은 검은 칸과 흰칸으로 구성된 격자 모양을 하고 있는데 이 중 모든 흰 칸을 세칸짜리 L자 모양의 블록으로 덮고 싶다. 이때 블록들은 자유롭게 회전해서 놓을 수 있지만, 서로 겹치거나, 검은칸을 덮거나, 게임판 밖으로 나가서는 안된다. 게임판이 주어질 떄 이를 덮는 방법의 수를 계산하는 프로그램을 작성해라

##### 시간 및 메모리 제한

프로그램은 2초 안에 실행되어야 하며, 64MB 이하의 메모리를 사용해야만 한다.

##### 입력

입력의 첫줄에는 테스트 케이스의 수가 주어진다. 각 테스트 케이스의 첫 줄에는 두 개의 정수 H,W가 주어진다. 다음 H 줄에 각 W 글자로 게임판의 모양이 주어진다. #은 검은칸 .은 흰칸을 나타낸다. 입력에 주어지는 게임판에 있는 흰 칸의 수는 50을 넘지 않는다.

##### 출력

한줄에 하나씩 흰 칸을 모두 덮는 방법의 수를 출력한다.

##### 예제 입력

```
3
3 7
#.....#
#.....#
##...##
3 7
#.....#
#.....#
##..###
8 10
##########
#........#
#........#
#........#
#........#
#........#
#........#
##########
```

##### 예제 출력

```
0
2
1514
```

#### 풀이

주어진 게임판에서 흰 칸의 수가 3의 배수가 아닐 경우 무조건 답이 없으니 이 부분을 따로 처리 해야한다.

##### 중복으로 세는 문제

블록을 놓는 순서는 이 문제에서 중요하지 않은데, 같은 배치도 블록을 놓는 순서에 따라서 여러번 세기 때문이다. 특정한 순서대로 답을 생성하도록 강제할 필요가 있다.

##### 구현

```java
class Solution {
    boolean [][] table;
    int maxX;
    int maxY;
    public static final int [][][] types = {
            {{0,0}, {1,0}, {0,1}},
            {{0,0},{0,1},{1,1}},
            {{0,0}, {1,0}, {1,1}},
            {{0,0}, {1,0}, {1,-1}}
    };
    public int solution(boolean [][] table, int h, int w) {
        this.table = table;
        this.maxX = h-1;
        this.maxY = w-1;
        return cover();
    }

    private boolean canSet(int x, int y, int typeNum) {
        return Arrays.stream(types[typeNum]).noneMatch(type -> {
            int nextX = x + type[0];
            int nextY = y + type[1];
            if (nextX > maxX || nextY > maxY || nextX < 0 || nextY < 0) {
                return true;
            }else return !table[nextX][nextY];
        });
    }
    private void set(int x, int y, int typeNum, boolean toSet) {
        Arrays.stream(types[typeNum]).forEach(type -> {
            int nextX = x + type[0];
            int nextY = y + type[1];
            table[nextX][nextY] = toSet;
        });
    }


    private int cover() {
        int x = -1;
        int y = -1;

        loop: for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                if (table[i][j]) {
                    x = i;
                    y = j;
                    break loop;
                }
            }
        }

        if (x == -1) return 1;
        int result = 0;
        for (int i = 0; i < 4; i++) {
            if (canSet(x, y, i)) {
                set(x, y, i, false);
                result += cover();
                set(x, y, i, true);
            }
        }
        return result;
    }
}
```

