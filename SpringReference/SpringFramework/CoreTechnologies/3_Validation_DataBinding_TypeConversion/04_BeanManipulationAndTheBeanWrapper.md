## Bean Manipulation and the `BeanWrapper`

`org.springframework.beans` 패키지는 JavaBeans 표준을 준수한다. JavaBean은 다음 규칙을 따르는 클래스를 말하는데, 매개변수가 없는 디폴트 생성자를 가지고, 예를 들어 필드가 `bingoMadness` 인 경우 `setBingoMadness(..)` 이름의 setter를 메소드로 가지고 `getBingoMadness()` 를 getter로 가진다.

Beans 패키지에서 매우 중요한 클래스 중 하나는 `BeanWrapper`인터페이스와 해당 구현 ( `BeanWrapperImpl`)이다. `BeanWrapper` 는 프로퍼티 값을 get하고 set하는 기능,프로퍼티 드스크립터를 가져오는 기능, 프로퍼티가 읽을 수 있는지 쓸 수 있는지 결정하기 위해 쿼리할 수 있는 기능을 제공한다. 또한, `BeanWrapper`는 무한 계층까지 하위 프로퍼티에서 프로퍼티를 설정하는 것이 가능하도록 중첩된 프로퍼티도 지원한다. 그리고 `BeanWrapper`는 대상 클래스에 지원 코드를 두지 않고도 표준 자바빈 `PropertyChangeListeners`와 `VetoableChangeListeners`를 추가하는 기능도 지원한다. 마지막으로 가장 중요한 것은 `BeanWrapper`가 색인된 프로퍼티를 설정하는 지원을 제공한다는 것이다. `BeanWrapper`는 보통 어플리케이션 코드에서 직접 사용하지 않고 `DataBinder`와 `BeanFactory`에서 사용한다.

`BeanWrapper`의 동작방식은 그 이름이 어느 정도 알려주고 있다. 프로퍼티를 설정하고 획득하는 것 같은 해당 빈에 대한 액션을 수행하기 위해서 빈을 감싼다.

### Setting and Getting Basic and Nested Properties

프로퍼티를 설정하고 가져오는 것은 `BeanWrapper`의  `setPropertyValue`와 `getPropertyValue` 오버로딩된 메소드 변형을 통해  이루어진다. 둘 다 다수의 오버로드된 메서드들이 있다. 자세한 내용은 스프링 자바독에 모두 설명되어 있다. 객체의 프로퍼티를 나타내는 여러 가지 관례가 있다는 사실은 중요하다. 다음은 몇가지 예제이다.

| 표현                 | 설명                                                         |
| -------------------- | ------------------------------------------------------------ |
| name                 | getName()나 isName()나 setName(..) 메서드와 대응되는 name 프로퍼티를 나타낸다. |
| account.name         | account 프로퍼티의 중첩된 name 프로퍼티를 나타낸다. 예를 들면 getAccount().setName()나 getAccount().getName()에 대응된다. |
| account[2]           | 색인된 account 프로퍼티의 세번째 요소를 나타낸다. 색인된 프로퍼티는 array, list나 자연스럽게 정렬된 컬렉션이 될 수 있다. |
| account[COMPANYNAME] | Map 프로퍼티 account의 COMPANYNAME 키로 찾은 값을 나타낸다.  |

다음은 `BeanWrapper`를 통해 값을 set하고 get하는 것을 보여준다.

```java
public class Company {

    private String name;
    private Employee managingDirector;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employee getManagingDirector() {
        return this.managingDirector;
    }

    public void setManagingDirector(Employee managingDirector) {
        this.managingDirector = managingDirector;
    }
}
```

```java
public class Employee {

    private String name;

    private float salary;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }
}

```

다음 코드는 인스턴스화된 `Companies`와 `Employees`의 프로퍼티를 어떻게 획득하고 조작하는지를 보여주는 예제이다.

