## Classes

모든 타입은 참조형 또는 기본형이다. 클래스, 열거형, 배열(모두 `java.lang.Object`에서 상속 받음) 뿐아니라 인터페이스 또한 참조형이다.

모든 타입의 객체에 대해, Java Virtual Machine은 멤버변수와 타입정보를 포함하는 객체의 런타임 속성을 살펴볼수 있는 메소드를 제공하는 `java.lang.Class`  불변 인스턴스를 생성한다. `Class` 는 또한 새로운 클래스와 객체를 만드는 기능을 제공한다. 가장 중요한 것은, Reflection API의 진입점이다.

### 클래스 객체 찾기

모든 Reflection 작업의 시작은 `java.lang.Class`이다. `java.lang.reflect.ReflectPermission`을 제외하고 모든 `java.lang.reflect` 패키지의 클래스들은 public 생성자를 가지는데, 이러한 클래스들에 접근하려면, `Class`의 적절한 메소드를 호출해야한다. 코드에 객체, 클래스 이름, 타입 또는 기존 클래스에 대한 액세스 권한이 있는지 여부에 따라 클래스를 가져오는 여러가지 방법이 있다.

#### `Object.getClass()`

객체의 인스턴스를 사용하고 있는 경우, 객체의 `Class`를 가지고 올수 있는 가장 간단한 방법은 `Object.getClass()`  메소드를 호출하는 것이다. 물론, `Object`를 상속하는 참조형 타입에서만 가능하다.

```java
Class c = "foo".getClass();
```

`String`의 `Class`를 얻을 수 있다.

```java
Class c = System.console().getClass();
```

`static` 메서드 `System.concsole()` 에 의해 반환되는 가상 머신과 관련된 고유 한 콘솔이 있다. `getClass()`에 의해 반환되는 `Class`는 `java.io.Console`과 일치한다.

```java
enum E { A, B }
Class c = A.getClass();
```

A는 E의 인스턴스이다. 따라서, `getClass()`를 호출하면 열거형 타입 E와 관련한 `Class` 가 리턴된다.

```java
byte[] bytes = new byte[1024];
Class c = bytes.getClass();
```

배열을 `Object`이므로, 배열의 인스턴스에서  `getClass()` 를 호출할 수 있다.

```java
import java.util.HashSet;
import java.util.Set;

Set<String> s = new HashSet<String>();
Class c = s.getClass();
```

이 경우, `java.util.Set`은 `java.util.HashSet`의 인터페이스이다. `getClass()`에 의해 반환되는 `Class`는 `java.util.HashSet`과 일치한다.

#### `.class` 구문

타입을 사용할 수 있지만 인스턴스가 없는 경우, 타입의 이름에 `.class`를 추가해 `Class`를 얻을 수 있다. 이것은 기본형에 대한 `Class`를 얻는 가장 쉬운 방법이다.

```java
boolean b;
Class c = b.getClass();   // compile-time error

Class c = boolean.class;  // correct
```

`boolean`은 기본형 타입이고 역 참조 할수 없기 때문에 `b.getClass()`는 컴파일 에러가 발생한다. 

```java
Class c = java.io.PrintStream.class;
```

```java
Class c = int[][][].class;
```

#### `Class.forName()`

정규화된 이름을 사용할 수 있는 경우, `Class.forName()`을 사용하여 일치하는 `Class`를 가져올수 있다. 하지만, 기본형에는 사용할 수 없다. 

```java
Class c = Class.forName("com.duke.MyLocaleServiceProvider");
```

```java
Class cDoubleArray = Class.forName("[D"); //double[].class

Class cStringArray = Class.forName("[[Ljava.lang.String;"); //String[][].class
```

#### TYPE Field for Primitive Type Wrappers

기본형과 void형은 `java.lang`에 있는 Wrapper 클래스의 `TYPE`필드에서 해당하는 `Class`를 구할 수 있다.

```java
Class c = Double.TYPE;
```

```java
Class c = Void.TYPE;
```

#### `Class`를 리턴하는 메소드

##### `Class.getSuperclass()`

