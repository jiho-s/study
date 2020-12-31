# IoC Container

## 목차

1. [Spring IoC Container와 Beans](#1.-spring-ioc-container와-beans)
2. [Container 개요](#2.-container-개요)
3. [Bean 개요](#3.-bean-개요)
4. [Dependencies](#4.-dependencies)

# 1. Spring IoC Container와 Beans

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

# 2. Container 개요

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

# 3. Bean 개요

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

# 4. Dependencies

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

#### 내부 Beans

Bean 안에 Bean을 정의 할 수 있다

```

```