```java
BeanWrapper company = new BeanWrapperImpl(new Company());
// setting the company name..
company.setPropertyValue("name", "Some Company Inc.");
// ... can also be done like this:
PropertyValue value = new PropertyValue("name", "Some Company Inc.");
company.setPropertyValue(value);

// ok, let's create the director and tie it to the company:
BeanWrapper jim = new BeanWrapperImpl(new Employee());
jim.setPropertyValue("name", "Jim Stravinsky");
company.setPropertyValue("managingDirector", jim.getWrappedInstance());

// retrieving the salary of the managingDirector through the company
Float salary = (Float) company.getPropertyValue("managingDirector.salary");
```

###  Built-in `PropertyEditor` Implementations

스프링은 `Object`와 `String`간의 변환에 `PropertyEditor`의 개념을 사용한다. 객체 자체와는 다른 방법으로 프로퍼티를 표현할 수 있는 손쉬운 방법이다. 예를 들어 Date는 사람이 읽을 수 있게 표현할 수 있고 (String '2007-14-09'처럼) 동시에 여전히 사람이 읽을 수 있는 형식을 다시 원래의 날짜로 변환할 수 있다.(또는 더 낫다: 사람이 읽을 수 있는 형식의 어떤 날짜도 다시 Date 객체로 변환할 수 있다.) 이 동작은 `java.beans.PropertyEditor` 타입의 커스텀 에디터를 등록함으로써 이뤄질 수 있다. `BeanWrapper`나 이전 챕터에서 얘기했던 대안적인 특정 IoC 컨테이너에 커스텀 에디터를 등록하면 어떻게 프로퍼티를 원하는 타입으로 변환하는 지 알려준다.

스프링에서 프로퍼티 수정을 사용하는 몇가지 예제:

- 빈의 속성 설정은 `PropertyEditor` 구현체를 사용하여 수행된다. XML 파일에 선언한 어떤 빈의 프로퍼티 값으로 `String`을 사용했을 때 스프링은 (해당 프로퍼티의 setter가 `Class` 파라미터를 가지고 있다면)파라미터를 Class 객체로 처리하려고 `ClassEditor`를 사용할 것이다.
- 스프링 MVC 프레임워크에서 HTTP 요청 파라미터의 파싱은 `CommandController`의 모든 하위클래스에 수동으로 연결할 수 있는 모든 종류의 `PropertyEditor`를 사용해서 수행된다.

Spring은  쉽게 사용할수 있도록 해주는 많은 `PropertyEditor` 내장 구현을 가지고 있다.  모두 `org.springframework.beans.propertyeditors` 패키지에 있다. 대부분은 기본적으로 `BeanWrapperImpl`에 등록된다. 프로퍼티 데이터가 몇가지 방법으로 설정할 수 있는 곳에서도 당연히 기본값은 자신만의 번형으로 오버라이드해서 등록할 수 있다.

