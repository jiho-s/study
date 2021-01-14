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

#### 빈 선언

`@Bean` annotation을 메소드에 사용해 빈을 선언할 수 있다. 이 메소드를 사용해 `ApplicationContext` 에 특정한 타입의 메소드 반환값으로 bean definition에 등록할 수 있다. 기본 값으로 bean 이름과 메소드 이름은 동일하다.

```java
@Configuration
public class AppConfig {

    @Bean
    public TransferServiceImpl transferService() {
        return new TransferServiceImpl();
    }
}
```

앞의 예시는 다음 XML 구성과 동일하다.

```java
<beans>
    <bean id="transferService" class="com.acme.TransferServiceImpl"/>
</beans>
```

#### 빈 의존성

`@Bean` annotation 이 붙은 메소드는 임의의 매개변수를 가질수 있는데, 이 때 매개변수는 빈을 빌드하는데 필요한 의존성을 나타낸다.

```java
@Configuration
public class AppConfig {

    @Bean
    public TransferService transferService(AccountRepository accountRepository) {
        return new TransferServiceImpl(accountRepository);
    }
}
```

#### 라이프 사이클 콜백

`@Bean` annotation으로 정의 된 모든 클래스는 라이프 사이클 콜백을 지원하며 JSR-250의 `@PostConstruct` 및 `@PreDestroy` annotation을 사용할 수 있다.

Spring 라이프 사이클 콜백도 지원하며 `InitializingBean` 과 `DisposableBean` 또는 `Lifecycle` 을 구현하여 사용할 수 있다.

`*Aware` 인터페이스도 지원한다.

XML 기반 설정의 `init-method`, `destroy-method`도 `@Bean` annotation의 속성을 이용해 지정할 수 있다.

```java
public class BeanOne {

    public void init() {
        // initialization logic
    }
}

public class BeanTwo {

    public void cleanup() {
        // destruction logic
    }
}

@Configuration
public class AppConfig {

    @Bean(initMethod = "init")
    public BeanOne beanOne() {
        return new BeanOne();
    }

    @Bean(destroyMethod = "cleanup")
    public BeanTwo beanTwo() {
        return new BeanTwo();
    }
}
```

#### 빈 스코프 설정

`@Scope`를 사용해 빈의 스코프를 설정 할 수 있다.

##### `@Scope` annotation 사용

기본 값으로 스코프는 `singleton` 이지만, `@Scope`  annotation을 오버라이딩하여 스코프를 변경할 수 있다.

```java
@Configuration
public class MyConfiguration {

    @Bean
    @Scope("prototype")
    public Encryptor encryptor() {
        // ...
    }
}
```

##### `@Scope` 와 `scoped-proxy`

`scoped-proxy` 를 통해 스코프가 설정된 의존성으로 작업할 떄 펺리한 방법을 제공한다. `@Scope` annotation을 사용하여 Java에서 빈을 구성할 때 `proxyMode` 속성을 사용할 수 있다. 기본 값은 프록시가 없다.

```java
// an HTTP Session-scoped bean exposed as a proxy
@Bean
@SessionScope
public UserPreferences userPreferences() {
    return new UserPreferences();
}

@Bean
public Service userService() {
    UserService service = new SimpleUserService();
    // a reference to the proxied userPreferences bean
    service.setUserPreferences(userPreferences());
    return service;
}
```

#### 빈 이름 커스텀

기본 값으로 빈 이름은 해당 메소드의 이름이다. `@Bean` 의 `name` 속성을 이용해 바꿀수 있다.

```java
@Configuration
public class AppConfig {

    @Bean(name = "myThing")
    public Thing thing() {
        return new Thing();
    }
}
```

#### 빈 Aliasing

빈에대한 별칭을 다음과 같이 설정할 수 있다.

```java
@Configuration
public class AppConfig {

    @Bean({"dataSource", "subsystemA-dataSource", "subsystemB-dataSource"})
    public DataSource dataSource() {
        // instantiate, configure and return DataSource bean...
    }
}
```

#### 빈 설명

`@Description` 을 사용해 빈에 대한 자세한 텍스트 설명을 제공할 수 있다.

```java
@Configuration
public class AppConfig {

    @Bean
    @Description("Provides a basic example of a bean")
    public Thing thing() {
        return new Thing();
    }
}
```

### `@Configuration` annotation 사용

