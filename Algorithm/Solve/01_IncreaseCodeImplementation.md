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
9. [모두의 약수](#모두의 약수)
10. [자리수의 합](#자리수의-합)
11. [숫자의 총 개수(small)](#숫자의-총-개수(small))
12. [숫자의 총 개수(large)](#숫자의-총-개수(large))
13. [가장 많이 사용된 자릿수](#가장-많이-사용된-자릿수)
14. [뒤집은 소수](#뒤집은-소수)
15. [소수의 개수](#소수의-개수)

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

### 숫자의 총 개수(small)

#### 문제

자연수 N이 입력되면 1부터 N까지의 자연수를 종이에 적을 때 각 숫자는 몇 개 쓰였을까요? 예를 들어 1부터 15까지는 1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 0, 1, 1, 1, 2, 1, 3, 1, 4, 1, 5으로 총 21개가 쓰였음을 알 수 있습니다.
 자연수 N이 입력되면 1부터 N까지 각 숫자는 몇 개가 사용되었는지를 구하는 프로그램을 작 성하세요.

#### 풀이

```
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

### 숫자의 총 개수(large)

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