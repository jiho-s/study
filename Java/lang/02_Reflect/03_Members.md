## Members

Reflection에는 `java.reflect.Member` 인터페이스가 선언되어있고, 이는  [`java.lang.reflect.Field`](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Field.html), [`java.lang.reflect.Method`](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Method.html), [`java.lang.reflect.Constructor`](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Constructor.html) 에 의해 구현된다. 

### `Field`

[`java.lang.reflect.Field`](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Field.html) 클래스의 메소드는 이름, 타입, modifier과 annotation과 같은 필드에 대한 정보를 검색 할 수 있다. 또한, 필드 값의 동적 접근 및 수정을 가능하게하는 메소드도 있다.

#### 필드 타입 얻기

`FieldSpy`예제는 주어진 binary class의 이름과 필드 네임을 사용하여 필드의 타입과 제네릭 타입을 출력한다.

```java
public class FieldSpy<T> {
    public boolean[][] b = {{ false, false }, { true, true } };
    public String name  = "Alice";
    public List<Integer> list;
    public T val;

    public static void main(String... args) {
        try {
            Class<?> c = Class.forName(args[0]);
            Field f = c.getField(args[1]);
            System.out.format("Type: %s%n", f.getType());
            System.out.format("GenericType: %s%n", f.getGenericType());

            // production code should handle these exceptions more gracefully
        } catch (ClassNotFoundException x) {
            x.printStackTrace();
        } catch (NoSuchFieldException x) {
            x.printStackTrace();
        }
    }
}
```

```shell
$ java FieldSpy FieldSpy b
Type: class [[Z
GenericType: class [[Z
$ java FieldSpy FieldSpy name
Type: class java.lang.String
GenericType: class java.lang.String
$ java FieldSpy FieldSpy list
Type: interface java.util.List
GenericType: java.util.List<java.lang.Integer>
$ java FieldSpy FieldSpy val
Type: class java.lang.Object
GenericType: T
```

#### 필드값 Getting, Setting

