## Dependencies

### Dependency Injection

Container가 객체가 생성될때 생성자 인수, 팩토리메소드 등을 이용해서 의존성을 주입해주는 것을 의미한다 

#### 생성자 기반 의존성 주입

의존성을 나타내는 생성자의 인수에 container가 객채를 생성하면서 의존성 주입을 한다 `static` 팩토리 메서드도 비슷한 방법으로 팩토리 메서드를 호출하여 빈을 생성한다

```java
public class SimpleMovieLister {

    // the SimpleMovieLister has a dependency on a MovieFinder
    private MovieFinder movieFinder;

    // a constructor so that the Spring container can inject a MovieFinder
    public SimpleMovieLister(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // business logic that actually uses the injected MovieFinder is omitted...
}
```

##### 생성자 인수 확인

생성자 인수 확인은 인수 타입을 사용한다 Bean 정의 순서가 bean이 생성될때 인수로 넣어지는 순서이다

```java
package x.y;

public class ThingOne {

    public ThingOne(ThingTwo thingTwo, ThingThree thingThree) {
        // ...
    }
}
```

```xml
<beans>
    <bean id="beanOne" class="x.y.ThingOne">
        <constructor-arg ref="beanTwo"/>
        <constructor-arg ref="beanThree"/>
    </bean>

    <bean id="beanTwo" class="x.y.ThingTwo"/>

    <bean id="beanThree" class="x.y.ThingThree"/>
</beans>
```

##### 생성자 인수 타입 매칭

`type` 을 사용해 특정한 타입을 생성자 인수와 매칭 시킬수 있다

```java
package examples;

public class ExampleBean {

    // Number of years to calculate the Ultimate Answer
    private int years;

    // The Answer to Life, the Universe, and Everything
    private String ultimateAnswer;

    public ExampleBean(int years, String ultimateAnswer) {
        this.years = years;
        this.ultimateAnswer = ultimateAnswer;
    }
}
```

```xml
<bean id="exampleBean" class="examples.ExampleBean">
    <constructor-arg type="int" value="7500000"/>
    <constructor-arg type="java.lang.String" value="42"/>
</bean>
```

##### 생성자 인수 인덱스 번호로 매칭

`index`  를 사용해 index번호와 생성자 인수를 매칭 시킬 수 있다

```xml
<bean id="exampleBean" class="examples.ExampleBean">
    <constructor-arg index="0" value="7500000"/>
    <constructor-arg index="1" value="42"/>
</bean>
```

##### 생성자 인수 이름으로 매칭

```xml
<bean id="exampleBean" class="examples.ExampleBean">
    <constructor-arg name="years" value="7500000"/>
    <constructor-arg name="ultimateAnswer" value="42"/>
</bean>
```

생성자 인수 이름으로 매칭을 하려면 컴파일 시 디버그 플래그를 활성화 하고 컴퍼일을 해야한다. 또는 `@ConstructorProperties` 를 이용해 생성자 인수 이름을 설정해야 한다.

```java
package examples;

public class ExampleBean {

    // Fields omitted

    @ConstructorProperties({"years", "ultimateAnswer"})
    public ExampleBean(int years, String ultimateAnswer) {
        this.years = years;
        this.ultimateAnswer = ultimateAnswer;
    }
}
```

#### Setter 기반 의존성 주입

container가 기본 생성자 또는 인수가 없은 `static` 팩토리 메소드로 객체를 생성한후 setter메소드를 호출하여 의존성을 주입한다

```java
public class SimpleMovieLister {

    // the SimpleMovieLister has a dependency on the MovieFinder
    private MovieFinder movieFinder;

    // a setter method so that the Spring container can inject a MovieFinder
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // business logic that actually uses the injected MovieFinder is omitted...
}
```

> Setter기반 의존성 주입은 Optional 의존성에만 사용하며 주입받은 의존성은 사용시 `null` 체크를 해야 한다

#### 의존성 확인 과정

- `ApplicationContext`가 빈 구성 정보를 이용해 만들어진다. 빈 구성 정보는 XML, Java code, annotation을 이용할 수 있다
- 각각의 bean에서 의존성은 프로퍼티 생성자 인수 static 팩토리 메소드 인수로 나타낸다 의존성은 bean이 실제 생성될때 제공된다.
- 각각의 프로퍼티와 생성자 인수는 실제 설정 되어야 할 값이거나 container에 있는 다른 bean에 대한 참조이다.
- 각각의 프로퍼티와 생성자 인수는 실제  프로퍼티와 인수의 타입으로 매핑된다

