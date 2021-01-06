## Container 개요

ApplicationContext 구성 정보는 XML, Java Annotaions, Java 코드로 부터 가져올수 있다

### 구성 정보 설정

객체를 인스턴스, 구성, 어셈블 정보 지시 아래와 같은 방법을 제공한다

- XML 기반 구성
- Annotation 기반 구성
- Java code 기반 구성

#### XML 기반 구성

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

### Container 예시

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

#### XML 기반 구성

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

### Container에서 인스턴스 가져오기

`getBean(String name, Class<T> requiredType)` 을 이용해 빈의 인스턴스를 가져 올 수 있다

```java
// create and configure beans
ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");

// retrieve configured instance
PetStoreService service = context.getBean("petStore", PetStoreService.class);

// use configured instance
List<String> userList = service.getUsernameList();
```

