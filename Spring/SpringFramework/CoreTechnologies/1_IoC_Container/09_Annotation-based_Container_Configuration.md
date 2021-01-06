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