주어진 클래스의 슈퍼클래스를 반환한다.

```java
Class c = javax.swing.JButton.class.getSuperclass();
```

##### `Class.getClasses()`

상속된 멤버를 포함하여, 모든 클래스, 인터페이스, 열거형을 반환환다.

```java
Class<?>[] c = Character.class.getClasses();
```

### Class Modifier와 Type 살펴보기

클래스는 여러개의 Modifier와 같이 선언될수 있다.

- Access modifiers: `public`, `protected`, `private`
- Modifier requiring override: `abstract`
- Modifier restricting to one instance: `static`
- Modifier prohibiting value modification: `final`
- Modifier forcing strict floating point behavior: `strictfp`
- Annotations

`java.lang.reflect.Modifier`  클래스에는 가능한 모든 Modifier가 선언되어있다. 또한 `Class.getModifiers()` 메소드에서 반환된 값을 디코딩할 수 있는 여러 메서드들도 포함하고 있다.

`ClassDeclarationSpy`예제에서는 Modifier, 제네릭 타입 매개 변수, 구현 된 인터페이스와 상속경로 등의 클래스의 선언 구성 요소를 얻는 방법을 보여준다. `Class`는 [`java.lang.reflect.AnnotatedElement`](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/AnnotatedElement.html)도 구현하기 때문에 런타임 Annotation도 조회할 수 있다.

```java
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import static java.lang.System.out;

public class ClassDeclarationSpy {
    public static void main(String... args) {
        try {
            Class<?> c = Class.forName(args[0]);
            out.format("Class:%n  %s%n%n", c.getCanonicalName());
            out.format("Modifiers:%n  %s%n%n",
                    Modifier.toString(c.getModifiers()));

            out.format("Type Parameters:%n");
            TypeVariable[] tv = c.getTypeParameters();
            if (tv.length != 0) {
                out.format("  ");
                for (TypeVariable t : tv)
                    out.format("%s ", t.getName());
                out.format("%n%n");
            } else {
                out.format("  -- No Type Parameters --%n%n");
            }

            out.format("Implemented Interfaces:%n");
            Type[] intfs = c.getGenericInterfaces();
            if (intfs.length != 0) {
                for (Type intf : intfs)
                    out.format("  %s%n", intf.toString());
                out.format("%n");
            } else {
                out.format("  -- No Implemented Interfaces --%n%n");
            }

            out.format("Inheritance Path:%n");
            List<Class> l = new ArrayList<Class>();
            printAncestor(c, l);
            if (l.size() != 0) {
                for (Class<?> cl : l)
                    out.format("  %s%n", cl.getCanonicalName());
                out.format("%n");
            } else {
                out.format("  -- No Super Classes --%n%n");
            }

            out.format("Annotations:%n");
            Annotation[] ann = c.getAnnotations();
            if (ann.length != 0) {
                for (Annotation a : ann)
                    out.format("  %s%n", a.toString());
                out.format("%n");
            } else {
                out.format("  -- No Annotations --%n%n");
            }

            // production code should handle this exception more gracefully
        } catch (ClassNotFoundException x) {
            x.printStackTrace();
        }
    }

    private static void printAncestor(Class<?> c, List<Class> l) {
        Class<?> ancestor = c.getSuperclass();
        if (ancestor != null) {
            l.add(ancestor);
            printAncestor(ancestor, l);
        }
    }
}
```

```shell
$ java ClassDeclarationSpy java.util.concurrent.ConcurrentNavigableMap
Class:
  java.util.concurrent.ConcurrentNavigableMap

Modifiers:
  public abstract interface

Type Parameters:
  K V

Implemented Interfaces:
  java.util.concurrent.ConcurrentMap<K, V>
  java.util.NavigableMap<K, V>

Inheritance Path:
  -- No Super Classes --

Annotations:
  -- No Annotations --
```

실제 `ConcurrentNavigableMap<K,V>`의 선언은 다음과 같다.

```java
public interface ConcurrentNavigableMap<K,V>
    extends ConcurrentMap<K,V>, NavigableMap<K,V>
```

