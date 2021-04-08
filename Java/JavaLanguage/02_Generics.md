## Generics

> #### 참고자료
>
> [제네릭](https://docs.oracle.com/javase/tutorial/java/generics/index.html)

### Generics

제네릭은 클래스, 인터페이스 및 메서드를 정의 할 때 타입이 매개 변수가되도록한다.  타입 매개 변수는 다른 입력 값으로 동일한 코드를 재사용 할 수있는 방법을 제공한다.

제네릭을 사용하는 코드에는 다음과 같은 장점이 있다.

- 컴파일 타임에 더 강력한 타입 검사

  Java 컴파일러는 강력한 타입 검사를 제네릭 코드에 적용하고 코드가 type safety를 위반하면 오류를 발생시킨다. 컴파일 타임 오류를 수정하는 것은 런타임 오류를 수정하는 것보다 쉽다.

- 타입 캐스팅 없이 사용가능

  ```java
  List list = new ArrayList();
  list.add("hello");
  String s = (String) list.get(0);
  ```

  제네릭을 사용하면 타입캐스팅이 필요 없다

  ```java
  List<String> list = new ArrayList<String>();
  list.add("hello");
  String s = list.get(0);   // no cast
  ```

- 다양한 유형의 컬렉션에서 작동하고 커스텀 할 수 있으며 타입이 안전하고 읽기 쉬운 알고리즘을 구현할 수 있다.

### RawType

타입 없이 사용하면 raw type이다.

```java
public class Box<T> {
    public void set(T t) { /* ... */ }
    // ...
}
```

```java
Box rawBox = new Box();
```

### Bounded Type Parameters

매개 변수가있는 형식에서 형식 인수로 사용할 수있는 형식을 제한하려는 경우 사용한다. `<T extends U>` 형태로 나타내며 T는 U를 상속 또는 구현 한 클래스 또는 인터페이스여야 한다.

```java
public class Box<T> {

    private T t;          

    public void set(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }

    public <U extends Number> void inspect(U u){
        System.out.println("T: " + t.getClass().getName());
        System.out.println("U: " + u.getClass().getName());
    }

    public static void main(String[] args) {
        Box<Integer> integerBox = new Box<Integer>();
        integerBox.set(new Integer(10));
        integerBox.inspect("some text"); // error: this is still String!
    }
}
```

#### Multiple Bounds

`&` 를 사용하여 여러 클래스 또는 인터페이스를 상속한 클래스 또는 인터페이스를 지정할 수 있다.

`<T extends B1 & B2 & B3>`

### 와일드 카드

`?`를 사용하여 제네릭을 표현할 수 있다.

#### Upper Bounded Wildcards

`<? extends Foo>`와 같은 형태, 인자를 받아올때 사용

#### Unbounded Wildcards

`<?>`와 같은 형태

#### Lower Bounded Wildcards

`<? super Integer>`과 같은 형태, 인자를 내보낼 때 사용

### Type Erasure

제네릭을 구현하기 위해 자바 컴파일러는 다음의 경우에 type erasure을 적용한다.

- 제네릭 타입의 모든 타입 매개 변수를 해당 범위 또는 타입 매개 변수가 제한되지 않은 경우 `Object로` 바꾼다 . 따라서 생성 된 바이트 코드에는 일반 클래스, 인터페이스 및 메서드 만 포함된다.
- type safety를 유지하기 위해 필요한 경우 타입 캐스팅을 삽입한다.
- 확장 된 제네릭 유형에서 다형성을 유지하는 브리지 메서드를 생성합니다.

Type Erasure는 매개 변수화 된 타입에 대해 새 클래스가 생성되지 않도록한다. 결과적으로 제네릭은 런타임 오버 헤드를 발생시키지 않는다.

#### 제네릭 타입 erasure

Java 컴파일러는 모든 타입 매개 변수를 지우고 타입 매개 변수가 bound 된 경우 각 매개 변수를 첫 번째 bound로 대체하고 타입 매개 변수가 bound 되지 않은 경우 `Object로` 대체합니다

```java
public class Node<T> {

    private T data;
    private Node<T> next;

    public Node(T data, Node<T> next) {
        this.data = data;
        this.next = next;
    }

    public T getData() { return data; }
    // ...
}
```

```java
public class Node {

    private Object data;
    private Node next;

    public Node(Object data, Node next) {
        this.data = data;
        this.next = next;
    }

    public Object getData() { return data; }
    // ...
}
```

bound가 있는경우

```java
public class Node<T extends Comparable<T>> {

    private T data;
    private Node<T> next;

    public Node(T data, Node<T> next) {
        this.data = data;
        this.next = next;
    }

    public T getData() { return data; }
    // ...
}
```

```java
public class Node {

    private Comparable data;
    private Node next;

    public Node(Comparable data, Node next) {
        this.data = data;
        this.next = next;
    }

    public Comparable getData() { return data; }
    // ...
}
```

#### 제네릭 메소드 erasure

Java 컴파일러는 제네릭 메소드 인수의 타입 매개 변수도 지운다.

```java
public static <T> int count(T[] anArray, T elem) {
    int cnt = 0;
    for (T e : anArray)
        if (e.equals(elem))
            ++cnt;
        return cnt;
}
```

```java
public static int count(Object[] anArray, Object elem) {
    int cnt = 0;
    for (Object e : anArray)
        if (e.equals(elem))
            ++cnt;
        return cnt;
}
```

bound가 있는 경우

```java
class Shape { /* ... */ }
class Circle extends Shape { /* ... */ }
class Rectangle extends Shape { /* ... */ }
```

```java
public static <T extends Shape> void draw(T shape) { /* ... */ }
```

```java
public static void draw(Shape shape) { /* ... */ }
```

#### Type Erasure의 사이드 이펙트와 Bridge Methods

유형 삭제로 인해 예상치 못한 상황이 발생할 수 있다. 다음 예는 이것이 어떻게 발생하는지 보여준다. 

```java
public class Node<T> {

    public T data;

    public Node(T data) { this.data = data; }

    public void setData(T data) {
        System.out.println("Node.setData");
        this.data = data;
    }
}
```

```java
public class MyNode extends Node<Integer> {
    public MyNode(Integer data) { super(data); }

    public void setData(Integer data) {
        System.out.println("MyNode.setData");
        super.setData(data);
    }
}
```

다음 코드가 있다.

```java
MyNode mn = new MyNode(5);
Node n = mn;            // A raw type - compiler throws an unchecked warning
n.setData("Hello");     
Integer x = mn.data;   
```

type erasure을 적용하면 다음과 같이된다.

```java
MyNode mn = new MyNode(5);
Node n = (MyNode)mn;         // A raw type - compiler throws an unchecked warning
n.setData("Hello");
Integer x = (String)mn.data;
```

##### Bridge Methods

```java
public class Node {

    public Object data;

    public Node(Object data) { this.data = data; }

    public void setData(Object data) {
        System.out.println("Node.setData");
        this.data = data;
    }
}
```

```java
public class MyNode extends Node {

    public MyNode(Integer data) { super(data); }

    public void setData(Integer data) {
        System.out.println("MyNode.setData");
        super.setData(data);
    }
}
```

type erasure후 메소드 구조가 일치하지 않으며 `MyNode`의 `setData()`는 Node의 `setData()`를 오버라이딩 하지 않는다.

이 문제를 해결하기 위해 Java 컴파일러는 하위 유형이 예상대로 작동하는지 확인하는 브릿지 메소드를 생성한다.

```java
class MyNode extends Node {

    // Bridge method generated by the compiler
    //
    public void setData(Object data) {
        setData((Integer) data);
    }

    public void setData(Integer data) {
        System.out.println("MyNode.setData");
        super.setData(data);
    }

    // ...
}
```

