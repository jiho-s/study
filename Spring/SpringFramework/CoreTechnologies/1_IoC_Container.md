# IoC Container

## 목차

1. [Spring IoC Container와 Beans](#1.-spring-ioc-container와-beans)
2. [Container 개요](#2.-container-개요)
3. [Bean 개요](#3.-bean-개요)
4. [Dependencies](#dependencies)

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