Spring Container는 container가 생성될때 각각의 빈 설정을 검증한다. 그러나 bean 프로퍼티는 bean이 실제 생성될때 까지 설정 되지 않는다. 기본값인 singleton-scoped, pre-instantiated로 설정된 bean은 container가 생성될때 만들어 진다. 다른 bean들은 bean이 필요할때 생성된다

> ##### 순환 의존성
>
> Class A에 Class B가 의존성 주입으로 필요하고 Class B에 의존성 주입으로 Class A가 필요한 경우 Class A, Class B가 서로 의존성 주입 되도록 Bean을 구성하면 Spring IoC Container는 런타임에 순환 참조를 감지하고 BeanCurrentlyInCreationException 예외를 던진다.
>
>
> Setter를 통한 의존성 주입으로 해결 할 수 있다.

Spring은 가능한 늦게 bean이 실제로 생성될때, 프로터티를 설정하고, 의존성을 해결한다. 이는 Spring container가 나중에 예외를 발생 시킬수 있음을 의미한다. 그래서 ApplicationContext가 기본값으로 싱글톤, 사전 인스턴스화로 설정된것을 미리 생성하는 이유다. 싱글톤 빈을 사전 인스턴스화 대신 지연 초기화로 설정 할 수도 있다.

#### 의존성 주입 예시

Setter를 이용해 의존성 주입을 받는 예시

```xml
<bean id="exampleBean" class="examples.ExampleBean">
    <!-- setter injection using the nested ref element -->
    <property name="beanOne">
        <ref bean="anotherExampleBean"/>
    </property>

    <!-- setter injection using the neater ref attribute -->
    <property name="beanTwo" ref="yetAnotherBean"/>
    <property name="integerProperty" value="1"/>
</bean>

<bean id="anotherExampleBean" class="examples.AnotherBean"/>
<bean id="yetAnotherBean" class="examples.YetAnotherBean"/>
```

```java
public class ExampleBean {

    private AnotherBean beanOne;

    private YetAnotherBean beanTwo;

    private int i;

    public void setBeanOne(AnotherBean beanOne) {
        this.beanOne = beanOne;
    }

    public void setBeanTwo(YetAnotherBean beanTwo) {
        this.beanTwo = beanTwo;
    }

    public void setIntegerProperty(int i) {
        this.i = i;
    }
}
```

생성자 기반 의존성 주입

```xml
<bean id="exampleBean" class="examples.ExampleBean">
    <!-- constructor injection using the nested ref element -->
    <constructor-arg>
        <ref bean="anotherExampleBean"/>
    </constructor-arg>

    <!-- constructor injection using the neater ref attribute -->
    <constructor-arg ref="yetAnotherBean"/>

    <constructor-arg type="int" value="1"/>
</bean>

<bean id="anotherExampleBean" class="examples.AnotherBean"/>
<bean id="yetAnotherBean" class="examples.YetAnotherBean"/>
```

```java
public class ExampleBean {

    private AnotherBean beanOne;

    private YetAnotherBean beanTwo;

    private int i;

    public ExampleBean(
        AnotherBean anotherBean, YetAnotherBean yetAnotherBean, int i) {
        this.beanOne = anotherBean;
        this.beanTwo = yetAnotherBean;
        this.i = i;
    }
}
```

`static` 팩토리 메서드를 이용해 의존성 주입

```xml
<bean id="exampleBean" class="examples.ExampleBean" factory-method="createInstance">
    <constructor-arg ref="anotherExampleBean"/>
    <constructor-arg ref="yetAnotherBean"/>
    <constructor-arg value="1"/>
</bean>

<bean id="anotherExampleBean" class="examples.AnotherBean"/>
<bean id="yetAnotherBean" class="examples.YetAnotherBean"/>
```

```java
public class ExampleBean {

    // a private constructor
    private ExampleBean(...) {
        ...
    }

    // a static factory method; the arguments to this method can be
    // considered the dependencies of the bean that is returned,
    // regardless of how those arguments are actually used.
    public static ExampleBean createInstance (
        AnotherBean anotherBean, YetAnotherBean yetAnotherBean, int i) {

        ExampleBean eb = new ExampleBean (...);
        // some other operations...
        return eb;
    }
}
```

### 의존성과 설정 자세히

