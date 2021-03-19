## Wrapper Classes

> ##### 참고 자료
>
> [java.lang 레퍼런스](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/package-summary.html)
>
> [Autoboxing Unboxing 레퍼런스](https://docs.oracle.com/javase/tutorial/java/data/autoboxing.html)

### 목차

1. [Autoboxing과 Unboxing](#autoboxing과-unboxing)
2. [Wrapper Class와 `==`](#wrapper-class와-==)
3. [Wrapper Class와 `equals`](#wrapper-class와-equals)

기본 타입의 변수를 객체로 다루어야 하는 경우 `Boolean`, `Byte`, `Character`, `Integer`, `Long`, `Float`, `Double` 같은 Wrapper Class를 사용한다. 각각의 Wrapper Class의 객체에는 해당 기본타입의 필드가 있고 그 객체를 참조하는 레퍼런스 타입으로 나타낸다. Wrapper Class는 기본 타입간 변환을 위한 여러 메소드를 제공하며  `equals`, `hashCode` 같은 표준 메소드를 제공한다.

### Autoboxing과 Unboxing

Autoboxing은 Java 컴파일러가 기본 타입과 해당 기본 타입과 일치하는 wrapper 클래스간에 수행되는 자동 변환이다. (예를 들어 int -> Integer)  변환이 반대로 진행되는 경우 Unboxing이라고 한다.

#### Autoboxing

```java
Character ch = 'a';
```

위와 같은 코드가 입력된 경우 컴파일 시 아래와 같은 코드로 변환 되어 실행하는데 이를 Autoboxing이라고 한다.

```java
Character ch = Character.valueOf('a')
```

#### Unboxing

객체는 `%` 와 `+=` 연산을 수행할 수 없는데 아래와 같은 코드를 실행해도 에러가 발생하지 않는다.

```java
public static int sumEven(List<Integer> li) {
    int sum = 0;
    for (Integer i: li)
        if (i % 2 == 0)
            sum += i;
        return sum;
}
```

컴파일 시 아래와 같은 코드로 변환되어 실행하고 이를 Unboxing이라고 한다.

```java
public static int sumEven(List<Integer> li) {
    int sum = 0;
    for (Integer i : li)
        if (i.intValue() % 2 == 0)
            sum += i.intValue();
        return sum;
}
```

### Wrapper Class와 `==`

####  Autoboxing을 이용하여 할당한경우

위에서 살펴본것 처럼 Autoboxing을 이용한 경우 각각의 wrapper class의 `valueof` 메소드로 변환이 된다.

```java
    public static Integer valueOf(int i) {
        if (i >= IntegerCache.low && i <= IntegerCache.high)
            return IntegerCache.cache[i + (-IntegerCache.low)];
        return new Integer(i);
    }
```

 `Integer`의 `valueOf` 메소드의 코드를 보면 매번  Integer 객체를 생성하는 것이 아닌 특정 범위에 있는 값의 경우 `IntegerCache` 클래스의 객체의 캐쉬된 값을 사용하는 것을 확인할 수 있고, 이 캐쉬된 값의 범위는 -128 ~ 127이다. 따라서 해당 범위의 값을 사용하여 `==` 연산을 사용하면 비교하는 값이 같은 경우 `true` 가 된다.

```java
public static Double valueOf(double d) {
    return new Double(d);
}
```

하지만 부동 소수점의 `==` 비교인 경우 매번 생성자를 이용해 새로운 객체를 생성하므로 같은 값이라도 새로 할당한경우 `false`가 된다.

#### 생성자를 이용해 할당한 경우

생성자를 이용해 서로 같은 값을 할당한 경우 새로운 객체를 생성하게 되므로 비교하고자 하는 값이 같은 경우에도 `==` 를 수행하면 `false`가 나오는 것을 확인할 수 있다.

```java
Integer integer = new Integer(1);
Integer integer1 = new Integer(1);
```

### Wrapper class와 `equals`

```java
public boolean equals(Object obj) {
    if (obj instanceof Integer) {
        return value == ((Integer)obj).intValue();
    }
    return false;
}
```

`equals` 의 경우 해당 wapper class의 기본 값의 `==`  연산을 수행한다.

