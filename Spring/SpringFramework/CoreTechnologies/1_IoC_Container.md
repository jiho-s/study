# IoC Container

## 목차

1. [Spring IoC Container와 Beans](#spring-ioc-container와-beans)
2. [Container 개요](#container-개요)
3. [Bean 개요](#bean-개요)
4. [Dependencies](#dependencies)
5. [Bean Scope](#bean-scope)
6. [Customizing the Nature of a Bean](#customizing-the-nature-of-a-bean)
7. [Bean Definition Inheritance](#bean-definition-inheritance)
8. [Container Extension Point](#container-extension-point)
9. [Annotation-based Container Configuration](#annotation-based-container-configuration)

# Spring IoC Container와 Beans

## IoC(Inversion of Control)

DI(dependency injection)이라고도 하며 객체가 의존 객체를 만들어서 사용하는 것이 아니라, 생성자, 팩토리 메서드, setter를 통해 객체가  의존성을 주입 받는 것

## BeanFactory

- 빈 설정 소스로부터 빈 정의를 읽음
- 빈을 구성 또는 제공, 관리
- 빈 간의 의존관계 설정, 빈 주입

## ApplicationContext

BeanFactory를 상속받음 Spring에서 실제 사용, 아래의 기능 추가

- Spring AOP 기능과 쉽게 통합
- Message resource handling(메세지 다국화)
- 이벤트 발행

# Container 개요

ApplicationContext 구성 정보는 XML, Java Annotaions, Java 코드로 부터 가져올수 있다

## 구성 정보 설정

객체를 인스턴스, 구성, 어셈블 정보 지시 아래와 같은 방법을 제공한다

- XML 기반 구성
- Annotation 기반 구성
- Java code 기반 구성

### XML 기반 구성

XML 기반 구성은 resource 폴더 안에 application.xml파일을 만들고 다음과 같이 작성한다.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="bookService"
          class="me.jiho.springdemo.BookService">
        <property name="bookRepository" ref="bookRepository"/>
    </bean>
    <bean id="bookRepository"
          class="me.jiho.springdemo.BookRepository"/>

</beans>
```

- Id는 각각의 빈 식별
- class는 클래스 이름

## Container 예시

ApplicationContext는 ClassPath를 통해 생성할 수도 있다

```java
ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");
```

services.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- services -->

    <bean id="petStore" class="org.springframework.samples.jpetstore.services.PetStoreServiceImpl">
        <property name="accountDao" ref="accountDao"/>
        <property name="itemDao" ref="itemDao"/>
        <!-- additional collaborators and configuration for this bean go here -->
    </bean>

    <!-- more bean definitions for services go here -->

</beans>
```

daos.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="accountDao"
        class="org.springframework.samples.jpetstore.dao.jpa.JpaAccountDao">
        <!-- additional collaborators and configuration for this bean go here -->
    </bean>

    <bean id="itemDao" class="org.springframework.samples.jpetstore.dao.jpa.JpaItemDao">
        <!-- additional collaborators and configuration for this bean go here -->
    </bean>

    <!-- more bean definitions for data access objects go here -->

</beans>
```

### XML 기반 구성

`<import>` 를 이용해 여러 위치에서 가져올수 있다

```xml
<beans>
    <import resource="services.xml"/>
    <import resource="resources/messageSource.xml"/>
    <import resource="/resources/themeSource.xml"/>

    <bean id="bean1" class="..."/>
    <bean id="bean2" class="..."/>
</beans>
```

## Container에서 인스턴스 가져오기

`getBean(String name, Class<T> requiredType)` 을 이용해 빈의 인스턴스를 가져 올 수 있다

```java
// create and configure beans
ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");

// retrieve configured instance
PetStoreService service = context.getBean("petStore", PetStoreService.class);

// use configured instance
List<String> userList = service.getUsernameList();
```

# Bean 개요

Spring IoC Container은 한 개 이상의 Bean을 관리하고 Bean은 Bean 설정에 의해 생성된다

### BeanDefinition

빈의 정의에는 다음 프로퍼티 포함

- Class
- Name
- Scope
- Constructor arguments
- Properties
- Autowiring mode
- Lazy initialization mode
- Initialization method
- Destruction method

## 빈 이름 설정

XML 기반 구성에서 id, name 속성을 이용해서 설정한다. 이름을 여러개 설정하려는 경우 name에 `,` `;` 를 사용해서 지정할 수 있다

## Bean 인스턴스 생성

container는 요청시 BeanDefinition을 보고 실제 객체를 생성한다

빈은 두 가지 방법 중 하나로 생성할 수 있다

- `new` 로 생성자를 이용해 생성
- `static` 팩토리 메소드를 이용해 생성 

### 생성자로 인스턴스 생성

생성자를 통해 인스턴스를 생성하는 경우 코드 작성 없이 생성 할 수 있다

Xml 기반 구성을 사용하는 경우 다음과 같이 Bean을 정의 할 수 있다

```xml
<bean id="exampleBean" class="examples.ExampleBean"/>

<bean name="anotherExample" class="examples.ExampleBeanTwo"/>
```

### Static 팩토리 메서드로 인스턴스 생성

`static` 팩토리 메서드로 인스턴스를 생성하는 경우 `factory-method` 를 이용해 사용할 팩토리 메서드를 지정 할 수 있다

```xml
<bean id="clientService"
    class="examples.ClientService"
    factory-method="createInstance"/>
```

```java
public class ClientService {
    private static ClientService clientService = new ClientService();
    private ClientService() {}

    public static ClientService createInstance() {
        return clientService;
    }
}
```

### 인스턴스 팩토리 메서드로 인스턴스 생성

기존에 존재하는 Bean에서 비 `static` 팩토리 메서드로 객체를 생성하는 경우 `class` 속성을 사용하지 않고 `factory-bean` 에서 빈 이름을 정한다

```xml
<!-- the factory bean, which contains a method called createInstance() -->
<bean id="serviceLocator" class="examples.DefaultServiceLocator">
    <!-- inject any dependencies required by this locator bean -->
</bean>

<!-- the bean to be created via the factory bean -->
<bean id="clientService"
    factory-bean="serviceLocator"
    factory-method="createClientServiceInstance"/>
```

```java
public class DefaultServiceLocator {

    private static ClientService clientService = new ClientServiceImpl();

    public ClientService createClientServiceInstance() {
        return clientService;
    }
}
```

### Bean의 런타임 Type

빈의 정의에 type은 초기 reference일 뿐이고 실제 타입과 일치하지 않을 수  있다

런타임 타입을 알기 위해서는 `BeanFactory.getType` 을 이용해 알 수 있다.

# Dependencies

## Dependency Injection

Container가 객체가 생성될때 생성자 인수, 팩토리메소드 등을 이용해서 의존성을 주입해주는 것을 의미한다 

### 생성자 기반 의존성 주입

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

#### 생성자 인수 확인

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

#### 생성자 인수 타입 매칭

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

#### 생성자 인수 인덱스 번호로 매칭

`index`  를 사용해 index번호와 생성자 인수를 매칭 시킬 수 있다

```xml
<bean id="exampleBean" class="examples.ExampleBean">
    <constructor-arg index="0" value="7500000"/>
    <constructor-arg index="1" value="42"/>
</bean>
```

#### 생성자 인수 이름으로 매칭

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

### Setter 기반 의존성 주입

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

### 의존성 확인 과정

- `ApplicationContext`가 빈 구성 정보를 이용해 만들어진다. 빈 구성 정보는 XML, Java code, annotation을 이용할 수 있다
- 각각의 bean에서 의존성은 프로퍼티 생성자 인수 static 팩토리 메소드 인수로 나타낸다 의존성은 bean이 실제 생성될때 제공된다.
- 각각의 프로퍼티와 생성자 인수는 실제 설정 되어야 할 값이거나 container에 있는 다른 bean에 대한 참조이다.
- 각각의 프로퍼티와 생성자 인수는 실제  프로퍼티와 인수의 타입으로 매핑된다

Spring Container는 container가 생성될때 각각의 빈 설정을 검증한다. 그러나 bean 프로퍼티는 bean이 실제 생성될때 까지 설정 되지 않는다. 기본값인 singleton-scoped, pre-instantiated로 설정된 bean은 container가 생성될때 만들어 진다. 다른 bean들은 bean이 필요할때 생성된다

> #### 순환 의존성
>
> Class A에 Class B가 의존성 주입으로 필요하고 Class B에 의존성 주입으로 Class A가 필요한 경우 Class A, Class B가 서로 의존성 주입 되도록 Bean을 구성하면 Spring IoC Container는 런타임에 순환 참조를 감지하고 BeanCurrentlyInCreationException 예외를 던진다.
>
>
> Setter를 통한 의존성 주입으로 해결 할 수 있다.

Spring은 가능한 늦게 bean이 실제로 생성될때, 프로터티를 설정하고, 의존성을 해결한다. 이는 Spring container가 나중에 예외를 발생 시킬수 있음을 의미한다. 그래서 ApplicationContext가 기본값으로 싱글톤, 사전 인스턴스화로 설정된것을 미리 생성하는 이유다. 싱글톤 빈을 사전 인스턴스화 대신 지연 초기화로 설정 할 수도 있다.

### 의존성 주입 예시

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

## 의존성과 설정 자세히

Spring Xml 기반 설정에서는`<property/>` 와 `<constructor-arg/>`  sub-element 타입을 제공해 프로퍼티와 생성자 인수를 정의 할 수 있다

### Primitives, Strings, and so on

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

#### `idref` element

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

### 다른 빈 참조

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

### 내부 Beans

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

### Collections

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

#### Null, Empty String 값 표시

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

## `depends-on`

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

## Lazy-initialized Beans

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

## Autowiring

#### Autowiring 모드

- `no`: 기본값으로 Bean의 참조는 `ref`로 정의되어야 한다
- `byName`: 프로터티 내임과 일치하는게 있으면 자동으로 연결
- `byType`: 컨테이서에 해당 프로퍼티와 같은 타입이 존재하면 자동으로 주입
- `constructor`: `byType` 과 비슷하지만 같은 타입이 하나가 아닌경우 오류

### Autowiring의 한계와 단점

- Primitives 타입은 autowiring  할 수 없다
- 사용 가능한 빈이 없으면 예외가 발생한다

### Bean 별 Autowiring 설정

Bean별로 autowiring을 안하게 설정할 수 있다. `autowire-condidate`에서 `false`로 설정

## 메소드 주입

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

### Lookup Method Injection

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

# Bean Scope

Bean definition에서 생성된 객체의 스코프를 제어할 수 있다

#### Bean의 스코프

| Scope       | 설명                                         |
| ----------- | -------------------------------------------- |
| singleton   | 기본값, IoC 컨테이너 안에 하나의 객체만 생성 |
| prototype   | 주입될때마다 새로운 객채 생성                |
| request     | 하나의 HTTP 요청에 하나의 빈만 생성          |
| session     | 하나의 Session에 하나의 빈만 생성            |
| application | 하나의 ServletContext에 하나의 빈만 생성     |
| websocket   | 하나의 WebSocket에 하나의 빈 생성            |

## Singleton Scope

공유된 하나의 빈 인스턴스만 관리되며, ID가 같은 빈을 요청하는 모든 요청은 같은 인스턴스가 반환된다

```xml
<bean id="accountService" class="com.something.DefaultAccountService"/>

<!-- the following is equivalent, though redundant (singleton scope is the default) -->
<bean id="accountService" class="com.something.DefaultAccountService" scope="singleton"/>
```

## Prototype Scope

요청이 있을 때마다 새로운 인스턴스를 생성, 일반적으로 Stateful Bean에는 프로토타입 스코프를 설정하고 Stateless Bean에는 싱글톤 스코프를 사용

```xml
<bean id="accountService" class="com.something.DefaultAccountService" scope="prototype"/>
```

### 싱글톤 빈에 프로토타입 빈 의존성이 있는 경우

싱글톤 빈이 만들어 질때 새로운 프로토타입 빈이 만들어져 의존성 주입된다.

싱글톤 스코프의 빈이 런타임시 프로토타입 스코프 빈의 새 인스턴스를 원한다면 [Method Injection](#메소드-주입)을 이용한다.

## Request, Session, Application, and WebSocket Scope

### Initial Web Configuration

`request`, `session`, `application`, `websocket` level의 스코프를 시용하려면 추가적으로 설정을 해야한다.

Spring Web MVC에 있는 스코프의 빈을 사용하려면 `DispatcherServlet`이 상태를 관리하기 떄문에 추가적인 설정이 필요없다

`web.xml`에 다음을 추가한다

```xml
<web-app>
    ...
    <listener>
        <listener-class>
            org.springframework.web.context.request.RequestContextListener
        </listener-class>
    </listener>
    ...
</web-app>
```

###  Request scope

```xml
<bean id="loginAction" class="com.something.LoginAction" scope="request"/>
```

annotation기반 구성에서는 `@RequestScope`를 사용해서 설정할 수 있다.

```java
@RequestScope
@Component
public class LoginAction {
    // ...
}
```

### Session scope

```xml
<bean id="userPreferences" class="com.something.UserPreferences" scope="session"/>
```

annotation기반 구성에서는 `@SessionScope`를 사용하여 설정할 수 있다.

```java
@SessionScope
@Component
public class UserPreferences {
    // ...
}
```

### Application Scope

```xml
<bean id="appPreferences" class="com.something.AppPreferences" scope="application"/>
```

`Application Scope`는 `ServletContext`레벨에서 스코프가 정의되고 `ServletContext`의 속성으로 저장된다

annotation 기반 구성에서는 `@ApplicationScope`를 이용해 설정 할 수 있다.

```java
@ApplicationScope
@Component
public class AppPreferences {
    // ...
}
```

### Scoped Beans as Dependencies

`request-scoped` 빈을 수명이 긴 다른 빈에 의존송 주입을 하는 경우 해당 스코프 빈 대신에 AOP proxy를 사용할 수 있다. 해당 스코프와 동일한 인터페이스를 갖는 프록시를 주입하고 실제 객체에 메소드 호출을 위임할 수 있다

> `<aop:scoped-proxy/>`를 사용 할 수도 있고
>
> `ObjectFactory<MyTargetBean>`을 사용하여 인스턴스를 얻을 수 있다

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:aop="http://www.springframework.org/schema/aop"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd">

    <!-- an HTTP Session-scoped bean exposed as a proxy -->
    <bean id="userPreferences" class="com.something.UserPreferences" scope="session">
        <!-- instructs the container to proxy the surrounding bean -->
        <aop:scoped-proxy/> 
    </bean>

    <!-- a singleton-scoped bean injected with a proxy to the above bean -->
    <bean id="userService" class="com.something.SimpleUserService">
        <!-- a reference to the proxied userPreferences bean -->
        <property name="userPreferences" ref="userPreferences"/>
    </bean>
</beans>
```

`<aop:scoped-porxy/>`: 프록시를 정의

## Custom Scope

자체 범위를 정의하거나 기존 범위를 재정의 할 수 있다.  `singleton` 과 `prototype` 스코프는 재정의 할 수 없다

### Custom Scope 만들기

`Scope`인터페이스를 구현하여 Custom Scope를 만들 수 있다.

스코프에 있은 객체를 리턴하는 메소드

```java
Object get(String name, ObjectFactory<?> objectFactory)
```

스코프의 객체를 제거하는 메소드 `null`을 반환할 수 있다

```java
Object remove(String name)
```

스코프의 객체가 소멸될때 콜백을 등록

```java
void registerDestructionCallback(String name, Runnable destructionCallback)
```

스코프의 conversation 식별자 조회

```java
String getConversationId()
```

### Custom Scope 사용

Spring container에 스코프 등록하기 `ConfigurableBeanFactory`에 선언되어 있는 `registerScope(String scopeName, Scope Scope)` 이용

```java
void registerScope(String scopeName, Scope scope);
```

```java
Scope threadScope = new SimpleThreadScope();
beanFactory.registerScope("thread", threadScope);
```

```xml
<bean id="..." class="..." scope="thread">
```

# Customizing the Nature of a Bean

Spring Framework는 빈의 여러 특성을 커스텀 할 수 있는 여러 인터페이스를 제공한다

- Lifecycle Callbacks
- `ApplicationContextAware`과 `BeanNameAware`
- 다른 `Aware` 인터페이스

## Lifecycle Callbacks

`InitializingBean`과 `DisposableBean` 인터페이스를 설정하여 컨네이너가 `afterPropertiesSet()`과 `destroy()`를 실행할때 특정 작업을 수행하게 할 수 있다

> JSR-250 의 `@PostConstruct`, `@PreDestroy`를 사용하여 수행할 수 있다.

Spring Framework는 `BeanPostProcessor` 구현을 사용하여 모든 콜백 인터페이스를 처리한다. Spring이 기본적으로 제공하지 않는 라이프 사이클 콜백이 필요한경우 `BeanPostProcessor`을 구현하여 사용할 수 있다.

### Initialization Callbacks

`InitializingBean`인터페이스는 컨테이너가 프로퍼티를 설정한 이후 초기화 작업을 수행하게 설정할 수 있다

`InitializingBean`인터페이스는 한개의 메소드만 존재한다

```java
void afterPropertiesSet() throws Exception;
```

을 사용하면 코드가 스프링에 종속되므로 `@PostConstruct` annotaion을 사용하거나 XML기반 설정을 사용해 `init-method`를 사용하는 것이 좋다

```xml
<bean id="exampleInitBean" class="examples.ExampleBean" init-method="init"/>
```

```java
public class ExampleBean {

    public void init() {
        // do some initialization work
    }
}
```

`InitializingBean`을 이용한 예시

```xml
<bean id="exampleInitBean" class="examples.AnotherExampleBean"/>
```

```java
public class AnotherExampleBean implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        // do some initialization work
    }
}

```

### Destruction Callbacks

`DisposableBean` 인터페이스를 구현하면 빈을 포함하는 컨테이너가 파괴될때 빈이 콜백을 받을 수 있다

`DisposableBean`은 하나의 메소드를 가지고 있다

```java
void destroy() throws Exception;
```

`DisposableBean`은 코드를 스프링에 종속 시키기 때문에 사용하지 않는것이 좋다 `@PreDestroy`주석을 사용하거나 XML 구성시 `destroy-method`를 사용하는 것이 좋다

```xml
<bean id="exampleInitBean" class="examples.ExampleBean" destroy-method="cleanup"/>
```

```java
public class ExampleBean {

    public void cleanup() {
        // do some destruction work (like releasing pooled connections)
    }
}
```

`DisposableBean`을 사용한 예

```xml
<bean id="exampleInitBean" class="examples.AnotherExampleBean"/>
```

```java
public class AnotherExampleBean implements DisposableBean {

    @Override
    public void destroy() {
        // do some destruction work (like releasing pooled connections)
    }
}
```

### Default Initialization and Destroy Methods

프로젝트 전체에서 Initialization, Destroy Methods를 설정 할수 있다.

#### Initialization 메소드 등록

`init-method="init"`형식으로 등록 할 수 있다. Bean이 생성될때 해당 메소드가 있으면 메소드를 실행한다.

```java
public class DefaultBlogService implements BlogService {

    private BlogDao blogDao;

    public void setBlogDao(BlogDao blogDao) {
        this.blogDao = blogDao;
    }

    // this is (unsurprisingly) the initialization callback method
    public void init() {
        if (this.blogDao == null) {
            throw new IllegalStateException("The [blogDao] property must be set.");
        }
    }
}
```

```xml
<beans default-init-method="init">

    <bean id="blogService" class="com.something.DefaultBlogService">
        <property name="blogDao" ref="blogDao" />
    </bean>

</beans>
```

#### Destroy 메소드 등록

`default-destroy-method`를 사용하여 등록할 수 있다.

### Lifecycle Mechanisms 결합

##### Bean의 Lifecycle 동작을 제어하는 방법

- `InitializingBean` 및 `DisposableBean` 인터페이스
- Custom `init()`, `destroy()` 메소드
- `@PostConstruct`, `@PreDestroy` annotation

#### 결합하여 사용하는 경우 호출 순서

1. `@PostConstruct`
2. `InitializingBean`에 정의된 `afterPropertiesSet()`
3. custom `init()`

1. `@PreDestroy`
2. `DisposableBean`에 정의된 `destroy()`
3. custom `destroy()`

### Startup and Shutdown Callbacks

```java
public interface Lifecycle {

    void start();

    void stop();

    boolean isRunning();
}
```

스프링이 관리하는 모든 객체는 `Lifecycle` 인터페이스를 구현 가능하다. `ApplicationContext`가 시작 및 중지 신호를 받으면 context 안에 정의되어 있는 `Lifecycle` 구현체에 이를 전달한다. `LifecycleProcessor` 가 이를 위임 받아 처리한다.

```java
public interface LifecycleProcessor extends Lifecycle {

    void onRefresh();

    void onClose();
}
```

#### SmartLifecycle

시작과 종료 순서가 중요 할 수 있다. 특정 타입의 객체가 다른 타입의 객체보다 먼저 시작되어야하는 경우 `SmartLifecycle`을 이용하여 순서를 지정할 수 있다

```java
public interface Phased {

    int getPhase();
}
```

```java
public interface SmartLifecycle extends Lifecycle, Phased {

    boolean isAutoStartup();

    void stop(Runnable callback);
}
```

`getPhase()`를 이용하여 시작 순서를 정할 수 있다 낮은 순서부터 시작되고 종료시에는 역순으로 종료된다. `SmartLifecycle`을 구현하지 않은 일반적인 객체는 기본값이 0으로 설정된다.

`SmartLifecycle`의 `stop`메소드는 callback을 인자로 받는다. 모든 `SmartLifecycle` 구현체는 

종료가 완료되면 이를 실행해야한다. 이를 이용하여 필요한경우 비동기 종료가 가능하다. `LifecycleProcessor`의 기본 구현체인 `DefaultLifecycleProcessor`를 이용하여 timeout 시간만 다음과 같이 수정할 수 있다. 기본 값은 30초이다.

```xml
<bean id="lifecycleProcessor" class="org.springframework.context.support.DefaultLifecycleProcessor">
    <!-- timeout value in milliseconds -->
    <property name="timeoutPerShutdownPhase" value="10000"/>
</bean>
```

## `ApplicationContextAware`과 `BeanNameAware`

`ApplicationContext`가 `ApplicationContextAware`의 구현체를 생성할때 구현체는 `ApplicationContext`에게 정보를 제공할 수 있다

```java
public interface ApplicationContextAware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
```

빈은 `ApplicationContext`가 빈을 생성할때 `ApplicationContext`인터페이스 또는 하위 인터페이스를 조작할 수 있다. 이를 이용해 다른 빈들의 프로그램 방식을 알수 있다. 그러나 코드가 스프링에 종속되고 IoC 스타일을 따르지 않기 때문에 사용을 피하는 것이 좋다. 또한 `ApplicationContext`의 메소드는 파일 리소스에 접근 이벤트 발생, `MessageSource`에 접근을 제공한다.

다른 방법으로는 Autowiring을 통해 `ApplicationContext`의 정보를 얻을 수 있다

`ApplicationContext`가 `BeanNameAware` 인터페이스의 구현체를 생성 할때 클래스는 객체 정의에서 정의된 이름에 관한 정보를 제공해준다.

```java
public interface BeanNameAware {

    void setBeanName(String name) throws BeansException;
}
```

# Bean Definition Inheritance

Bean definition은 설정 정보, 생성자 인수, 프로퍼티 값, 컨테이너 틍정 정보를 포함한 많은 구성정보가 포함될 수 있다. child 빈은 이러한 설정 정보를 상속 받을 수 있다. child definition은 상속 받은 값 재정의 및 또는 필요한 다른 값을 추가 할 수 있다.

XML 기반 구성을 사용 할 때, child bean definition을 `parent` 속성을 사용해 나타낼 수 있다.

```xml
<bean id="inheritedTestBean" abstract="true"
        class="org.springframework.beans.TestBean">
    <property name="name" value="parent"/>
    <property name="age" value="1"/>
</bean>

<bean id="inheritsWithDifferentClass"
        class="org.springframework.beans.DerivedTestBean"
        parent="inheritedTestBean" init-method="initialize">  
    <property name="name" value="override"/>
    <!-- the age property value of 1 will be inherited from parent -->
</bean>
```

`abstact` 속성을 이용해 명시적으로 parent bean의 정의를 추상화 시켰다

# Container Extension Point

특정 통합 인터페이스 구현을 Spring IoC container에 확장 시킬 수 있다.

## `BeanPostProcessor`를 이용한 Bean Custom

`BeanPostProcessor` 인터페이스는 callback method가 있고, 콜백 메서드를 구현 하여 인스턴스화 로직, 의존성 구성 로직 등을 설정 할 수 있다. Spring Container가 인스턴스를 만들고 빈에 대한 구성과 초기화가 끝난후 로직을 추가시키고 싶으면 BeanPostProcessor 구현체를 이용해 추가할 수 있다

여러개의 `BeanPostProcessor` 인스턴스를 구성할수 있으며, `order` 속성을 이용하여 순서를 설정 할 수 있다. `BeanPostProcesser`인스턴스가 `Ordered` 인터페이를 구현 한 경우에만 속성을 설정 할 수 있다.

`BeanPostProcessor`는 두개의 콜백 메소드로 구성되어 있다. 컨테이너에 post-processor로 등록이 되면, 각각의 bean이 컨테이너에 의 해 생성될때 initialization methods 전에 콜백을 받고 initialization methods 후에 콜백을 받는다. Post-processor는 bean 인스턴스에 관련한 모든 것을 할 수 있다. 일반적으로 콜백 인스턴스를 확인하거나 프록시로 빈을 래핑 할 수 있다. 일부 Spring AOP 인프라 클래스는 프록시 랩핑 로직을 제공하기 위해 post-processor를 사용한다.

`@Bean`을 이용해 `BeanPostProcessor`를 선언 할때, 리턴 타입은 `BeanPostProcessor`의 구현체 이거나 또는 `BeanPostProcessor`인터페이스 여야한다. 그렇지 않으면 `ApplicationContext` 가 구성될 때 자동으로 등록 할 수 없다.

> #### 프로그램 방식으로 `BeanPostProcessor` 인터페이스 등록
>
> `ApplicationContext`자동 검색으로 등록되는 것을 권장하지만, `ConfigurableBeanFactory`의 `addBeanPostProcessor` 메소드를 이용하여 등록할 수 있다. 그러나 `Ordered`를 고려하지 않고 등록이 된다.

#### `BeanPostProcessor` 예시: `RequiredAnnotationBeanPostProcessor`

`BeanPostProcessor` 구현체로 annotation으로 표시된 자바 빈의 프로퍼티의 빈들의 의조선 주입을 보장한다.

## `BeanFactoryPostProcessor`를 이용한 설정 데이터 Custom

`BeanFactoryPostProcessor`는 `BeanPostProcessor`와 비슷하지만 가장큰 차이는 `BeanFactoryPostProcessor`는 빈 설정데이터에서 작동한다. Spring IoC container가 `BeanFactoryPostProcessor`이외의 빈들의 인스턴스를 만들기 전에 `BeanPostProcessor`가 설정 데이터를 읽고 이를 변경할 수 있다.

여러개의 `BeanFactoryPostProcessor`를 설정 할 수 있으며 `Orderd`인터페이스를 구현한 경우 `order`를 이용해 순서를 정할 수 있다.

> 실제 빈 인스턴스를 변경 하려는 경우 `BeanPostProcessor`를 이용해야한다. `BeanFactoryPostProcessor`를 이용해서도 변경 할 수 있지만 컨테이너 표준 라이프 사이클에 위반된다.

`ApplicationContext`는 자동으로 `BeanFactoryPostProcessor` 구현체를 감지하여 등록한다.

> `Bean(Factory)PostProcessor`는 지연초기화를 설정해도 무시된다.

#### BeanFactoryPostProcessor 예시: `PropertySourcePlaceholderConfigurer`

`PropertySourcePlaceholderConfigurer`를 사용하여 Java `Properties` 포맷을 사용하여 별도의 파일에 있는 bean definition의 프로퍼티를 사용할 수 있다. 이를 이용해 데이터베이스 url 같은 환경 변수 설정을 XML 정의 파일 또는 컨테이너 파일 수정없이 변경 할 수 있다.

다음 `DataSource`에는 placeholder 값이 정의 되어 있다.

```xml
<bean class="org.springframework.context.support.PropertySourcesPlaceholderConfigurer">
    <property name="locations" value="classpath:com/something/jdbc.properties"/>
</bean>

<bean id="dataSource" destroy-method="close"
        class="org.apache.commons.dbcp.BasicDataSource">
    <property name="driverClassName" value="${jdbc.driverClassName}"/>
    <property name="url" value="${jdbc.url}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>
```

런타임시 `PropertySourcePlaceholderConfigurer`는 다른 값으로 `DataSource`의 프로퍼티로 변경한다.

```properties
jdbc.driverClassName=org.hsqldb.jdbcDriver
jdbc.url=jdbc:hsqldb:hsql://production:9002
jdbc.username=sa
jdbc.password=root
```

`${jdbc.username}`은 'sa'로 바뀐다.

`PropertySourcePlcaeholderConfigurer`는 `Properties` file에서 프로퍼티를 찾을 뿐아니라, Spring `Environment` 프로퍼티와 자바의 `System` 프로퍼티도 찾는다.

## `FactoryBean`으로 인스턴스 로직 Custom

`FactoryBean`을 사용하면 생성자나 정적 메소드가 아닌 다른 방법으로 생성되는 객체를 스프링 빈으로 사용할 수 있게 된다.

`FactoryBean` 는 세개의 메소드를 제공한다

- `Object getObject()`: 팩토리가 생산하는 객체를 반환한다. 팩토리가 싱글톤 또는 프로토 타입의 빈을 리턴하는지에 따라 공유할 수 있는지 정해진다.
- `boolean isSingleton()`: 싱글톤 타입의 빈인경우 `true`를 리턴한다.
- `Class getObjectType()`: 객체의 타입을 리턴한다.

`ApplicationContext`의 `getBean` 메소드에 `FactoryBean`을 알고 싶은 `Bean`의 `id`에 `&`를 붙이면 해당 빈의 `FactoryBean`을 알 수 있다.

# Annotation-based Container Configuration

XML 기반 설정의 대안으로 annotation 기반 설정이 있으며, annotation 기반 설정은 괄호로 선언하는 대신 바이트코드 메타데이터를 이용해 구성 요소를 연결한다.

`BeanPostProcessor`를 이용해 annotation과 결합하는 것은 Spring IoC 컨테이너를 확장하는 일반적인 방법이다.

XML 기반 구성에서 `context`를 이용해 전체 빈에 적용 시킬 수 있다.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config/>

</beans>
```

## `@Required`

`@Required` annotation은 빈 프로퍼티 setter 메소드에 적용이 가능하다

```java
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Required
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // ...
}
```

`@Required`는 bean definition에 명시적 값 정의 또는 autowiring을 통해 구성 시 채워져야 함을 나타낸다. annotation이 포함된 Bean 프로퍼티가 채워지지 않은 경우 컨테이너에서 예외를 발생한다.  나중에 `NullPointerException`이 발생하는 것을 피할 수 있다.

> `@Required` annotation은 Spring Framework 5.1에서 deprecated되었다.

## `@Autowired`

> JSR 330의 `@Inject` annotation을 `@Autowired` 대신에 사용 할 수 있다.

`@Autowired`는 생성자에 적용할 수 있다

```java
public class MovieRecommender {

    private final CustomerPreferenceDao customerPreferenceDao;

    @Autowired
    public MovieRecommender(CustomerPreferenceDao customerPreferenceDao) {
        this.customerPreferenceDao = customerPreferenceDao;
    }

    // ...
}
```

> Spring Framework 4.3부터 생성자가 하나인 경우  `@Autowired` 를 생략 할 수 있다

setter 메서드에 적용 할 수 있다.

```java
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Autowired
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // ...
}
```

여러 인수가 있는 메서드에 적용 할 수 있다.

```java
public class MovieRecommender {

    private MovieCatalog movieCatalog;

    private CustomerPreferenceDao customerPreferenceDao;

    @Autowired
    public void prepare(MovieCatalog movieCatalog,
            CustomerPreferenceDao customerPreferenceDao) {
        this.movieCatalog = movieCatalog;
        this.customerPreferenceDao = customerPreferenceDao;
    }

    // ...
}
```

필드에도 적용할 수 있다

```java
public class MovieRecommender {

    private final CustomerPreferenceDao customerPreferenceDao;

    @Autowired
    private MovieCatalog movieCatalog;

    @Autowired
    public MovieRecommender(CustomerPreferenceDao customerPreferenceDao) {
        this.customerPreferenceDao = customerPreferenceDao;
    }

    // ...
}
```

해당 타입이 여러개인 경우 배열과 Collections에 주입 받을 수 있다.

```java
public class MovieRecommender {

    private Set<MovieCatalog> movieCatalogs;

    @Autowired
    public void setMovieCatalogs(Set<MovieCatalog> movieCatalogs) {
        this.movieCatalogs = movieCatalogs;
    }

    // ...
}
```

> 주입 받는 배열, 리스트를 특정 순서로 정렬하고 싶으면 `Ordered` 인터페이스를 구현하거나 `@Order`, `@Priority` annotation을 이용해 설정 할 수 있다.

`Map`에도 사용할 수 있으며 키 에는 해당 Bean의 이름이 들어가고 value에는 해당 인스턴스가 주입된다.

```java
public class MovieRecommender {

    private Map<String, MovieCatalog> movieCatalogs;

    @Autowired
    public void setMovieCatalogs(Map<String, MovieCatalog> movieCatalogs) {
        this.movieCatalogs = movieCatalogs;
    }

    // ...
}
```

`required`를 `false`로 설정하여 해당 빈이 없는 경우 무시하고 진행 할 수 있다.

```java
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Autowired(required = false)
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // ...
}
```

non-required 의존성을 `Optional`을 이용해 해결 할 수도 있다

```java
public class MovieRecommender {

    @Autowired
    private ApplicationContext context;

    public MovieRecommender() {
    }

    // ...
}
```

> `@Autowired`, `@Inject`, `@Value`, `@Resource` annotation은 Spring의 `BeanPostProcessor` 구현체에 의해 처리된다. 따라서 custom `BeanPostProcessor`나 `BeanFactoryPostProcessor`의 경우에는 `@Bean`을 이용해 등록하거나 XML 구성을 통해 등록해야 한다.

## `@Primary`로 우선권 부여

같은 타입의 빈이 여러게인 경우 `@Primary`로 설정된 빈이 있으면 해당 빈이 의존성 주입 된다.

```java
@Configuration
public class MovieConfiguration {

    @Bean
    @Primary
    public MovieCatalog firstMovieCatalog() { ... }

    @Bean
    public MovieCatalog secondMovieCatalog() { ... }

    // ...
}
```

`firstMovieCatalog`가 주입된다.

```java
public class MovieRecommender {

    @Autowired
    private MovieCatalog movieCatalog;

    // ...
}
```

XML로 다음과 같이 표현 할 수 있다.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config/>

    <bean class="example.SimpleMovieCatalog" primary="true">
        <!-- inject any dependencies required by this bean -->
    </bean>

    <bean class="example.SimpleMovieCatalog">
        <!-- inject any dependencies required by this bean -->
    </bean>

    <bean id="movieRecommender" class="example.MovieRecommender"/>

</beans>
```

## `@Qualifier를 사용하여 특정 빈 지정

`@Qulifier`를 사용하여 빈을 특정한 값에 연관시켜 특정 빈을 선택하도록 할 수 있다.

```java
public class MovieRecommender {

    private MovieCatalog movieCatalog;

    private CustomerPreferenceDao customerPreferenceDao;

    @Autowired
    public void prepare(@Qualifier("main") MovieCatalog movieCatalog,
            CustomerPreferenceDao customerPreferenceDao) {
        this.movieCatalog = movieCatalog;
        this.customerPreferenceDao = customerPreferenceDao;
    }

    // ...
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config/>

    <bean class="example.SimpleMovieCatalog">
        <qualifier value="main"/> 

        <!-- inject any dependencies required by this bean -->
    </bean>

    <bean class="example.SimpleMovieCatalog">
        <qualifier value="action"/> 

        <!-- inject any dependencies required by this bean -->
    </bean>

    <bean id="movieRecommender" class="example.MovieRecommender"/>

</beans>
```

기본적으로 `qualifier` 값은 bean의 이름으로 설정된다. 

> bean name으로 주입 받으려고 하는 경우 `@Qualifier` annotaion이 필요하지 않다. 의존성이 여러게인 상황에서 Spring은 기본적으로 주입 지점 이름(필드명, 등)가 일치하는 것을 의존성 주입 한다.

#### `@Resource`

bean 이름으로 annotation 기반 주입을 하려는 경우 `@Autowired`을 사용하지 말고, JSR-250에 선언된 `@Resource` annotation을 사용해야한다. `@Resource` annotation는 빈의 타입은 보지 않고 이름으로만 빈을 주입한다. `@Autowired`의 경우 먼저 타입일치하는 것을 확인하고 `Spring`의 `qualifier` 값을 사용해 주입을한다.

`@Autowired`의 경우 필드, 생성자, 여러 인수를 가진 메소드에 사용할 수 있지만, `@Resource`는 필드에만 사용 할 수 있다.

#### Custom `qualifier` 

```java
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Genre {

    String value();
}
```

```java
public class MovieRecommender {

    @Autowired
    @Genre("Action")
    private MovieCatalog actionCatalog;

    private MovieCatalog comedyCatalog;

    @Autowired
    public void setComedyCatalog(@Genre("Comedy") MovieCatalog comedyCatalog) {
        this.comedyCatalog = comedyCatalog;
    }

    // ...
}
```

XML 설정에서 `qulifier`의 `type` 프로퍼티를 이용해 custom `qualifier`를 연결 할 수 있다.

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config/>

    <bean class="example.SimpleMovieCatalog">
        <qualifier type="Genre" value="Action"/>
        <!-- inject any dependencies required by this bean -->
    </bean>

    <bean class="example.SimpleMovieCatalog">
        <qualifier type="example.Genre" value="Comedy"/>
        <!-- inject any dependencies required by this bean -->
    </bean>

    <bean id="movieRecommender" class="example.MovieRecommender"/>

</beans>
```

## `CustomAutowireConfigurer` 사용

`CustomAutowireConfigurer`는 `BeanFactoryPostProcessor` 구현체로 custom qualifier annotation을 등록 할 수 있다.

```xml
<bean id="customAutowireConfigurer"
        class="org.springframework.beans.factory.annotation.CustomAutowireConfigurer">
    <property name="customQualifierTypes">
        <set>
            <value>example.CustomQualifier</value>
        </set>
    </property>
</bean>
```

`AutowireCandidateResolver`는 다음을 통해 autowire를 결정한다.

- Bean definition의 `autowire-candidate`값
- `<beans/>`의 `default-autowire-candidate` 값
- `@Qualifier` annotation 또는 `CustomAutowireConfigurer`에 등록된 커스텀 qualifier

## `@Resource`

필드 또는 setter 메소드에서 JSR-250의 `@Resource` annotation을 통한 의존성 주입을 지원한다. 

`@Resource`는 `name` 속성을 가진다. 기본적으로 Spring은 해당 값을 주입 할 bean의 이름으로 해석한다.

```java
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Resource(name="myMovieFinder") 
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }
}
```

`name`속성이 설정되지 않는경우 기본 값은 필드 명 도는 setter 메소드 프로퍼티 이름에서 가져온다.

```java
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Resource
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }
}
```

## `@Value`

`@Value`는 외부 속성을 주입하는데 사용된다.

```java
@Component
public class MovieRecommender {

    private final String catalog;

    public MovieRecommender(@Value("${catalog.name}") String catalog) {
        this.catalog = catalog;
    }
}
```

```java
@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig { }
```

다음과 같이 기본값을 설정 할 수 있다.

```java
@Component
public class MovieRecommender {

    private final String catalog;

    public MovieRecommender(@Value("${catalog.name:defaultCatalog}") String catalog) {
        this.catalog = catalog;
    }
}
```

SpEl을 사용할 수 있다.

```java
@Component
public class MovieRecommender {

    private final String catalog;

    public MovieRecommender(@Value("#{systemProperties['user.catalog'] + 'Catalog' }") String catalog) {
        this.catalog = catalog;
    }
}
```

## `@PostConstruct`와 `@PreDestroy`

`CommonAnnotationBeanPostProcessor`는 `@Resource` 뿐 아니라, `@PostCOnstruct`, `@PreDestroy` 도 지원한다.

```java
public class CachingMovieLister {

    @PostConstruct
    public void populateMovieCache() {
        // populates the movie cache upon initialization...
    }

    @PreDestroy
    public void clearMovieCache() {
        // clears the movie cache upon destruction...
    }
}
```