클래스의 인스턴스가 주어지면 리플렉션을 사용하여 해당 클래스의 필드 값을 설정할 수 있다. 이러한 액세스는 일반적으로 클래스의 설계 의도에 위배되기 때문에 최대한의 신중하게 사용해야한다.

 `Book`클래스는 long, array 및 enum 필드 타입의 값을 설정하는 방법을 보여줍니다. 다른 기본 타입을 가져오고 설정하는 방법은  [`Field`](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Field.html#method_summary)에 설명되어있다.

```java
import java.lang.reflect.Field;
import java.util.Arrays;
import static java.lang.System.out;

enum Tweedle { DEE, DUM }

public class Book {
    public long chapters = 0;
    public String[] characters = { "Alice", "White Rabbit" };
    public Tweedle twin = Tweedle.DEE;

    public static void main(String... args) {
        Book book = new Book();
        String fmt = "%6S:  %-12s = %s%n";

        try {
            Class<?> c = book.getClass();

            Field chap = c.getDeclaredField("chapters");
            out.format(fmt, "before", "chapters", book.chapters);
            chap.setLong(book, 12);
            out.format(fmt, "after", "chapters", chap.getLong(book));

            Field chars = c.getDeclaredField("characters");
            out.format(fmt, "before", "characters",
                    Arrays.asList(book.characters));
            String[] newChars = { "Queen", "King" };
            chars.set(book, newChars);
            out.format(fmt, "after", "characters",
                    Arrays.asList(book.characters));

            Field t = c.getDeclaredField("twin");
            out.format(fmt, "before", "twin", book.twin);
            t.set(book, Tweedle.DUM);
            out.format(fmt, "after", "twin", t.get(book));

            // production code should handle these exceptions more gracefully
        } catch (NoSuchFieldException x) {
            x.printStackTrace();
        } catch (IllegalAccessException x) {
            x.printStackTrace();
        }
    }
}
```

```shell
$ java Book
BEFORE:  chapters     = 0
 AFTER:  chapters     = 12
BEFORE:  characters   = [Alice, White Rabbit]
 AFTER:  characters   = [Queen, King]
BEFORE:  twin         = DEE
 AFTER:  twin         = DUM
```

### `Method`

메소드에는 호출할수 있는 실행 가능한 코드가 포함되어 있다. 메소드는 리플렉션 코드가 아닌 경우, 컴파일러에 의해 overloading, overriding, hiding 과 같이 동작한다. 반대로 리플렉션 코드를 사용하면 수퍼 클래스를 고려하지 않고 메서드 선택을 특정 클래스로 제한 할 수 있다. 

#### 메소드 호출

리플렉션은 클래스에서 메서드를 호출하는 수단을 제공한다. 리플렉션이 아닌 코드에서 클래스의 인스턴스를 원하는 타입으로 캐스팅 할 수없는 경우에만 사용해야 한다. 메소드는 [`java.lang.reflect.Method.invoke()`](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Method.html#invoke-java.lang.Object-java.lang.Object...-)로 호출할 수 있다. 첫 번째 인수는이 특정 메서드가 호출 될 객체의 인스턴스입니다. (메서드가 `static`인 경우 첫 번째 인수는 `null`이어야 한다.) 다음 인수는 메서드의 매개 변수이다. 메서드가 예외가 발생하면  [`java.lang.reflect.InvocationTargetException`](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/InvocationTargetException.html) 로 Wrapp된다.  [`InvocationTargetException.getCause()`](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/InvocationTargetException.html#getCause--)메서드를 사용하여 메서드의 원래 예외를  검색 할 수 있다.

```java
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Locale;
import static java.lang.System.out;
import static java.lang.System.err;

public class Deet<T> {
    private boolean testDeet(Locale l) {
        // getISO3Language() may throw a MissingResourceException
        out.format("Locale = %s, ISO Language Code = %s%n", l.getDisplayName(), l.getISO3Language());
        return true;
    }

    private int testFoo(Locale l) { return 0; }
    private boolean testBar() { return true; }

    public static void main(String... args) {
        if (args.length != 4) {
            err.format("Usage: java Deet <classname> <langauge> <country> <variant>%n");
            return;
        }

        try {
            Class<?> c = Class.forName(args[0]);
            Object t = c.newInstance();

            Method[] allMethods = c.getDeclaredMethods();
            for (Method m : allMethods) {
                String mname = m.getName();
                if (!mname.startsWith("test")
                        || (m.getGenericReturnType() != boolean.class)) {
                    continue;
                }
                Type[] pType = m.getGenericParameterTypes();
                if ((pType.length != 1)
                        || Locale.class.isAssignableFrom(pType[0].getClass())) {
                    continue;
                }

                out.format("invoking %s()%n", mname);
                try {
                    m.setAccessible(true);
                    Object o = m.invoke(t, new Locale(args[1], args[2], args[3]));
                    out.format("%s() returned %b%n", mname, (Boolean) o);

                    // Handle any exceptions thrown by method to be invoked.
                } catch (InvocationTargetException x) {
                    Throwable cause = x.getCause();
                    err.format("invocation of %s failed: %s%n",
                            mname, cause.getMessage());
                }
            }

            // production code should handle these exceptions more gracefully
        } catch (ClassNotFoundException x) {
            x.printStackTrace();
        } catch (InstantiationException x) {
            x.printStackTrace();
        } catch (IllegalAccessException x) {
            x.printStackTrace();
        }
    }
}
```

```java
$ java Deet Deet ja JP JP
invoking testDeet()
Locale = Japanese (Japan,JP), 
ISO Language Code = jpn
testDeet() returned true
$ java Deet Deet xx XX XX
invoking testDeet()
invocation of testDeet failed: 
Couldn't find 3-letter language code for xx
```

### 생성자

메서드와 마찬가지로 리플렉션은 클래스의 생성자를 검색하고 modifier, 매개 변수, annotation 및 throw 된 예외와 같은 선언 정보를 얻기 위한 API를 제공한다. 지정된 생성자를 사용하여 클래스의 새 인스턴스를 만들 수도 있다.

#### 새로운 클래스 인스턴스 생성

새로운 클래스 인스턴스를 만드는 리플렉션 메소드는 [`java.lang.reflect.Constructor.newInstance()`](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Constructor.html#newInstance-java.lang.Object...-) and [`Class.newInstance()`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html#newInstance--) 두개 가 있다. 전자가 자주 선호되며 이유는 다음과 같다.

- [`Class.newInstance()`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html#newInstance--)는 인수가없는 생성자 만 호출 할 수있는 반면 [`Constructor.newInstance()`](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Constructor.html#newInstance-java.lang.Object...-)는 매개 변수 수에 관계없이 모든 생성자를 호출 할 수 있다.
- [`Class.newInstance()`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html#newInstance--)는 확인 여부에 관계없이 생성자에서 throw 된 모든 예외를 throw 한다. [`Constructor.newInstance()`](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Constructor.html#newInstance-java.lang.Object...-)는 항상 throw 된 예외를 [`InvocationTargetException`](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/InvocationTargetException.html)로 Wrapp한다.
- [`Class.newInstance()`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html#newInstance--)생성자가 접근할 수 있어야 한다.  [`Constructor.newInstance()`](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Constructor.html#newInstance-java.lang.Object...-)는   `private` 인 생성자도 호출할 수 있다.

```java
import java.io.Console;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;

import static java.lang.System.out;

public class ConsoleCharset {
    public static void main(String... args) {
        Constructor[] ctors = Console.class.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }

        try {
            ctor.setAccessible(true);
            Console c = (Console)ctor.newInstance();
            Field f = c.getClass().getDeclaredField("cs");
            f.setAccessible(true);
            out.format("Console charset         :  %s%n", f.get(c));
            out.format("Charset.defaultCharset():  %s%n",
                    Charset.defaultCharset());

            // production code should handle these exceptions more gracefully
        } catch (InstantiationException x) {
            x.printStackTrace();
        } catch (InvocationTargetException x) {
            x.printStackTrace();
        } catch (IllegalAccessException x) {
            x.printStackTrace();
        } catch (NoSuchFieldException x) {
            x.printStackTrace();
        }
    }
}
```

```shell
$ java ConsoleCharset
Console charset          :  ISO-8859-1
Charset.defaultCharset() :  ISO-8859-1
```

