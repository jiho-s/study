## 코드 구현력 기르기

### 목차

1. [1부터 N까지 M배수 합](#1부터-n까지-m배수-합)
2. [자연수의 합](#자연수의-합)
3. [진약수의 합](#진약수의-합)
4. [나이 차이](#나이-차이)
5. [나이 계산](#나이-계산)
6. [숫자만 추출](#숫자만-추출)
7. [영어 단어 복구](#영어-단어-복구)
8. [올바른 괄호](#올바른-괄호)
9. [모두의 약수](#모두의-약수)
10. [자리수의 합](#자리수의-합)
11. [숫자의 총 개수(small)](#숫자의-총-개수-small)
12. [숫자의 총 개수(large)](#숫자의-총-개수-large)
13. [가장 많이 사용된 자릿수](#가장-많이-사용된-자릿수)
14. [뒤집은 소수](#뒤집은-소수)
15. [소수의 개수](#소수의-개수)
16. [Anagram](#anagram)
17. [선생님 퀴즈](#선생님-퀴즈)
18. [층간소음](#층간소음)
19. [분노 유발자](#분노-유발자)
20. [가위 바위 보](#가위-바위-보)
21. [카드게임](#카드게임)
22. [온도의 최대값](#온도의-최대값)
23. [연속 부분 증가 수열](#연속-부분-증가-수열)
24. [Jolly Jumpers](#jolly-jumpers)
25. [석차 구하기](#석차-구하기)
26. [마라톤](#마라톤)
27. [N!의 표현법](#N!의-표현법)
28. [N!에서 0의 개수](#N!에서-0의-개수)
29. [3의 개수는(small)](#3의 개수는 small)
30. [3의 개수는(large)](#3의 개수는-large)

### 1부터 N까지 M배수 합

#### 문제

자연수 N이 입력되면 1부터 N까지의 수 중 M의 배수합을 출력하는 프로그램을 작성하세요.

#### 풀이

```java
    public static int solution1(int n, int m) {
        return IntStream.iterate(m, i -> i <= n, i -> i + m).sum();
    }
```

### 자연수의 합

#### 문제

자연수 A, B가 주어지면 A부터 B까지의 합을 수식과 함께 출력하세요.

```java
    public static int solution2(int a, int b) {
        return IntStream.rangeClosed(a, b).reduce(0, (result, i) -> {
            if (i == b) {
                System.out.print(i + " = ");
            }else {
                System.out.print(i + " + ");
            }
            result += i;
            return result;
        });
    }
```

### 진약수의 합

#### 문제

자연수 N이 주어지면 자연수 N의 진약수의 합을 수식과 함께 출력하는 프로그램을 작성하세

요.

#### 풀이

```java
    public static int solution3(int n) {
        return IntStream.rangeClosed(1, n/2).reduce(0, (result, i) -> {
            if (n % i != 0) {
                return result;
            }
            if (i == 1) {
                System.out.print(i);
            } else {
                System.out.print(" + " + i);
            }
            result += i;
            return result;
        });
    }
```

### 나이 차이

#### 문제

N(2<=N<=100)명의 나이가 입력됩니다. 이 N명의 사람 중 가장 나이차이가 많이 나는 경우는 몇 살일까요? 최대 나이 차이를 출력하는 프로그램을 작성하세요.

#### 풀이

```java
public static int solution4(int [] ages) {
    sort(ages);
    return ages[ages.length-1] - ages[0];
}
```

### 나이 계산

#### 문제

주민등록증의 번호가 주어지면 주민등록증 주인의 나이와 성별을 판단하여 출력하는 프로그램 을 작성하세요. 주민등록증의 번호는 -를 기준으로 앞자리와 뒷자리로 구분된다.
 뒷자리의 첫 번째 수가 1이면 1900년대생 남자이고, 2이면 1900년대생 여자, 3이면 2000년대 생 남자, 4이면 2000년대생 여자이다.

올해는 2019년입니다. 해당 주민등록증 주인의 나이와 성별을 출력하세요.

#### 풀이

```java
public static String solution5(String idString) {
    int current = 2019;
    current += 1;
    String[] split = idString.split("-");
    int year = Integer.parseInt(split[0].substring(0,2));
    switch (split[1].charAt(0)) {
        case '1':
            year += 1900;
            return current - year + " M";
        case '2':
            year += 1900;
            return current - year + " W";
        case '3':
            year += 2000;
            return current - year + " M";
        case '4':
            year += 2000;
            return current - year + " W";
        default:
            return ">?????";
    }
}
```

### 숫자만 추출

#### 문제

문자와 숫자가 섞여있는 문자열이 주어지면 그 중 숫자만 추출하여 그 순서대로 자연수를 만 듭니다. 만들어진 자연수와 그 자연수의 약수 개수를 출력합니다.
 만약 “t0e0a1c2her”에서 숫자만 추출하면 0, 0, 1, 2이고 이것을 자연수를 만들면 12가 됩니 다.즉첫자리0은자연수화할때무시합니다. 출력은12를출력하고,다음줄에12의약 수의 개수를 출력하면 됩니다.

추출하여 만들어지는 자연수는 100,000,000을 넘지 않습니다.

#### 풀이

```java
public static String solution6(String s) {
    String collect = s.chars().filter(Character::isDigit).mapToObj(c -> (char) c + "").collect(Collectors.joining());
    int num = Integer.parseInt(collect);
    long count = IntStream.range(1, num / 2 + 1).filter(i -> num % i == 0).count();
    return num + " " + (count + 1);
}
```

### 영어 단어 복구

#### 문제

현수의 컴퓨터가 바이러스에 걸려 영어단어가 뛰어쓰기와 대소문자가 혼합되어 표현된다.
 예를 들면 아름다운 이란 뜻을 가지고 있는 beautiful 단어가 “bE au T I fu L” 과 같이 컴퓨터에 표시되고 있습니다. 위와 같이 에러로 표시되는 영어단어를 원래의 표현대로 공백을 제거하고 소문자화 시켜 출력하는 프로그램을 작성하세요.

#### 풀이

```java
public static String solution7(String s) {
    return s.replace(" ", "").toLowerCase();
}
```

### 올바른 괄호

#### 문제

괄호가 입력되면 올바른 괄호이면 “YES", 올바르지 않으면 ”NO"를 출력합니다. 

(())() 이것은 괄호의 쌍이 올바르게 위치하는 거지만, (()()))은 올바른 괄호가 아니다.

#### 풀이

스택으로 푸는것

```java
    public static String solution8(String s) {
        Stack<Character> stack = new Stack<>();
        boolean answer = s.chars().noneMatch(c -> {
            if (c == '(') {
                stack.push((char) c);
                return false;
            } else if (c == ')' && stack.isEmpty()) {
                return true;
            }
            stack.pop();
            return false;
        });
        if (answer) {
            answer = stack.isEmpty();
        }
        return answer ? "YES" : "NO";
    }
```



### 모두의 약수

#### 문제

자연수 N이 입력되면 1부터 N까지의 각 숫자들의 약수의 개수를 출력하는 프로그램을 작성하 세요. 만약 N이 8이 입력된다면 1(1개), 2(2개), 3(2개), 4(3개), 5(2개), 6(4개), 7(2개), 8(4 개) 와 같이 각 숫자의 약수의 개수가 구해집니다.

 출력은 다음과 같이 1부터 차례대로 약수의 개수만 출력하면 됩니다.

1 2 2 3 2 4 2 4 와 같이 출력한다.

#### 풀이

테이블에 각각의 숫자를 배수 한 값을 1증가 시켜 저장한후 이를 출력한다

```java
    public static String solution9(int n) {
        int [] table = new int[n+1];
        return IntStream.rangeClosed(1, n).mapToObj(i -> {
            IntStream.iterate(i, j -> j <= n, j -> j + i).forEach(j -> {
                table[j]++;
            });
            return table[i] + "";
        }).collect(Collectors.joining(" "));
    }
```

### 자리수의 합

#### 문제

N개의 자연수가 입력되면 각 자연수의 자릿수의 합을 구하고, 그 합이 최대인 자연수를 출력 하는 프로그램을 작성하세요. 각 자연수의 자릿수의 합을 구하는 함수를 int digit_sum(int x)를 꼭 작성해서 프로그래밍 하세요.

#### 풀이

```java
    public static String solution10(String input) {
        var max = new Object() {
            Integer maxSum = Integer.MIN_VALUE;
            Integer max = Integer.MIN_VALUE;
        };
        String[] split = input.split(" ");
        Arrays.stream(split).forEach(s -> {
            int sum = s.chars().reduce(0, (result, i) -> {
                char c = (char) i;
                result += (i - '0');
                return result;
            });
            if (max.maxSum < sum) {
                max.maxSum = sum;
            }else if (max.maxSum == sum) {
                int current = Integer.parseInt(s);
                if (max.max < current) {
                    max.max = current;
                }
            }
        });
        return max.max + "";
    }
```

### 숫자의 총 개수 small

#### 문제

자연수 N이 입력되면 1부터 N까지의 자연수를 종이에 적을 때 각 숫자는 몇 개 쓰였을까요? 예를 들어 1부터 15까지는 1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 0, 1, 1, 1, 2, 1, 3, 1, 4, 1, 5으로 총 21개가 쓰였음을 알 수 있습니다.
 자연수 N이 입력되면 1부터 N까지 각 숫자는 몇 개가 사용되었는지를 구하는 프로그램을 작 성하세요.

#### 풀이

```java
    public static String solution11(int n) {
        int reduce = IntStream.rangeClosed(1, n).reduce(0, (result, i) -> {
            int cnt = 0;
            while (i > 0) {
                cnt++;
                i /= 10;
            }
            result += cnt;
            return result;
        });
        return String.valueOf(reduce);
    }
```

### 숫자의 총 개수 large

#### 문제

자연수 N이 입력되면 1부터 N까지의 자연수를 종이에 적을 때 각 숫자는 몇 개 쓰였을까요? 예를 들어 1부터 15까지는 1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 0, 1, 1, 1, 2, 1, 3, 1, 4, 1, 5으로 총 21개가 쓰였음을 알 수 있습니다.
 자연수 N이 입력되면 1부터 N까지 각 숫자는 몇 개가 사용되었는지를 구하는 프로그램을 작 성하세요.

#### 풀이

```java
    public static String solution12(int n) {
        if (n < 10) {
            return String.valueOf(n);
        }
        int result = -1;
        for (int i = 1, bound = 0; ; i++) {
            int preBound = bound;
            bound = bound*10 + 9;
            if (bound < n) {
                result += i*10;
                continue;
            }
            result += (n - preBound) * i;
            break;
        }
        return String.valueOf(result);
    }
```

### 가장 많이 사용된 자릿수

#### 문제

N자리의 자연수가 입력되면 입력된 자연수의 자릿수 중 가장 많이 사용된 숫자를 출력하는 프 로그램을 작성하세요.
 예를 들어 1230565625라는 자연수가 입력되면 5가 3번 상용되어 가장 많이 사용된 숫자입니 다. 답이 여러 개일 경우 그 중 가장 큰 수를 출력하세요.

#### 풀이

```java
public static String solution13(String s) {
    int [] table = new int[10];
    s.chars().forEach(c -> {
        table[c - '0']++;
    });
    int result = -1;
    int max = Integer.MIN_VALUE;
    for (int i = 9; i >=0; i--) {
        if (table[i] > max) {
            max = table[i];
            result = i;
        }
    }
    return String.valueOf(result);
}
```

### 뒤집은 소수

#### 문제

N개의 자연수가 입력되면 각 자연수를 뒤집은 후 그 뒤집은 수가 소수이면 그 수를 출력하는 프로그램을 작성하세요. 예를 들어 32를 뒤집으면 23이고, 23은 소수이다. 그러면 23을 출력 한다. 단 910를 뒤집으면 19로 숫자화 해야 한다. 첫 자리부터의 연속된 0은 무시한다.
 뒤집는 함수인 int reverse(int x) 와 소수인지를 확인하는 함수 bool isPrime(int x)를 반드시 작성하여 프로그래밍 한다.

#### 풀이

```java
public static String solution14(List<Integer> list) {
    return list.stream().map(num -> {
        int reverseNum = 0;
        while (num > 0) {
            int temp = num % 10;
            reverseNum = reverseNum*10 + temp;
            num = num / 10;
        }
        return reverseNum;
    }).filter(num -> {
        return IntStream.rangeClosed(2, (int)sqrt(num)).noneMatch(i -> num % i == 0);
    }).map(String::valueOf).collect(Collectors.joining(" "));
}
```

### 소수의 개수

#### 문제

자연수 N이 입력되면 1부터 N까지의 소수의 개수를 출력하는 프로그램을 작성하세요. 만약 20이 입력되면 1부터 20까지의 소수는 2, 3, 5, 7, 11, 13, 17, 19로 총 8개입니다. 제한시간은 1초입니다.

#### 풀이

```java
public static String solution15(int n) {
    boolean [] table = new boolean[n+1];
    long count = IntStream.rangeClosed(2, n).filter(i -> {
        if (table[i])
            return false;
        IntStream.iterate(i, j -> j <= n, j -> j + i).forEach(j -> {
            table[j] = true;
        });
        return true;
    }).count();
    return String.valueOf(count); 
}
```

### Anagram

#### 문제

Anagram이란 두 문자열이 알파벳의 나열 순서를 다르지만 그 구성이 일치하면 두 단어는 아 나그램이라고 합니다.
 예를 들면 AbaAeCe 와 baeeACA 는 알파벳을 나열 순서는 다르지만 그 구성을 살펴보면 A(2), a(1), b(1), C(1), e(2)로 알파벳과 그 개수가 모두 일치합니다. 즉 어느 한 단어를 재 배열하면 상대편 단어가 될 수 있는 것을 아나그램이라 합니다.

길이가 같은 두 개의 단어가 주어지면 두 단어가 아나그램인지 판별하는 프로그램을 작성하세 요. 아나그램 판별시 대소문자가 구분됩니다.

#### 풀이

```java
public static String solution16(String s1, String s2) {
    char[] a = s1.toCharArray();
    char[] b = s2.toCharArray();
    sort(a);
    sort(b);
    boolean result = IntStream.range(0, a.length).noneMatch(i -> {
        return a[i] != b[i];
    });
    return result ? "YES" : "NO";
}
```

### 선생님 퀴즈

#### 문제

현수네 반은 학생이 N명 있습니다. 수업도중 선생님이 잠깐 자리를 비워야 하는데 그 동안 학 생들이 떠들거나 놀지 않도록 각 학생들에게 퀴즈를 냈습니다.
 선생님은 각 학생들에게 숫자가 적힌 카드를 줬습니다. 각 학생들은 1부터 자기 카드에 적힌 숫자까지의 합을 구하는 퀴즈입니다.

선생님이 돌아와서 각 학생들의 답이 맞았는지 확인을 하려고 하는데 너무 힘들어서 여러분에 게 자동으로 채점을 하는 프로그램을 부탁했습니다. 여러분이 선생님을 도와주세요.

#### 풀이

```java
public static String solution17(List<Integer> q, List<Integer> answer) {
    return IntStream.range(0, q.size()).mapToObj(i -> {
        Integer currentQ = q.get(i);
        Integer currentAnswer = answer.get(i);
        if ((currentQ + 1) * currentQ / 2 == currentAnswer) {
            return "YES";
        }
        return "No";
    }).collect(Collectors.joining(" "));
}
```

### 층간소음

#### 문제

T편한 세상 아파트는 층간소음 발생 시 윗집의 발뺌을 방지하기 위해 애초 아파트를 지을 때 바닥에 진동센서를 설치했습니다. 이 센서는 각 세대의 층간 진동소음 측정치를 초단위로 아 파트 관리실에 실시간으로 전송합니다. 그리고 한 세대의 측정치가 M값을 넘으면 세대호수와 작은 경보음이 관리실 모니터에서 울립니다. 한 세대의 N초 동안의 실시간 측정치가 주어지면 최대 연속으로 경보음이 울린 시간을 구하세요. 경보음이 없으면 -1를 출력합니다.

#### 풀이

```java
public static String solution18(int bound, List<Integer> sounds) {
    var lambdaContext = new Object() {
        Integer startTime = -1;
        Integer max = -1;
    };
    IntStream.range(0, sounds.size()).forEach(i -> {
        if (sounds.get(i) <= bound) {
            if (lambdaContext.startTime != -1) {
                lambdaContext.max = max(i - lambdaContext.startTime, lambdaContext.max);
                lambdaContext.startTime = -1;
            }
            return;
        }
        if (lambdaContext.startTime == -1) {
            lambdaContext.startTime = i;
        }
    });
    return String.valueOf(lambdaContext.max);
}
```

### 분노 유발자

#### 문제

오늘은 수능이 끝난 다음날로 교장선생님은 1, 2학년 재학생들에게 강당에 모여 어벤져스 영 화를 보여준다고 하여 학생들이 강당에 모였습니다.
 강당의 좌석은 영화관처럼 계단형이 아니라 평평한 바닥에 의자만 배치하고 학생들이 앉습니 다. 그런데 만약 앞자리에 앉은 키가 큰 학생이 앉으면 그 학생보다 앉은키가 작은 뒷자리 학 생은 스크린이 보이지 않습니다. 한 줄에 앉은키 정보가 주어지면 뒷사람 모두의 시야를 가려 영화 시청이 불가능하게 하는 분노유발자가 그 줄에 몇 명이 있는지 구하는 프로그램을 작성 하세요.

#### 풀이

```java
public static String solution19(List<Integer> list) {
    Stack<Integer> stack = new Stack<>();
    list.forEach(num -> {
        while (!stack.isEmpty() && stack.peek() < num) {
            stack.pop();
        }
        stack.push(num);
    });
    return String.valueOf(stack.size() - 1);
}
```

### 가위 바위 보

#### 문제

A, B 두 사람이 가위바위보 게임을 합니다. 총 N번의 게임을 하여 A가 이기면 A를 출력하고, B가 이기면 B를 출력합니다. 비길 경우에는 D를 출력합니다.
 가위, 바위, 보의 정보는 1:가위, 2:바위, 3:보로 정하겠습니다.
 예를 들어 N=5이면

|   회수   |  1   |  2   |  3   |  4   |  5   |
| :------: | :--: | :--: | :--: | :--: | :--: |
| A의 정보 |  2   |  3   |  3   |  1   |  3   |
| B의 정보 |  1   |  1   |  2   |  2   |  3   |
|   승자   |  A   |  B   |  A   |  B   |  D   |

두 사람의 각 회의 가위, 바위, 보 정보가 주어지면 각 회를 누가 이겼는지 출력하는 프로그램 을 작성하세요.

#### 풀이

```java
public static String solution20(List<Integer> a, List<Integer> b) {
    return IntStream.range(0, a.size()).mapToObj(i -> {
        Integer currentA = a.get(i);
        Integer currentB = b.get(i);
        if (currentA.equals(currentB)) {
            return "D";
        } else if ((currentA + 1) % 3 == currentB) {
            return "B";
        } else {
            return "A";
        }
    }).collect(Collectors.joining(" "));
}
```

### 카드게임

#### 문제

0부터 9까지의 숫자가 표시된 카드를 가지고 두 사람 A와 B가 게임을 한다. A와 B에게는 각 각 0에서 9까지의 숫자가 하나씩 표시된 10장의 카드뭉치가 주어진다. 두 사람은 카드를 임의의  순서로 섞은 후 숫자가 보이지 않게 일렬로 늘어놓고 게임을 시작한다. 단, 게임 도중 카드의  순서를 바꿀 수는 없다.

A와 B 각각이 늘어놓은 카드를 뒤집어서 표시된 숫자를 확인하는 것을 한 라운드라고 한 다. 게임은 첫 번째 놓인 카드부터 시작하여 순서대로 10번의 라운드로 진행된다. 각 라운드에 서는 공개된 숫자가 더 큰 사람이 승자가 된다. 승자에게는 승점 3점이 주어지고 패자에게는 승점이 주어지지 않는다. 만약 공개된 두 숫자가 같아서 비기게 되면, A, B 모두에게 승점 1 점이 주어진다.

10번의 라운드가 모두 진행된 후, 총 승점이 큰 사람이 게임의 승자가 된다. 만약, A와 B 의 총 승점이 같은 경우에는, 제일 마지막에 이긴 사람을 게임의 승자로 정한다. 그래도 승부 가 나지 않는 경우는 모든 라운드에서 비기는 경우뿐이고 이 경우에 두 사람은 비겼다고 한 다.

예를 들어, 다음 표에서 3번째 줄은 각 라운드의 승자를 표시하고 있다. 표에서 D는 무승 부를 나타낸다. 이 경우에 A의 총 승점은 16점이고, B는 13점이어서, A가 게임의 승자가 된 다.

| 라운드 | 1    | 2    | 3    | 4    | 5    | 6    | 7    | 8    | 9    | 10   |
| ------ | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- |
| A      | 4    | 5    | 6    | 7    | 0    | 1    | 2    | 3    | 9    | 8    |
| B      | 1    | 2    | 3    | 4    | 5    | 6    | 7    | 8    | 9    | 0    |
| 승     | A    | A    | A    | A    | B    | B    | B    | B    | D    | A    |

A와 B가 늘어놓은 카드의 숫자가 순서대로 주어질 때, 게임의 승자가 A인지 B인지, 또는 비겼 는지 결정하는 프로그램을 작성하시오.

#### 풀이

```java
//자바 stream
public static String solution21(List<Integer> a, List<Integer> b) {
    var streamValue = new Object() {
        int scoreA;
        int scoreB;
        Boolean lastWinner = null;
    };
    IntStream.range(0, a.size()).forEach(i -> {
        int currentA = a.get(i);
        int currentB = b.get(i);
        if (currentA == currentB) {
            streamValue.scoreA++;
            streamValue.scoreB++;
        } else if (currentA > currentB) {
            streamValue.lastWinner = true;
            streamValue.scoreA += 3;
        } else {
            streamValue.lastWinner = false;
            streamValue.scoreB += 3;
        }
    });
    String winner;
    if (streamValue.scoreA > streamValue.scoreB) {
        winner = "A";
    } else if (streamValue.scoreA < streamValue.scoreB) {
        winner = "B";
    } else {
        if (streamValue.lastWinner == null) {
            winner = "D";
        } else {
            winner = streamValue.lastWinner ? "A" : "B";
        }
    }
    return streamValue.scoreA + " " + streamValue.scoreB + "\n" + winner;
}
```

```kotlin
fun solution21(a: IntArray, b: IntArray): String {
        val (scoreA, scoreB, lastWinner) =  (a zip b).fold(Triple(0, 0,  "D")) { (scoreA, scoreB, lastWinner), (currentA, currentB) ->
            val compare = currentA.compareTo(currentB)
            when {
                compare == 0 -> {
                    Triple(scoreA + 1, scoreB + 1, lastWinner)
                }
                compare > 0 -> {
                    Triple(scoreA + 3, scoreB, "A")
                }
                else -> {
                    Triple(scoreA, scoreB + 3, "B")
                }
            }
        }
        val compare = scoreA.compareTo(scoreB)
        val winner = when {
            compare.equals(0) -> lastWinner
            compare > 0 -> "A"
            else -> "B"
        }
        return """
            $scoreA $scoreB
            $winner
        """.trimIndent()
    }
}
```

### 온도의 최대값

#### 문제

매일 아침 9시에 학교에서 측정한 온도가 어떤 정수의 수열로 주어졌을 때, 연속적인 며칠 동안의 온도의 합이 가장 큰 값을 알아보고자 한다. 매일 측정한 온도가 정수의 수열로 주어졌을 때, 연속적인 며칠 동안의 온도의 합이 가장 큰 값을 계산하는 프로그램을 작성하시오.

#### 풀이

```java
public static String solution22(List<Integer> tem, int numOfDay) {
    var streamValue = new Object() {
        int result = Integer.MIN_VALUE;
    };
    streamValue.result = IntStream.range(0, numOfDay).map(tem::get).sum();
    int lastSum = IntStream.range(numOfDay, tem.size()).reduce(streamValue.result, (result, i) -> {
        int index = i - numOfDay;
        int currentSum = result - tem.get(i - numOfDay) + tem.get(i);
        streamValue.result = max(currentSum, streamValue.result);
        return currentSum;
    });
    return String.valueOf(max(streamValue.result, lastSum));
}
```

```kotlin
fun solution22(tem: IntArray, sumOfDay: Int): String {
    var max = (0 until sumOfDay).map { tem[it] }.sum()
    val lastSum = (sumOfDay until tem.size).fold(max) { result, i ->
        val nextSum = result + tem[i] - tem[i - sumOfDay]
        max = max(nextSum, max)
        nextSum
    }
    return max(max, lastSum).toString()
}
```

### 연속 부분 증가 수열

#### 문제

N개의 숫자가 나열된 수열이 주어집니다. 이 수열 중 연속적으로 증가하는 부분 수열을 최대 길이를 구하여 출력하는 프로그램을 작성하세요.

 만약 N=9이고 5 7 3 3 12 12 13 10 11 이면 “3 3 12 12 13”부분이 최대 길이 증가수열이므로 그 길이인 5을 출력합니다. 값이 같을 때는 증가하는 걸로 생각합니다.

#### 풀이

```java
public static String solution23(List<Integer> numbers) {
    var streamValue = new Object(){
        int maxCount = 0;
        int currentCount = 1;
    };
    numbers.stream().reduce((preNum, number) -> {
        if (number >= preNum) {
            streamValue.currentCount++;
        } else {
            streamValue.maxCount = max(streamValue.currentCount, streamValue.maxCount);
            streamValue.currentCount = 1;
        }
        return number;
    });
    return String.valueOf(max(streamValue.currentCount, streamValue.maxCount));
}
```

### Jolly Jumpers

#### 문제

N개의 정수로 이루어진 수열에 대해 서로 인접해 있는 두 수의 차가 1에서 N-1까지의 값을 모두 가지면 그 수열을 유쾌한 점퍼(jolly jumper)라고 부른다. 예를 들어 다음과 같은 수열에 서 1 4 2 3 앞 뒤에 있는 숫자 차의 절대 값이 각각 3 ,2, 1이므로 이 수열은 유쾌한 점퍼가 된다. 어떤 수열이 유쾌한 점퍼인지 판단할 수 있는 프로그램을 작성하라.

#### 풀이

```java
public static String solution24(List<Integer> numbers) {
    int len = numbers.size();
    return IntStream.range(1, len).noneMatch(i -> {
        int temp = numbers.get(i) - numbers.get(i-1);
        return temp > len || temp < -len;
    }) ? "YES" : "NO";
}
```

### 석차 구하기

#### 문제

N명의 학생의 수학점수가 입력되면 각 학생의 석차를 입력된 순서대로 출력하는 프로그램을 작성하세요. 같은 점수가 입력될 경우 높은 석차로 동일 처리한다. 즉 가장 높은 점수가 92점인데 92 점이 3명 존재하면 1등이 3명이고 그 다음 학생은 4등이 된다. 점수는 100점 만점이다.

#### 풀이

```java
public static String solution25(List<Integer> scores) {
    Map<Integer, Integer> map = new HashMap<>();
    List<Integer> sorted = scores.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    map.put(sorted.get(0), 1);
    IntStream.range(1, sorted.size()).forEach(i -> {
        if (sorted.get(i - 1).equals(sorted.get(i))) {
            return;
        }
        map.put(sorted.get(i), i+1);
    });
    return scores.stream().map(score -> String.valueOf(map.get(score))).collect(Collectors.joining(" "));
}
```

### 마라톤

#### 문제

KSEA 장거리 달리기 대회가 진행되어 모든 선수가 반환점을 넘었다. 각 선수의 입장에서 자 기보다 앞에 달리고 있는 선수들 중 평소 실력이 자기보다 좋은 선수를 남은 거리 동안 앞지 르는 것은 불가능하다. 반대로, 평소 실력이 자기보다 좋지 않은 선수가 앞에 달리고 있으면 남은 거리 동안 앞지르는 것이 가능하다. 이러한 가정 하에서 각 선수는 자신이 앞으로 얻을 수 있는 최선의 등수를 알 수 있다.

각 선수의 평소 실력은 정수로 주어지는데 더 큰 값이 더 좋은 실력을 의미한다. 현재 달리고 있는 선수를 앞에서 부터 표시했을 때 평소 실력이 각각 2, 8, 10, 7, 1, 9, 4, 15라고 하면 각 선수가 얻을 수 있는 최선의 등수는 (같은 순서로) 각각 1, 1, 1, 3, 5, 2, 5, 1이 된다. 예 를 들어, 4번째로 달리고 있는 평소 실력이 7인 선수는 그 앞에서 달리고 있는 선수들 중 평 소 실력이 2인 선수만 앞지르는 것이 가능하고 평소실력이 8과 10인 선수들은 앞지르는 것이 불가능하므로, 최선의 등수는 3등이 된다.

선수들의 평소 실력을 현재 달리고 있는 순서대로 입력 받아서 각 선수의 최선의 등수를 계산 하는 프로그램을 작성하시오.

#### 풀이

```java
public static String solution26(List<Integer> runners) {
    return IntStream.range(0, runners.size()).mapToObj(i -> {
        long count = runners.stream().limit(i).filter(runner -> runner >= runners.get(i)).count();
        return String.valueOf(count + 1);
    }).collect(Collectors.joining(" "));
}
```

### N!의 표현법

#### 문재

임의의 N에 대하여 N!은 1부터 N까지의 곱을 의미한다. 이는 N이 커짐에 따라 급격하게 커진 다. 이러한 큰 수를 표현하는 방법으로 소수들의 곱으로 표현하는 방법이 있다. 먼저 소수는 2, 3, 5, 7, 11, 13... 순으로 증가함을 알아야 한다. 예를 들면 825는 (0 1 2 0 1)로 표현이 가능한데, 이는 2는 없고 3은 1번, 5는 2번, 7은 없고, 11은 1번의 곱이라는 의미이다. 101보 다 작은 임의의 N에 대하여 N 팩토리얼을 이와 같은 표기법으로 변환하는 프로그램을 작성해 보자. 출력은 아래 예제와 같이 하도록 한다.

#### 풀이

```java
public static String solution27(int n) {
    int [] table = new int[n+1];
    IntStream.rangeClosed(2,n).forEach(i -> {
        int current = i;
        for (int j = 2; j <= i; j++) {
            if (current == 1) {
                break;
            }
            while (current % j == 0) {
                current /= j;
                table[j]++;
            }
        }
    });
    return IntStream.rangeClosed(0,n).filter(i -> table[i] != 0).mapToObj(i -> String.valueOf(table[i])).collect(Collectors.joining(" "));
}
```

### N!에서 0의 개수

#### 문제

자연수 N이 입력되면 N! 값에서 일의 자리부터 연속적으로 ‘0’이 몇 개 있는지 구하는 프로그 램을 작성하세요. 만약 5! = 5×4×3×2×1 = 120으로 일의자리부터 연속적된 ‘0’의 개수는 1입니다. 만약 12! = 479001600으로 일의자리부터 연속적된 ‘0’의 개수는 2입니다.

#### 풀이

```java
    public static String solution28(int n) {
        var lambdaContext = new Object() {
            int numberOf2 = 0;
            int numberOf5 = 0;
        };
        IntStream.rangeClosed(2, n).forEach(i -> {
            int current = i;
            for (int j = 2, range = max(5, i); j <= range; j++) {
                if (current == 1) {
                    break;
                }
                while (current % j == 0) {
                    current /= j;
                    if (2 == j) {
                        lambdaContext.numberOf2++;
                    }else if (5 == j) {
                        lambdaContext.numberOf5++;
                    }
                }
            }

        });
        return String.valueOf(min(lambdaContext.numberOf2, lambdaContext.numberOf5));
    }
```

### 3의 개수는 small

#### 문제

자연수 N이 입력되면 1부터 N까지의 자연수를 종이에 적을 때 각 숫자 중 3의 개수가 몇 개 있는지 구하려고 합니다.예를 들어 1부터 15까지는 1, 2, **3**, 4, 5, 6, 7, 8, 9, 1, 0, 1, 1, 1, 2, 1, **3**, 1, 4, 1, 5으로 3의 개수는 2개입니다.

자연수 N이 입력되면 1부터 N까지 숫자를 적을 때, 3의 개수가 몇 개인지 구하여 출력하는 프로그램을 작성하세요.

#### 풀이

```java
public static String solution29(int n) {
    int answer = IntStream.rangeClosed(1, n).reduce(0, (result, currentNumber) -> {
        int count = IntStream.iterate(currentNumber, j -> j > 0, j -> j / 10).reduce(0, (tempResult, j) -> {
            int num = j % 10;
            if (num == 3) {
                return tempResult + 1;
            }
            return tempResult;
        });
        return result + count;
    });
    return String.valueOf(answer);
}
```

### 3의 개수는 large

#### 문제

자연수 N이 입력되면 1부터 N까지의 자연수를 종이에 적을 때 각 숫자 중 3의 개수가 몇 개 있는지 구하려고 합니다.예를 들어 1부터 15까지는 1, 2, **3**, 4, 5, 6, 7, 8, 9, 1, 0, 1, 1, 1, 2, 1, **3**, 1, 4, 1, 5으로 3의 개수는 2개입니다.

자연수 N이 입력되면 1부터 N까지 숫자를 적을 때, 3의 개수가 몇 개인지 구하여 출력하는 프로그램을 작성하세요.

#### 풀이

```java
public static String solution30(int n) {
    int answer = 0;
    for (int k = 1, left = 1, right = 0, current = 0; left != 0; k *= 10) {
        left = n/(k*10);
        right = n % k;
        current = (n / k) % 10;
        if (current > 3) {
            answer += (left + 1) * k;
        } else if (current == 3) {
            answer += left*k+right+1;
        } else {
            answer +=left*k;
        }
    }
    return String.valueOf(answer);
}
```

### 탄화수소 질량

#### 문제

탄소(C)와 수소(H)로만 이루어진 화합물을 탄화수소라고 합니다.

탄소(C) 한 개의 질량은 12g, 수소(H) 한 개의 질량은 1g입니다. 에틸렌(C2H4)의 질량은 12*2+1*4=28g입니다.메탄(CH4)의 질량은 12*1+1*4=16g입니다.

 탄화수소식이 주어지면 해당 화합물의 질량을 구하는 프로그램을 작성하세요.

#### 풀이

```java
public static String solution31(String s) {
    final int cWeight = 12;
    final int hWeight = 1;
    int cCount = 0;
    int hCount = 0;
    if (s.charAt(1) == 'H') {
        cCount = 1;
        hCount = Character.getNumericValue(s.charAt(2));
    } else {
        cCount = Character.getNumericValue(s.charAt(1));
        hCount = Character.getNumericValue(s.charAt(3));
    }
    int result = cWeight*cCount + hWeight*hCount;
    return String.valueOf(result);
}
```

