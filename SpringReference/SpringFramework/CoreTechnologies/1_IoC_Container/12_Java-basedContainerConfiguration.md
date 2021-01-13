## Java-based Container Configuration

이 섹션에서는 Java 코드에 주석을 사용하여 Spring Container를 구성하는 방법을 다룬다.

### 기본 개념: `@Bean`과 `@Configuration`

Spring에서 Java-based Configuration의 핵심은 `@Configuration` 이 붙은 class와 `@Bean` 이 붙은  method이다.

`@Bean` annotation은 메소드가 Spring IoC container가 관리하는 객체를 만들고, 구성하고, 초기화하는 메소드를 나타낸다. XML 기반 구성의 `<bean/>` 과 같은 일을 한다. `@Bean` 을 사용한 메소드는 `@Component` annotation이 붙은 모든 class에서 사용할 수 있지만, `@Configuration` annotation이 붙은 class와 많이 사용된다.

`@Configuration`이 붙은 class는 해당 클래스가 bean definition을 위해 사용되었음을 나타낸다. 또한 `@Bean` annotation이 붙은 메소드끼리 다른 메소드를 호출하여 의존성을 설정할 수 있다.

```java
@Configuration
public class AppConfig {

    @Bean
    public MyService myService() {
        return new MyServiceImpl();
    }
}
```

### `AnnotationConfigApplicationContext` 를 사용한 Spring Container 인스턴스 생성

`AnnotationConfigApplicationContext` 는 `ApplicationContext` 의 구현체로 `@Configuration` class를 입력으로 사용할 뿐아니라, `@Component` 클래스와 JSR-330 annotation으로 선언된 클래스도 사용한다.

`@Configuration` 이 입력값으로 오면, `@Configuration` 클래스가 bean definition으로 등록되고 클래스 안에 있는 `@Bean` 이 붙은 메소드가 빈으로 등록된다.

`@Component` 나 JSR-330 클래스가 제공되면 빈으로 등록되고 필요한곳에 의존성 주입이 된다.

#### Simple Construction

`@Configuration` 클래스를 `AnnotationConfigApplicationContext` 의 입력 값으로 사용할 수 있다.

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
    MyService myService = ctx.getBean(MyService.class);
    myService.doStuff();
}
```

`@Configuration` 클래스 뿐아니라, `@Component` 클래스도 입력값으로 사용할 수 있다.

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(MyServiceImpl.class, Dependency1.class, Dependency2.class);
    MyService myService = ctx.getBean(MyService.class);
    myService.doStuff();
}
```

#### `regeister(Class<?>...)` 를 사용한 빌드

`regeister()` 메소드를 사용하여 클래스를 등록 할 수 있다.

```java
public static void main(String[] args) {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.register(AppConfig.class, OtherConfig.class);
    ctx.register(AdditionalConfig.class);
    ctx.refresh();
    MyService myService = ctx.getBean(MyService.class);
    myService.doStuff();
}
```

#### `scan(String...)`으로 컴포넌트 스캔 활성화

`@Configuration` 클래스에 `@ComponentScan` annotation을 사용하여 컴포넌트 스캔을 활성화 할 수 있다.

```java
@Configuration
@ComponentScan(basePackages = "com.acme") 
public class AppConfig  {
    ...
}
```

앞의 예제는 `com.acme` 패키지 안에 있는 `@Component` annotation이 붙은 클래스를 찾기 위해 붙인다. `AnnotationConfigApplicationContext` 의 `scan()` 메소드를 이용해 설정할 수도 있다.

```java
public static void main(String[] args) {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.scan("com.acme");
    ctx.refresh();
    MyService myService = ctx.getBean(MyService.class);
}
```

#### `AnnotationConfigWebApplicationContext` 의 Web Application 지원

`WebApplicationContext` 의 변형인 `AnnotationConfigApplicationContext`는 `AnnotationConfigWebApplicationContext`와 사용할 수 있다.  Spring `ContextLoaderListenr` 서블릿 리스너를 구성하거나, Spring MVC `DispatcherServlet`을 구성할 때 이 구현체를 사용할 수 있다.

```xml
<web-app>
    <!-- Configure ContextLoaderListener to use AnnotationConfigWebApplicationContext
        instead of the default XmlWebApplicationContext -->
    <context-param>
        <param-name>contextClass</param-name>
        <param-value>
            org.springframework.web.context.support.AnnotationConfigWebApplicationContext
        </param-value>
    </context-param>

    <!-- Configuration locations must consist of one or more comma- or space-delimited
        fully-qualified @Configuration classes. Fully-qualified packages may also be
        specified for component-scanning -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>com.acme.AppConfig</param-value>
    </context-param>

    <!-- Bootstrap the root application context as usual using ContextLoaderListener -->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <!-- Declare a Spring MVC DispatcherServlet as usual -->
    <servlet>
        <servlet-name>dispatcher</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!-- Configure DispatcherServlet to use AnnotationConfigWebApplicationContext
            instead of the default XmlWebApplicationContext -->
        <init-param>
            <param-name>contextClass</param-name>
            <param-value>
                org.springframework.web.context.support.AnnotationConfigWebApplicationContext
            </param-value>
        </init-param>
        <!-- Again, config locations must consist of one or more comma- or space-delimited
            and fully-qualified @Configuration classes -->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>com.acme.web.MvcConfig</param-value>
        </init-param>
    </servlet>

    <!-- map all requests for /app/* to the dispatcher servlet -->
    <servlet-mapping>
        <servlet-name>dispatcher</servlet-name>
        <url-pattern>/app/*</url-pattern>
    </servlet-mapping>
</web-app>
```

### `@Bean` Annotation 사용

`@Bean`은 매소드 레벨 annotation이며 XML 구성의 `<bean/>`과 유사하다.

`@Configuration` annotation이 붙은 클래스나 `@Component` annotation이 붙은 클래스 안에 사용할 수 있다

