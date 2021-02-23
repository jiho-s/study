## 정렬, 이분탐색, 스택

### 목차

33. [3등의 성적은?](#3등의-성적은)
34. 
35. [SpecialSort](#specialsort)
36. 
37. [Least Recently Used](#least-recently-used)
38. [Inversion Sequence](#inversion-sequence)
39. 
40. [교집합](#교집합)
41. [연속된 자연수의 합](#연속된-자연수의-합)
42. [이분검색](#이분검색)
43. [뮤직비디오](#뮤직비디오)
44. [마구간 정하기](#마구간 정하기)
45. [공주 구하기](#공주-구하기)
46. [멀티태스킹](#멀티태스킹)
47. [봉우리](#봉우리)
48. [각 행의 평균과 가장 가까운 값](#각-행의-평균과-가장-가까운-값)
49. [블록의 최대값](#블록의 최대값)
50. [영지 선택 (small)](#영지-선택-small)

### 3등의 성적은?

#### 문제

N명의 수학성적이 주어지면 그 중 3등을 한 수학성적을 출력하는 프로그램을 작성하세요. 

만약 학생의 점수가 100점이 3명, 99점이 2명, 98점이 5명, 97점이 3명 이런식으로 점수가 분포되면 1등은 3명이며, 2등은 2명이며 3등은 5명이 되어 98점이 3등을 한 점수가 됩니다.

#### 풀이

```java
public static String solution33(List<Integer> list) {
    List<Integer> sorted = list.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    for (int i = 1, count = 0, len = sorted.size(); i < len; i++) {
        if (sorted.get(i-1) != sorted.get(i)) {
            count++;
        }
        if (count == 2) {
            return String.valueOf(sorted.get(i));
        }
    }
    return String.valueOf(-1);
}
```

### SpecialSort

#### 문제

N개의 정수가 입력되면 당신은 입력된 값을 정렬해야 한다.
 음의 정수는 앞쪽에 양의정수는 뒷쪽에 있어야 한다. 또한 양의정수와 음의정수의 순서에는 변함이 없어야 한다.

#### 풀이

```java
public static String solution35(List<Integer> list) {
    return list.stream().sorted((a, b) -> {
        if (a < 0 && b < 0) {
            return 0;
        }
        return a - b;
    }).map(String::valueOf).collect(Collectors.joining(" "));
}
```

### Least Recently Used

#### 문제

캐시메모리는 CPU와 주기억장치(DRAM) 사이의 고속의 임시 메모리로서 CPU가 처리할 작업 을 저장해 놓았다가 필요할 바로 사용해서 처리속도를 높이는 장치이다. 워낙 비싸고 용량이 작아 효율적으로 사용해야 한다. 철수의 컴퓨터는 캐시메모리 사용 규칙이 LRU 알고리즘을 따 른다. LRU 알고리즘은 Least Recently Used 의 약자로 직역하자면 가장 최근에 사용되지 않 은 것 정도의 의미를 가지고 있습니다. 캐시에서 작업을 제거할 때 가장 오랫동안 사용하지 않은 것을 제거하겠다는 알고리즘입니다.

만약캐시의사이즈가5이고작업이 2 3 1 6 7 순으로저장되어있다면, (맨 앞이 가장 최근에 쓰인 작업이고, 맨 뒤는 가장 오랫동안 쓰이지 않은 작업이다.)

1) Cache Miss : 해야할 작업이 캐시에 없는 상태로 위 상태에서 만약 새로운 작업인 5번 작 업을 CPU가 사용한다면 Cache miss가 되고 모든 작업이 뒤로 밀리고 5번작업은 캐시의 맨

앞에위치한다. 5 2 3 1 6 (7번작업은캐시에서삭제된다.)
 2) Cache Hit : 해야할 작업이 캐시에 있는 상태로 위 상태에서 만약 3번 작업을 CPU가 사용

한다면 Cache Hit가 되고, 63번 앞에 있는 5, 2번 작업은 한 칸 뒤로 밀리고, 3번이 맨 앞으

로 위치하게 된다. 5 2 3 1 6 ---> 3 5 2 1 6

캐시의 크기가 주어지고, 캐시가 비어있는 상태에서 N개의 작업을 CPU가 차례로 처리한다면 N개의 작업을 처리한 후 캐시메모리의 상태를 가장 최근 사용된 작업부터 차례대로 출력하는 프로그램을 작성하세요.

#### 풀이

```java
public static String solution37(int n, List<Integer> jobs) {
    LinkedList<Integer> queue = new LinkedList<>();
    jobs.forEach(job -> {
        queue.remove(job);
        if (queue.size() >= 5) {
            queue.removeLast();
        }
        queue.addFirst(job);
    });
    return queue.stream().map(String::valueOf).collect(Collectors.joining(" "));

}
```

### Inversion Sequence

#### 문제

1부터 n까지의 수를 한 번씩만 사용하여 이루어진 수열이 있을 때, 1부터 n까지 각각의 수 앞에 놓여 있는 자신보다 큰 수들의 개수를 수열로 표현한 것을 Inversion Sequence라 한다.

예를 들어 다음과 같은 수열의 경우 48625137

1앞에 놓인 1보다 큰 수는 4, 8, 6, 2, 5. 이렇게 5개이고, 2앞에 놓인 2보다 큰 수는 4, 8, 6. 이렇게 3개,
 3앞에 놓인 3보다 큰 수는 4, 8, 6, 5 이렇게 4개......

따라서4 8 6 2 5 1 3 7의inversionsequence는53402110이된다.
 n과 1부터 n까지의 수를 사용하여 이루어진 수열의 inversion sequence가 주어졌을 때, 원래 의 수열을 출력하는 프로그램을 작성하세요.

#### 풀이

```java
public static String solution38(List<Integer> list) {
    int [] answer = new int[list.size()];
    IntStream.range(0, list.size()).forEach(num -> {
        int index = list.get(num);
        for (int i = 0; i <= index; i++) {
            if (answer[i] != 0) {
                index++;
            }
        }
        answer[index] = num + 1;
    });
    return Arrays.stream(answer).mapToObj(String::valueOf).collect(Collectors.joining(" "));
}
```

### 교집합

#### 문제

두 집합 A, B가 주어지면 두 집합의 교집합을 출력하는 프로그램을 작성하세요.

#### 풀이

```java
public static String solution40(List<Integer> a, List<Integer> b) {
    a = a.stream().sorted(Comparator.comparing(Integer::valueOf)).collect(Collectors.toList());
    b = b.stream().sorted(Comparator.comparing(Integer::valueOf)).collect(Collectors.toList());
    List<Integer> result = new ArrayList<>();
    for (int i = 0, j = 0, alen = a.size(), blen = b.size(); i < alen && j < blen;) {
        if (a.get(i).equals(b.get(j))) {
            result.add(a.get(i));
            i++;
            j++;
        } else if (a.get(i) > b.get(i)) {
            j++;
        } else {
            i++;
        }
    }
    return result.stream().map(String::valueOf).collect(Collectors.joining(" "));
}
```

### 연속된 자연수의 합

#### 문제

입력으로 양의 정수 N이 입력되면 2개 이상의 연속된 자연수의 합으로 정수 N을 표현하는 방 법의 가짓수를 출력하는 프로그램을 작성하세요.
 만약 N=15이면
$$
7+8=15\\
4+5+6=15\\
1+2+3+4+5=15
$$

 와 같이 총 3가지의 경우가 존재한다.

#### 풀이

```java
public static String solution41(int number) {
    int n = number;
    StringBuilder stringBuilder = new StringBuilder();
    int b = 1, result = 0;
    n -= b;
    while (n > 0) {
        b += 1;
        n -= b;
        if (n % b == 0) {
            result++;
            int div = n /b;
            String collect = IntStream.range(0, b).mapToObj(i -> String.valueOf(i + div + 1)).collect(Collectors.joining(" + "));
            stringBuilder.append(collect).append(" = ").append(number).append("\n");
        }
    }
    return stringBuilder.append(result).toString();
}
```

### 이분검색

#### 문제

임의의 N개의 숫자가 입력으로 주어집니다. N개의 수를 오름차순으로 정렬한 다음 N개의 수 중 한 개의 수인 M이 주어지면 이분검색으로 M이 정렬된 상태에서 몇 번째에 있는지 구하는 프로그램을 작성하세요.

#### 풀이

```java
public static String solution42(int n, List<Integer> list) {
    list = list.stream().sorted().collect(Collectors.toList());
    for (int start = 0, end = list.size(); start <= end;) {
        int mid = (start + end) / 2;
        if (list.get(mid).equals(n)) {
            return String.valueOf(mid);
        } else if (list.get(mid) > mid) {
            start = mid + 1;
        } else  {
            end = mid;
        }
    }
    return "";
}
```

### 뮤직비디오

#### 문제

지니레코드에서는 불세출의 가수 조영필의 라이브 동영상을 DVD로 만들어 판매하려 한다. DVD에는 총 N개의 곡이 들어가는데, DVD에 녹화할 때에는 라이브에서의 순서가 그대로 유지 되어야 한다. 순서가 바뀌는 것을 우리의 가수 조영필씨가 매우 싫어한다. 즉, 1번 노래와 5번 노래를 같은 DVD에 녹화하기 위해서는 1번과 5번 사이의 모든 노래도 같은 DVD에 녹화해야 한다.

지니레코드 입장에서는 이 DVD가 팔릴 것인지 확신할 수 없기 때문에 이 사업에 낭비되는 DVD를 가급적 줄이려고 한다. 고민 끝에 지니레코드는 M개의 DVD에 모든 동영상을 녹화하기 로 하였다. 이 때 DVD의 크기(녹화 가능한 길이)를 최소로 하려고 한다. 그리고 M개의 DVD는 모두 같은 크기여야 제조원가가 적게 들기 때문에 꼭 같은 크기로 해야 한다.

#### 풀이

```java
import java.util.List;

class Solution {
    public String solution43(List<Integer> list, int n) {
        int max = list.stream().mapToInt(Integer::intValue).sum();
        int min = max / n;
        while (min < max) {
            int mid = (max + min) / 2;
            int sum = 0;
            int count = 1;
            for (Integer integer : list) {
                int next = integer + sum;
                if (next > mid) {
                    sum = integer;
                    count++;
                } else {
                    sum = next;
                }
            }
            if (count > n) {
                min = mid + 1;
            } else {
                max = mid;
            }
        }
        return String.valueOf(min);
    }
}
```

### 마구간 정하기

#### 문제

N개의 마구간이 1차원 수직선상에 있습니다. 각 마구간은 x1, x2, x3, ......, xN의 좌표를 가 지며, 마구간간에 좌표가 중복되는 일은 없습니다.
 현수는 C마리의 말을 가지고 있는데, 이 말들은 서로 가까이 있는 것을 좋아하지 않습니다. 각 마구간에는 한 마리의 말만 넣을 수 있고, 가장 가까운 두 말의 거리가 최대가 되게 말을 마구간에 배치하고 싶습니다.

C마리의 말을 N개의 마구간에 배치했을 때 가장 가까운 두 말의 거리가 최대가 되는 그 최대 값을 출력하는 프로그램을 작성하세요.

#### 풀이

```java
public String solution44(List<Integer> list, int numOfHorses) {
    List<Integer> collect = list.stream().sorted().collect(Collectors.toList());
    int min = 1;
    int len = collect.size();
    int max = collect.get(len - 1);
    int answer = 0;
    while (min <= max) {
        int mid = (min + max) / 2;
        var ref = new Object() {
            int pre = collect.get(0);
            int count = 1;
        };
        IntStream.range(1, len).forEach(i -> {
            Integer next = collect.get(i);
            if (next - ref.pre >= mid) {
                ref.pre = next;
                ref.count += 1;
            }
        });
        if (ref.count >= numOfHorses) {
            min = mid + 1;
            answer = mid;
        }
        else {
            max = mid - 1;
        }
    }
    return String.valueOf(answer);
}
```

### 공주 구하기

#### 문제

정보 왕국의 이웃 나라 외동딸 공주가 숲속의 괴물에게 잡혀갔습니다.
 정보 왕국에는 왕자가 N명이 있는데 서로 공주를 구하러 가겠다고 합니다. 정보왕국의 왕은 다음과 같은 방법으로 공주를 구하러 갈 왕자를 결정하기로 했습니다.
 왕은 왕자들을 나이 순으로 1번부터 N번까지 차례로 번호를 매긴다. 그리고 1번 왕자부터 N 번 왕자까지 순서대로 시계 방향으로 돌아가며 동그랗게 앉게 한다. 그리고 1번 왕자부터 시 계방향으로 돌아가며 1부터 시작하여 번호를 외치게 한다. 한 왕자가 K(특정숫자)를 외치면 그 왕자는 공주를 구하러 가는데서 제외되고 원 밖으로 나오게 된다. 그리고 다음 왕자부터 다시 1부터 시작하여 번호를 외친다.
 이렇게 해서 마지막까지 남은 왕자가 공주를 구하러 갈 수 있다.

예를 들어 총 8명의 왕자가 있고, 3을 외친 왕자가 제외된다고 하자. 처음에는 3번 왕자가 3 을 외쳐 제외된다. 이어 6, 1, 5, 2, 8, 4번 왕자가 차례대로 제외되고 마지막까지 남게 된 7 번 왕자에게 공주를 구하러갑니다.
 N과 K가 주어질 때 공주를 구하러 갈 왕자의 번호를 출력하는 프로그램을 작성하시오.

#### 풀이

```java
public String solution45(int num, int seq) {
    LinkedList<Integer> collect = IntStream.rangeClosed(1, num)
            .sorted().boxed()
            .collect(Collectors.toCollection(LinkedList::new));
    int index = 0;
    while (collect.size() != 1) {
        for (int i = 0; i < seq - 1; i++) {
            index++;
        }
        index %= collect.size();
        collect.remove(index);
    }
    return collect.getFirst().toString();
}
```

### 멀티태스킹

#### 문제

현수의 컴퓨터는 멀티태스킹이 가능하다. 처리해야 할 작업이 N개 들어오면 현수의 컴퓨터는

작업을 1부터 N까지의 번호를 부여하고 처리를 다음과 같이 한다.

1) 컴퓨터는 1번 작업부터 순서대로 1초씩 작업을 한다. 즉 각 작업을 1초만 작업하고 다음 작업을 하는 식이다.
 2) 마지막 번호의 작업을 1초 했으면 다시 1번 작업으로 가서 다시 1초씩 후속 처리를 한다. 3) 처리가 끝난 작업은 작업 스케쥴에서 사라지고 새로운 작업은 들어오지 않는다.

그런데 현수의 컴퓨터가 일을 시작한 지 K초 후에 정전이 되어 컴퓨터가 일시적으로 멈추었 다. 전기가 들어오고 나서 현수의 컴퓨터가 몇 번 작업부터 다시 시작해야 하는지 알아내는 프로그램을 작성하세요.

#### 풀이

```java
public String solution46(List<Integer> jobs, int outTime) {
    ArrayList<Integer> list = new ArrayList<>(jobs);
    int len = list.size();
    int i1 = IntStream.range(0, outTime).reduce((index, i) -> {
        int next = index;
        int current = 0;
        while ((current = list.get(next)) == 0) {
            next = (next + 1) % len;
        }
        current--;
        list.set(next, current);
        return next;
    }).orElse(-1);
    return String.valueOf(i1 + 1);
}
```

### 봉우리

#### 문제

지도 정보가 N*N 격자판에 주어집니다. 각 격자에는 그 지역의 높이가 쓰여있습니다. 각 격자 판의 숫자 중 자신의 상하좌우 숫자보다 큰 숫자는 봉우리 지역입니다. 봉우리 지역이 몇 개 있는 지 알아내는 프로그램을 작성하세요.
 격자의 가장자리는 0으로 초기화 되었다고 가정한다.

만약 N=5 이고, 격자판의 숫자가 다음과 같다면 봉우리의 개수는 10개입니다.

#### 풀이

```java
public String solution47(int [][] input) {
    ArrayList<ArrayList<Integer>> table = new ArrayList<>();
    table.add(new ArrayList<>());
    IntStream.range(0,input.length+2).forEach(i ->table.get(0).add(0));
    IntStream.range(0,input.length).forEach(i -> {
        table.add(new ArrayList<>());
        table.get(i+1).add(0);
        IntStream.range(0, input[i].length).forEach(j -> {
            table.get(i+1).add(input[i][j]);
        });
        table.get(i+1).add(0);
    });
    IntStream.range(0, input.length+2).forEach(i -> table.get(input.length+1).add(0));
    List<List<Integer>> offsets = List.of(List.of(0, 1), List.of(0, -1), List.of(1, 0), List.of(-1, 0));
    long sum = IntStream.range(1, table.size()).mapToLong(i -> {
        return IntStream.range(1, table.get(i).size()).filter(j -> {
            return offsets.stream().noneMatch(offset -> {
                return table.get(i + offset.get(0)).get(j + offset.get(1)) > table.get(i).get(j);
            });
        }).count();
    }).sum();
    return String.valueOf(sum);
}
```

### 각 행의 평균과 가장 가까운 값

#### 문제

 9×9 격자판에 쓰여진 81개의 자연수가 주어질 때, 각 행의 평균을 구하고, 그 평균과 가장 가까운 값을 출력하는 프로그램을 작성하세요. 평균은 소수점 첫 째 자리에서 반 올림합니다. 평균과 가까운 값이 두 개이면 그 중 큰 값을 출력하세요.

#### 풀이

```java
public String solution48(List<List<Integer>> input) {
    StringBuilder stringBuilder = new StringBuilder();
    input.stream().forEach(line -> {
        int avg = line.stream().mapToInt(Integer::intValue).sum() / line.size();
        int diff = Integer.MAX_VALUE;
        Integer num = line.stream().min((a, b) -> {
            return abs(avg - a) - abs(avg - b);
        }).orElse(-1);
        stringBuilder.append(avg).append(" ").append(num).append("\n");
    });
    return stringBuilder.toString();
}
```

### 블록의 최대값

#### 문제

현수는 블록놀이를 좋아합니다. 현수에게 정면에서 본 단면과 오른쪽 측면에서 본 단면을 주 고 최대 블록개수를 사용하여 정면과 오른쪽 측면에서 본 모습으로 블록을 쌓으라 했습니다. 현수가 블록을 쌓는데 사용해야 할 최대 개수를 출력하는 프로그램을 작성하세요.

첫 줄에 블록의 크기 N(3<=N<=10)이 주어집니다. 블록이 크기는 정사각형 N*N입니다. 두 번째 줄에 N개의 정면에서의 높이 정보가 왼쪽 정보부터 주어집니다.
 세 번째 줄에 N개의 오른쪽 측면 높이 정보가 앞쪽부터 주어집니다.
 블록의 높이는 10 미만입니다.

#### 풀이

```java
public String solution49(List<List<Integer>> input) {
    int len = input.size();
    int result = IntStream.range(0, len).map(i -> {
        return IntStream.range(0, len).map(j -> {
            return min(input.get(0).get(j), input.get(1).get(len - 1 - i));
        }).sum();
    }).sum();
    return String.valueOf(result);
}
```

### 영지 선택 Small

#### 문제

세종대왕은 현수에게 현수가 다스릴 수 있는 영지를 하사하기로 했다. 전체 땅은 사각형으로 표 시된다. 그 사각형의 땅 중에서 세종대왕이 현수가 다스릴 수 있는 땅의 크기(세로의 길이와 가 로의 길이)를 정해주면 전체 땅 중에서 그 크기의 땅의 위치를 현수가 정하면 되는 것이다. 전체 땅은 사각형의 모양의 격자로 되어 있으며, 그 사각형 땅 안에는 많은 오렌지 나무가 심 겨져 있다. 현수는 오렌지를 무척 좋아하여 오렌지 나무가 가장 많이 포함되는 지역을 선택하 고 싶어 한다. 현수가 얻을 수 있는 영지의 오렌지 나무 최대 개수를 출력하는 프로그램을 작 성하세요. 다음과 같은 땅의 정보가 주어지고, 현수가 하사받을 크기가, 가로 2, 세로 3의 크 기이면 가장 많은 오렌지 나무가 있는 영지는 총 오렌지 나무의 개수가 16인 3행 4열부터 시 작하는 구역이다.

#### 풀이