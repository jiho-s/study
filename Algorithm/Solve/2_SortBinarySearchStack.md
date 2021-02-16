## 정렬, 이분탐색, 스택

### 목차

33. [3등의 성적은?](#3등의-성적은)
34. 
35. [SpecialSort](#specialsort)
36. 
37. [Least Recently Used](#least-recently-used)
38. [Inversion Sequence](#inversion-sequence)
39. [두 배열 합치기](#두-배열-합치기)
40. [교집합](#교집합)

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

### 두 배열 합치기

### 교집합