`@Configuration`은 클래스 레벨의 annotation으로  `@Bean` annotation이 붙은 public 메소드를 통해 Bean을 선언한다.

#### 빈 간 의존성 주입

Bean이 다른 빈에 대해 의존성을 가질때, 하나의 Bean메소드가 다른 Bean 메소드를 호출하는 것으로 의존성을 표현할 수 있다.

```java
@Configuration
public class AppConfig {

    @Bean
    public BeanOne beanOne() {
        return new BeanOne(beanTwo());
    }

    @Bean
    public BeanTwo beanTwo() {
        return new BeanTwo();
    }
}
```

#### Lookup Method Injection

메소드 인젝션은 드물게 사용하는 고급 기능이다. 싱글톤 스코프의 빈이 프로토 스코프의 빈에 의존성을 가질떄 유용하다.

```java
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
}
```

자바 코드 기반 구성을 사용하여 `createCommand()` 메소드를 구현한 `CommandManager` 를 만들었다.

```java
@Bean
@Scope("prototype")
public AsyncCommand asyncCommand() {
    AsyncCommand command = new AsyncCommand();
    // inject dependencies here as required
    return command;
}

@Bean
public CommandManager commandManager() {
    // return new anonymous implementation of CommandManager with createCommand()
    // overridden to return a new prototype Command object
    return new CommandManager() {
        protected Command createCommand() {
            return asyncCommand();
        }
    }
}
```

#### Java 기반 빈 구성이 내부적으로 작동하는 방식

`@Bean` 이 붙은 메소드가 두번 호출되는 코드를 살펴보자

```java
@Configuration
public class AppConfig {

    @Bean
    public ClientService clientService1() {
        ClientServiceImpl clientService = new ClientServiceImpl();
        clientService.setClientDao(clientDao());
        return clientService;
    }

    @Bean
    public ClientService clientService2() {
        ClientServiceImpl clientService = new ClientServiceImpl();
        clientService.setClientDao(clientDao());
        return clientService;
    }

    @Bean
    public ClientDao clientDao() {
        return new ClientDaoImpl();
    }
}
```

위 예제에서 `clientDao()` 가 두번 호출되는 것을 볼수 있다. 스프링에 등록된 빈은 싱글톤 스코프를 가지는데 이는 문제가 될수 있다. 스프링에서는 모든 `@Conmfiguration` 클래스는 `CGLIB`를 이용해 하위 메서드는 먼저 컨테이너를 체크하고 빈이 없으면 새로운 빈을 생성한다.

### 자바 기반 구성 작성

 자바 기반 구성을 사용하면 annotation을 사용해 구성의 복잡성을 줄일 수 있다.

#### `@Import` annotation

XML 구성의 `<import/>` 처럼 `@Import` 를 사용해 구성 클래스를 모듈화 할 수 있다.

```java
@Configuration
public class ConfigA {

    @Bean
    public A a() {
        return new A();
    }
}

@Configuration
@Import(ConfigA.class)
public class ConfigB {

    @Bean
    public B b() {
        return new B();
    }
}
```

context를 생성할때 `ConfigB` 만 넣어도 `ConfigA` 도 사용할 수 있다.

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(ConfigB.class);

    // now both beans A and B will be available...
    A a = ctx.getBean(A.class);
    B b = ctx.getBean(B.class);
}
```

#### Import 된 `@Bean` 정의에 대한 의존성 주입

실제 시나리오에서 Bean은 구성시 다른 구성 클래스의 빈에 대해 서로 의존성을 가진다. XML 기반 구성을 사용시 컴파일러가 관여하지 않기 때문에 문제가 되지 않으며 `ref="someBean"` 을 이용해 컨테이너 초기화중에 해결할 수 있다. `@Configuration` 클래스 사용시 Java 컴파일러는 다른 빈에 대한 참조가 자바 문법에 맞아야 한다는 점에서 구성 모델에 제약조건을 적용한다.

해결방법으로 `@Bean` 메소드는 임의의 갯수의 매개변수를 가질수 있는데 이 점을 이용한다.

```java
@Configuration
public class ServiceConfig {

    @Bean
    public TransferService transferService(AccountRepository accountRepository) {
        return new TransferServiceImpl(accountRepository);
    }
}

@Configuration
public class RepositoryConfig {

