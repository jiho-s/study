## Static

### static Import

`static` 으로 선언된 메소드와 프로퍼티를 간단한 이름으로 접근할 수 있게 해준다.

```java
import static TypeName . Identifier ;
```

### static Fields

필드가 `static`으로 선언하면, 인스턴스의 생성 갯수와 상관 없이 하나의 공간만 존재한다. 클래스를 생성하지 않고도 사용할 수 있다.

```java
class Point {
    int x, y, useCount;
    Point(int x, int y) { this.x = x; this.y = y; }
    static final Point origin = new Point(0, 0);
}
```

### static Methods

클래스 메소드라고도 부르며, 특정 인스턴스에 대한 참조없이 호출된다. `static`  메소드로 `static` 이 아닌 필드나 메소드에 접근시 컴파일 오류가 발생한다.

### static Initalizers

클래스 초기화시 한번 실행된다.

```java
class Solution {
    static {
        System.out.println("init");
    }
}
```

