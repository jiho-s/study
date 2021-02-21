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