    @Bean
    public AccountRepository accountRepository(DataSource dataSource) {
        return new JdbcAccountRepository(dataSource);
    }
}

@Configuration
@Import({ServiceConfig.class, RepositoryConfig.class})
public class SystemTestConfig {

    @Bean
    public DataSource dataSource() {
        // return new DataSource
    }
}

public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SystemTestConfig.class);
    // everything wires up across configuration classes...
    TransferService transferService = ctx.getBean(TransferService.class);
    transferService.transfer(100.00, "A123", "C456");
}
```

#### `@Configuration` 클래스와 `@Bean`메소드의 조건부 Include

`@Porfile` annotation을 사용해 특정 프로파일이 활성화 된경우에만 Bean을 활성화 시킬수 있다.

`@Profile` annotation은 `@Conditional`을 구현한 annotation이다. `@Conditional` 은 `Condition` 의 구현체로 `@Bean` 이 등록되기 전에 참고해야한다.

`Condition` 인터페이스의 구현할 때는 `matches()` 매소드를 구현해야하며 `true`, `false` 를 리턴해야한다.

```java
@Override
public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    // Read the @Profile annotation attributes
    MultiValueMap<String, Object> attrs = metadata.getAllAnnotationAttributes(Profile.class.getName());
    if (attrs != null) {
        for (Object value : attrs.get("value")) {
            if (context.getEnvironment().acceptsProfiles(((String[]) value))) {
                return true;
            }
        }
        return false;
    }
    return true;
}
```

#### Java와 XML 구성 결합

Spring의 `@Configuration` 클래스는 XML 기반 구성을 100% 대체 하는 것을 목표로 하지 않닌다. XML namespace는 컨테이너를 구성하는 이상적인 방법으로 남아있다. `@ImportRessource` 를 이용해 XML 파일을 불러올수 있다.

##### XML 중심의 `@Configuration` 클래스 사용

###### `<bean/>` 안에 `@Configuration` 선언

`@Configuration` 클래스는 컨테이너의 빈정의이다. 아래 예제에서는 `AppConfig`라는 `@Configuration` 클래스를 만든다. `<context:annotation-config/>` 가 활성화 되어있기 때문에 컨테이너는 `@Configuration` annotation을 인지한다.

```java
@Configuration
public class AppConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public AccountRepository accountRepository() {
        return new JdbcAccountRepository(dataSource);
    }

    @Bean
    public TransferService transferService() {
        return new TransferService(accountRepository());
    }
}
```

```xml
<beans>
    <!-- enable processing of annotations such as @Autowired and @Configuration -->
    <context:annotation-config/>
    <context:property-placeholder location="classpath:/com/acme/jdbc.properties"/>

    <bean class="com.acme.AppConfig"/>

    <bean class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>
</beans>
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/com/acme/system-test-config.xml");
    TransferService transferService = ctx.getBean(TransferService.class);
    // ...
}
```

###### `<context:component-scan/>` 을 이용한 `@Configuration` 클래스 선택

이 경우 `<context:component-scan/>` 가 `<context:annotation-config/>` 의 일을 하므로 따로 선언할 필요가 없다.

```java
<beans>
    <!-- picks up and registers AppConfig as a bean definition -->
    <context:component-scan base-package="com.acme"/>
    <context:property-placeholder location="classpath:/com/acme/jdbc.properties"/>

    <bean class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>
</beans>
```

##### `@ImportResource` 로 `@Configuration` 클래스 중심의 XML 구성 사용

`@COnfiguration` 클래스가 컨테이너 구성 매커니즘인 애플리케이션에서도 여전히 일부 XML을 사용해 야한다. 이런 상황에서 `@ImportResource` 를 이용해 XML을 사용하고 정ㅇ의할 수 있다. 이렇게 하면 컨테이너 구성에 대한 자바 중심 접근을 달성하고 XML을 최소한으로 사용할 수 있다.

```java
@Configuration
@ImportResource("classpath:/com/acme/properties-config.xml")
public class AppConfig {

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        return new DriverManagerDataSource(url, username, password);
    }
}
```

```xml
properties-config.xml
<beans>
    <context:property-placeholder location="classpath:/com/acme/jdbc.properties"/>
</beans>
```

```properties
jdbc.properties
jdbc.url = jdbc : hsqldb : hsql : // localhost / xdb
jdbc.username = sa
jdbc.password =
```