Spring Xml 기반 설정에서는`<property/>` 와 `<constructor-arg/>`  sub-element 타입을 제공해 프로퍼티와 생성자 인수를 정의 할 수 있다

#### Primitives, Strings, and so on

`value` 속성을 이용해 프로퍼티와 생성자 인수 의 값을 지정 할 수 있다.

```xml
<bean id="myDataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
    <!-- results in a setDriverClassName(String) call -->
    <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
    <property name="url" value="jdbc:mysql://localhost:3306/mydb"/>
    <property name="username" value="root"/>
    <property name="password" value="misterkaoli"/>
</bean>
```

p-namespce를 이용해 xml 설정을 간결하게 표시할수 있다

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:p="http://www.springframework.org/schema/p"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="myDataSource" class="org.apache.commons.dbcp.BasicDataSource"
        destroy-method="close"
        p:driverClassName="com.mysql.jdbc.Driver"
        p:url="jdbc:mysql://localhost:3306/mydb"
        p:username="root"
        p:password="misterkaoli"/>

</beans>
```

##### `idref` element

`idref` 는 container에 있는 다른 bean들의 id 값을 사용하여 에러를 방지하는 방법이다

```xml
<bean id="theTargetBean" class="..."/>

<bean id="theClientBean" class="...">
    <property name="targetName">
        <idref bean="theTargetBean"/>
    </property>
</bean>
```

아래와 같이 사용할 수 있다

```xml
<bean id="theTargetBean" class="..." />

<bean id="client" class="...">
    <property name="targetName" value="theTargetBean"/>
</bean>
```

#### 다른 빈 참조

`ref` element를 이용해 다른 빈에 대한 참조를 할 수 있다

```xml
<ref bean="someBean"/>
```

`parent` 속성을 통해 Bean을 설정하면 현재 container의 상위 container에 참조를 만들수 있다. 상위 container와 이름이 같은 프록시를 만드는 경우 사용

```xml
<!-- in the parent context -->
<bean id="accountService" class="com.something.SimpleAccountService">
    <!-- insert dependencies as required as here -->
</bean>
```

```xml
<!-- in the child (descendant) context -->
<bean id="accountService" <!-- bean name is the same as the parent bean -->
    class="org.springframework.aop.framework.ProxyFactoryBean">
    <property name="target">
        <ref parent="accountService"/> <!-- notice how we refer to the parent bean -->
    </property>
    <!-- insert other configuration and dependencies as required here -->
</bean>
```

#### 내부 Beans

Bean 안에 Bean을 정의 할 수 있다

```xml
<bean id="outer" class="...">
    <!-- instead of using a reference to a target bean, simply define the target bean inline -->
    <property name="target">
        <bean class="com.example.Person"> <!-- this is the inner bean -->
            <property name="name" value="Fiona Apple"/>
            <property name="age" value="25"/>
        </bean>
    </property>
</bean>
```

내부 빈은 `id` 와 `name` 이 필요하지 않고 `scope`는 무시된다. 항삭 익명으로 생성되고 외부 bean과 같이 생성되기 때문

#### Collections

`<list/>` , `<set/>`, `<map/>`, `<props/> `, 는 `Collection` 타입 `List`, `Set`, `Map`, `Properties`를 의미한다.

```xml
<bean id="moreComplexObject" class="example.ComplexObject">
    <!-- results in a setAdminEmails(java.util.Properties) call -->
    <property name="adminEmails">
        <props>
            <prop key="administrator">administrator@example.org</prop>
            <prop key="support">support@example.org</prop>
            <prop key="development">development@example.org</prop>
        </props>
    </property>
    <!-- results in a setSomeList(java.util.List) call -->
    <property name="someList">
        <list>
            <value>a list element followed by a reference</value>
            <ref bean="myDataSource" />
        </list>
    </property>
    <!-- results in a setSomeMap(java.util.Map) call -->
    <property name="someMap">
        <map>
            <entry key="an entry" value="just some string"/>
            <entry key ="a ref" value-ref="myDataSource"/>
        </map>
    </property>
    <!-- results in a setSomeSet(java.util.Set) call -->
    <property name="someSet">
        <set>
            <value>just some string</value>
            <ref bean="myDataSource" />
        </set>
    </property>
</bean>
```

##### Null, Empty String 값 표시

빈 문자열 표시

```xml
<bean class="ExampleBean">
    <property name="email" value=""/>
</bean>
```

`null` 표시

```xml
<bean class="ExampleBean">
    <property name="email">
        <null/>
    </property>
