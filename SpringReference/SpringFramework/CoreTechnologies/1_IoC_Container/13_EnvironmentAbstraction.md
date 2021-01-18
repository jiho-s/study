## Envivonment Abstraction

`Environment` 인터페이스는 프로파일과 프로퍼티로 컨테이너에 통합 추상화 되어있는 애플리케이션 environment의 주요 형태이다.

프로파일은 지정된 프로파일이 활성화 된 경우에만 컨테이너에 등록되는 명명된, 논리적 그룹의 빈 정의이다. XML로 정의 되든  annotation으로 정의되든 빈은 프로파일에 규정할 수 있다. `Environment` 객체의 프로파일과 관련한 역할은 현재 활성화 된 프로파일과 기본적으로 활성화되어야하는 프로파일을 결정하는 것이다.

프로퍼티는 모든 애플리케이션에서 중요한 역할을 하며, 프로퍼티파일 JVM 시스템 프로퍼티, 시스탬 환경 변수, JNDI, 서블릿 컨텍스트 매개변수 등 다양한 소스에서 올수 있다. `Environment` 객체의 프로퍼티와 관련한 역할은 프로퍼티 소스를 구성하고 프로퍼티를 분석 할 수 있는 편리한 인터페이스를 사용자에게 제공하는 것이다.

### Bean Definition Profiles

빈 정의 프로파일은 다른 environment에서 다른 빈을 등록 할 수 있는 코어 컨테이너의 메커니즘을 제공한다. environment는 사용자마다 다른 의미를 가질 수 있으며, 다음 예시가 도움이 될 것이다.

- QA 또는 프로덕션에서 JNDI의 동일한 데이터 소스를 조회하는데신 개발중에는 인메모리 데이터소스를 사용한다. 
- 애플리케이션을 성능 측정 할때만 모니터링 인프라를 등록한다.
- 고객별 커스텀 빈 등록

태스트 환경에서 `DataSource` 구성

```java
@Bean
public DataSource dataSource() {
    return new EmbeddedDatabaseBuilder()
        .setType(EmbeddedDatabaseType.HSQL)
        .addScript("my-schema.sql")
        .addScript("my-test-data.sql")
        .build();
}
```

QA나 프로덕션 환경에서 설정

```java
@Bean(destroyMethod="")
public DataSource dataSource() throws Exception {
    Context ctx = new InitialContext();
    return (DataSource) ctx.lookup("java:comp/env/jdbc/datasource");
}
```

다음으로 상황 A에서 프로파일을 등록하고 상황 B에서 다른 프로파일을 등록하는 방법을 살펴보자

#### `@Profile`

`@Profile`은 한 개 이상의 지정된 프로파일이 활성화 될 때 해당 구성요소가 등록 해야함을 표시 할 수 있다.

```java
@Configuration
@Profile("development")
public class StandaloneDataConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .addScript("classpath:com/bank/config/sql/schema.sql")
            .addScript("classpath:com/bank/config/sql/test-data.sql")
            .build();
    }
}
```

```java
@Configuration
@Profile("production")
public class JndiDataConfig {

    @Bean(destroyMethod="")
    public DataSource dataSource() throws Exception {
        Context ctx = new InitialContext();
        return (DataSource) ctx.lookup("java:comp/env/jdbc/datasource");
    }
}
```

`@Profile`을 meta-annotation으로 사용할 수 있다.

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Profile("production")
public @interface Production {
}
```

`@Profile`을 메소드 레벨에서  특정 빈 하나만 포함하도록 선언할 수 있다.

```java
@Configuration
public class AppConfig {

    @Bean("dataSource")
    @Profile("development") 
    public DataSource standaloneDataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .addScript("classpath:com/bank/config/sql/schema.sql")
            .addScript("classpath:com/bank/config/sql/test-data.sql")
            .build();
    }

    @Bean("dataSource")
    @Profile("production") 
    public DataSource jndiDataSource() throws Exception {
        Context ctx = new InitialContext();
        return (DataSource) ctx.lookup("java:comp/env/jdbc/datasource");
    }
}
```

#### 프로파일 활성화

프로파일을 활성화하는 가장 간단한 방법은 `ApplicationContext`에서 활성화 하는 것이다.

```java
AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
ctx.getEnvironment().setActiveProfiles("development");
ctx.register(SomeConfig.class, StandaloneDataConfig.class, JndiDataConfig.class);
ctx.refresh();
```

`spring.profiles.active` 속성을 이용해 선언적으로 프로파일을 활성화 할 수도 있다. 

#### 기본 프로파일

기본 프로파일은 기본적으로 활성화된 프로파일을 나타낸다.

```java
@Configuration
@Profile("default")
public class DefaultDataConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .addScript("classpath:com/bank/config/sql/schema.sql")
            .build();
    }
}
```

활성화 된 프로파일이 없으면 `dataSource` 가 생성된다. 다른 프로파일이 활성화 된 경우 기본 프로파일이 적용되지 않는다.

### `PropertySource` Abstraction

스프링의 `Environment` 추상화는 설정 가능한 계층구조의 프로퍼티 소스 검색 작업을 제공한다.

```java
ApplicationContext ctx = new GenericApplicationContext();
Environment env = ctx.getEnvironment();
boolean containsMyProperty = env.containsProperty("my-property");
System.out.println("Does my environment contain the 'my-property' property? " + containsMyProperty);
```

`PropertySource`는 key-value 쌍의 추상화이며, 스프링 `StandardEnvironment`는 두 개의 `PropertySource` 객체로 이루어져 있다. 하나는 JVM 시스템 프로퍼티를 나타내고 다른 하나는 시스템 환경 값을 나타낸다.

`StandardEnvironment`를 사용하고, `my-property` 가 스스템 프로퍼티나 환경 변수에 런타임시 존재하고 있으면, `env.containsProperty("my-property")` 는 true이다.

> ##### `StandardServletEnvironment` 의 계층구조 및 우선순위
>
> 1. ServletConfig 매개변수
> 2. ServletContext 매개변수
> 3. JNDI 환경변수
> 4. JVM 시스템 속성
> 5. JVM 시스템 환경

#### `@PropertySource`

`@PropertySource` 는 스프링 `Environment`에서 `PropertySource`를 추가하는데 대한 선언적이고 편리한 메커니즘을 제공한다.

`testbean.name=myTestBean` key-value 값을 가진 app.properties 파일이 있을 때, `@Configuration` annotation이 붙은 클래스의 `testBean.getName()` 은 `myTestBean` 을 리턴한다.

```java
@Configuration
@PropertySource("classpath:/com/myco/app.properties")
public class AppConfig {

    @Autowired
    Environment env;

    @Bean
    public TestBean testBean() {
        TestBean testBean = new TestBean();
        testBean.setName(env.getProperty("testbean.name"));
        return testBean;
    }
}
```

### Placeholder Resolution in Statements

`Environment` 추상화 Placeholder resolution을 컨테이너 전체에 통합해서 쉽게 라우팅할수 있다.

```xml
<beans>
    <import resource="com/bank/service/${customer}-config.xml"/>
</beans>

```

