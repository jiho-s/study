## 코드 구현력 기르기

### 목차

1. [1부터 N까지 M배수 합](#1부터-n까지-m배수-합)
2. [자연수의 합](#자연수의-합)
3. 
4. 
5. 
6. 
7. 
8. [올바른 괄호](#올바른-괄호)
9. [모두의 약수](#모두의 약수)
10. [자리수의 합](#자리수의-합)

### 1부터 N까지 M배수 합

### 자연수의 합

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