| Class                     | 설명                                                         |
| ------------------------- | ------------------------------------------------------------ |
| `ByteArrayPropertyEditor` | 바이트 배열에 대한 에디터. 문자열은 해당되는 바이트 표현으로 변환된다 `BeanWrapperImpl`의 기본값으로 등록된다. |
| `ClassEditor`             | 클래스를 나타내는 문자열을 실제 클래스로 파싱하거나 그 반대로 파싱한다. 클래스를 찾지 못하면 `IllegalArgumentException`를 던진다. `BeanWrapperImpl`의 기본값으로 등록된다. |
| `CustomBooleanEditor`     | `Boolean` 프로퍼티에 대해 커스터마이징할 수 있는 프로퍼티 데이터다. `BeanWrapperImpl`의 기본값으로 등록되지만 커스텀 에디터처럼 커스텀 인스턴스를 등록함으로써 오버라이드할 수 있다. |
| `CustomCollectionEditor`  | 컬렉션에 대한 프로퍼티 에디터로 모든 소스 `Collection`를 전달한 타겟 `Collection` 타입으로 변환한다. |
| `CustomDateEditor`        | `java.util.Date`에 대한 커스터마이징 할 수 있는 프로퍼티 에디터로 커스텀 `DateFormat`을 지원한다. 기본값으로 등록되지 않는다. 적절한 형식으로 필요한 만큼 사용자가 등록해야 한다. |
| `CustomNumberEditor`      | `Integer`, `Long`, `Float`, `Double`같은 숫자타입의 하위클래스에 대한 커스마타이징할 수 있는 프로퍼티 에디터이다. `BeanWrapperImpl`의 기본값으로 등록되지만 커스텀 에디터처럼 커스텀 인스턴스를 등록해서 오바라이드할 수 있다. |
| `FileEditor`              | 문자열을 `java.io.File` 객체로 처리할 수 있다. `BeanWrapperImpl`의 기본값으로 등록된다. |
| `InputStreamEditor`       | `InputStream` 프로퍼티를 문자열로 직접 설정할 수 있도록 텍스트 문자열을 받아서 `InputStream`을 생성하는(중간에 `ResourceEditor`와 `Resource`를 통해서) 단방향 프로퍼티 에디터이다. 기본 사용법은 `InputStream`를 닫지 않는다. `BeanWrapperImpl`의 기본값으로 등록된다. |
| `LocaleEditor`            | 문자열을 `Locale` 객체로 처리하거나 그 반대로 할 수 있다.(문자열 형식은 `Locale`의 `toString()` 메서드가 제공하는 형식과 같은 *[country]*[variant]이다.) `BeanWrapperImpl`의 기본값으로 등록된다. |
| `PatternEditor`           | 문자열을 `java.util.regex.Pattern` 객체로 처리하거나 그 반대로 처리할 수 있다 |
| `PropertiesEditor`        | 문자열(`java.lang.Properties` 클래스의 Javadoc에서 정의된 것과 같은 형식으로 포매팅된)을 `Properties` 객체로 변환할 수 있다.`BeanWrapperImpl`의 기본값으로 등록된다. |
| `StringTrimmerEditor`     | 스트림을 `trim`하는 프로퍼티 에디터이다. 선택적으로 비어있는 문자열을 `null` 값으로 변형할 수도 있다. 기본적으로는 등록되지 않는다. 필요에 따라 사용자가 등록해야 한다. |
| `URLEditor`               | URL의 문자열 표현을 실제 `URL` 객체로 처리할 수 있다. `BeanWrapperImpl`의 기본값으로 등록된다. |

스프링은 필요한 프로퍼티 에디터에 검색경로를 설정하는데 `java.beans.PropertyEditorManager`를 사용한다. 검색 경로는 `Font`와 `Color`나 대부분의 프리미티브 타입같은 타입에 대한 `PropertyEditor` 구현체를 포함하는 `sun.bean.editors`를 포함할 수도 있다. 다루는 클래스와 같은 패키지에 있고 해당 클래스와 같은 이름이면서 `Editor`가 붙어있으면 표준 자바빈 기반은 자동적으로 PropertyEditor 클래스(명시적으로 등록하지 않아도)를 검색할 것이다. 예를 들어 `SometingEditor` 클래스를 인식하기에 충분하고 `Someting`타입에 대한 프로퍼티를 위한 `PropertyEditor`로 사용되려면 다음 클래스와 패키지 구조를 갖고 있어야 한다.

```java
com
  chank
    pop
      Something
      SomethingEditor // the PropertyEditor for the Something class
```

여기서도 표준 `BeanInfo` 자바빈 메카니즘을 사용할 수 있다. 다음은 연관된 클래스의 프로퍼티와 하나 이상의 `PropertyEditor` 인스턴스를 명시적으로 등록하기 위해 `BeanInfo` 메카니즘을 사용하는 예제다

```java
com
  chank
    pop
      Something
      SomethingBeanInfo // the BeanInfo for the Something class
```

```java
public class SomethingBeanInfo extends SimpleBeanInfo {

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            final PropertyEditor numberPE = new CustomNumberEditor(Integer.class, true);
            PropertyDescriptor ageDescriptor = new PropertyDescriptor("age", Something.class) {
                public PropertyEditor createPropertyEditor(Object bean) {
                    return numberPE;
                };
            };
            return new PropertyDescriptor[] { ageDescriptor };
        }
        catch (IntrospectionException ex) {
            throw new Error(ex.toString());
        }
    }
}
```