</bean>
```

### `depends-on`

어떤 빈이 다른 빈에 종속적일때 사용 데이터베이스 드라이버 등록 같은 경우

```xml
<bean id="beanOne" class="ExampleBean" depends-on="manager"/>
<bean id="manager" class="ManagerBean" />
```

종속성이 여러개인 경우 표시

```xml
<bean id="beanOne" class="ExampleBean" depends-on="manager,accountDao">
    <property name="manager" ref="manager" />
</bean>

<bean id="manager" class="ManagerBean" />
<bean id="accountDao" class="x.y.jdbc.JdbcAccountDao" />
```

### Lazy-initialized Beans

lazy-initialized beans는 싱글톤 스코프의 빈을 `ApplicaionContext`가 시작될때가 아닌 bean이 처음 사용될때 초기화를 시킨다

```xml
<bean id="lazy" class="com.something.ExpensiveToCreateBean" lazy-init="true"/>
<bean name="not.lazy" class="com.something.AnotherBean"/>
```

`default-lazy-init`속성을 사용해서 container level에서 지연 초기화를 설정할 수 있다

```xml
<beans default-lazy-init="true">
    <!-- no beans will be pre-instantiated... -->
</beans>
```

### Autowiring

##### Autowiring 모드

- `no`: 기본값으로 Bean의 참조는 `ref`로 정의되어야 한다
- `byName`: 프로터티 내임과 일치하는게 있으면 자동으로 연결
- `byType`: 컨테이서에 해당 프로퍼티와 같은 타입이 존재하면 자동으로 주입
- `constructor`: `byType` 과 비슷하지만 같은 타입이 하나가 아닌경우 오류

#### Autowiring의 한계와 단점

- Primitives 타입은 autowiring  할 수 없다
- 사용 가능한 빈이 없으면 예외가 발생한다

#### Bean 별 Autowiring 설정

Bean별로 autowiring을 안하게 설정할 수 있다. `autowire-condidate`에서 `false`로 설정

### 메소드 주입

싱글톤 스코프인 A가 싱글톤 스코프가 아닌 B를 의존성 주입 받으려는 경우 A는 B를 의존성 주입받을 수없다,

`ApplicationContextAware`을 구현하여  ApplicationContext를 통해`getBean("B")`를 빈 A가 필요할 떄 호출하여 사용할 수 있지만 Spring에 종속되기 때문에 바람직하지 못하다.

```java
// a class that uses a stateful Command-style class to perform some processing
package fiona.apple;

// Spring-API imports
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class CommandManager implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public Object process(Map commandState) {
        // grab a new instance of the appropriate Command
        Command command = createCommand();
        // set the state on the (hopefully brand new) Command instance
        command.setState(commandState);
        return command.execute();
    }

    protected Command createCommand() {
        // notice the Spring API dependency!
        return this.applicationContext.getBean("command", Command.class);
    }

    public void setApplicationContext(
            ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
```

#### Lookup Method Injection

Lookup Method Injection은 컨테이너가 관리하는 Bean의 메소드를 override 하고 컨테이너가 관리하는 다른 Bean을 조회하여 리턴하는 기능

```java
package fiona.apple;

// no more Spring imports!

public abstract class CommandManager {

    public Object process(Object commandState) {
        // grab a new instance of the appropriate Command interface
        Command command = createCommand();
        // set the state on the (hopefully brand new) Command instance
        command.setState(commandState);
        return command.execute();
    }

    // okay... but where is the implementation of this method?
    protected abstract Command createCommand();
```

`abstract`로 선언된 메소드가 있으면 하위 클래스를 동적으로 생성될때 원래 클래스에 정의 된 메서드를 재정의 한다

```xml
<!-- a stateful bean deployed as a prototype (non-singleton) -->
<bean id="myCommand" class="fiona.apple.AsyncCommand" scope="prototype">
    <!-- inject dependencies here as required -->
</bean>

<!-- commandProcessor uses statefulCommandHelper -->
<bean id="commandManager" class="fiona.apple.CommandManager">
    <lookup-method name="createCommand" bean="myCommand"/>
</bean>
```

Annotation 기반 구성에서는 다음과 같이 설정할 수 있다.

```java
public abstract class CommandManager {

    public Object process(Object commandState) {
        Command command = createCommand();
        command.setState(commandState);
        return command.execute();
    }

    @Lookup("myCommand")
    protected abstract Command createCommand();
}
```

# 