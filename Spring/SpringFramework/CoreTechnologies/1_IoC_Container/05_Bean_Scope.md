## Bean Scope

Bean definition에서 생성된 객체의 스코프를 제어할 수 있다

##### Bean의 스코프

| Scope       | 설명                                         |
| ----------- | -------------------------------------------- |
| singleton   | 기본값, IoC 컨테이너 안에 하나의 객체만 생성 |
| prototype   | 주입될때마다 새로운 객채 생성                |
| request     | 하나의 HTTP 요청에 하나의 빈만 생성          |
| session     | 하나의 Session에 하나의 빈만 생성            |
| application | 하나의 ServletContext에 하나의 빈만 생성     |
| websocket   | 하나의 WebSocket에 하나의 빈 생성            |

### Singleton Scope

공유된 하나의 빈 인스턴스만 관리되며, ID가 같은 빈을 요청하는 모든 요청은 같은 인스턴스가 반환된다

```xml
<bean id="accountService" class="com.something.DefaultAccountService"/>

<!-- the following is equivalent, though redundant (singleton scope is the default) -->
<bean id="accountService" class="com.something.DefaultAccountService" scope="singleton"/>
```

### Prototype Scope

요청이 있을 때마다 새로운 인스턴스를 생성, 일반적으로 Stateful Bean에는 프로토타입 스코프를 설정하고 Stateless Bean에는 싱글톤 스코프를 사용

```xml
<bean id="accountService" class="com.something.DefaultAccountService" scope="prototype"/>
```

#### 싱글톤 빈에 프로토타입 빈 의존성이 있는 경우

싱글톤 빈이 만들어 질때 새로운 프로토타입 빈이 만들어져 의존성 주입된다.

싱글톤 스코프의 빈이 런타임시 프로토타입 스코프 빈의 새 인스턴스를 원한다면 [Method Injection](#메소드-주입)을 이용한다.

### Request, Session, Application, and WebSocket Scope

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

####  Request scope

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

#### Session scope

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

#### Application Scope

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

#### Scoped Beans as Dependencies

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

### Custom Scope

자체 범위를 정의하거나 기존 범위를 재정의 할 수 있다.  `singleton` 과 `prototype` 스코프는 재정의 할 수 없다

#### Custom Scope 만들기

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

#### Custom Scope 사용

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
