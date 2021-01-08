## Classpath Scanning and Managed Components

이 섹션에서는 classpath를 스캔하여 빈들을 등록하는 방법을 소개한다. 이러한 컴포넌트의 후보는 필터 기존에 일치하는 클래스이며 컨테이너에 등록된 bean definition과 일치한다. 이 방법을 사용하면 빈 등록을 위해 XML을 사용할 필요가 없고, 대신 annotation, AspectJ type expressions, 커스텀 filter 기준을 사용하여 어떤 클래스가 컨테이너에 등록된 bean definition이 될 수 있는지 선택할 수 있다.

### `@Component`와 스테레오 타입 주석

`@Repository` annotation는 레파지토리의 스테레오타입을 만족하는 모든 클래스에 대한 표시이다. 이 표시의 역할 중 하나는 자독으적으로 예외를 변환시킨다.

Spring은 추가 스테레오 티입을 제공하고 다음과 같다. `@Component`, `@Service`, `@Controller`. `@Component`는 모든 스프링 관리 component의 제네릭 스테레오타입이다. `@Repository`, `@Service`, `@Controller`는 `@Component`의 특수한 형태(각각 영속화, 서비스, 표현 계층)이다. 그러므로 `@Coponent`를 component 클래스에 적용하여 사용할 수 있다. 하지만 `@Repository`, `@Service`, `@Controller`를 사용하는 것이 적절한 도구에 의한 처리와 관점과 연관하기에 더 적합하다. 예를 들어 이러한 스테레오 타입들은 적절한 AOP pointcut을 위한 target을 만들어 준다. `@Repository`, `@Service`, `@Controller`는 스프링 나중 버전에서 추가적인 의미를 줄 수도 있다.

### Meta-annotation 사용 및 Composed Annotation

스프링에서 제공하는 많은 annotation은 meta-annotation으로 사용할 수 있다. meta-annotation은 다른 annotation에 적용할 수 있는 annotation이다. 예를 들어 `@Service` annotation은 다음의 meta-annotation으로 이루어져 있다.

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component 
public @interface Service {

    // ...
}
```

meta-annotaions을 결합하여 'composed annotaion'을 만들 수 있다. 예를 들어 `@RestController` 는 `@Controller` 와 `@ResponseBody` 를 합친것이다.

또한 composed annotation은 meta-annotation의 속성을 재정의 할 수 있다. 이는 meta-annotaion 속성의 일부만 사용하고 싶을때 유용하다. 예를들어 스프링의 `@SessionSocpe` 는 scope는 `session`으로 고정 시키고 `proxyMode`는 여전히 바꿀수 있다.

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope(WebApplicationContext.SCOPE_SESSION)
public @interface SessionScope {

    /**
     * Alias for {@link Scope#proxyMode}.
     * <p>Defaults to {@link ScopedProxyMode#TARGET_CLASS}.
     */
    @AliasFor(annotation = Scope.class)
    ScopedProxyMode proxyMode() default ScopedProxyMode.TARGET_CLASS;

}
```

### 클래스 자동 감지와 Bean Definition 등록

Spring은 스테레오 타입 클래스를 자동으로 감지하고 `ApplicationContext`에 `BeanDefinition` 일치하는 인스턴스를 등록한다.

이런 클래스들을 자동감지하고 일치하는 빈들을 등록하기 위해서는 `@Configuration` 클래스에 `@ComponentScan` 에 붙여야하고, `basePackages` 속성은 등록하고자 하는 클래스의 공통 상위 패키지를 적는다

```java
@Configuration
@ComponentScan(basePackages = "org.example")
public class AppConfig  {
    // ...
}
```

XML에서는 다음과 같이 작성한다.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="org.example"/>

</beans>
```

### `Filter`를 사용하여 스캔 커스텀

기본적으로 `@Compnent`,`@Repository`, `@Service`, `@Controller`, `@Configuration`을 사용하거나 `@Component` 를 meta-annotaion으로 사용한 커스텀 어노테이션만 component로 감지된다. 그러나 `@ComponentScan` 의 `includeFilters`나 `excludeFilters` 속성을 사용하여 커스텀 필터를 등록할 수 있다. 각 필터 요소에는 `type` 과 `expression` 속성이 필요하다.

| Filter Type         | Example Expression           | Description                                                  |
| ------------------- | ---------------------------- | ------------------------------------------------------------ |
| annotation(default) | `org.example.SomeAnnotation` | 해당 컴포넌트의 타입 레벨로 meta-annotation으로 존재하거나 존재하는 annotation |
| assignable          | `org.example.SomeClass`      | 해당 컴포넌트로 확장 하거나 구현 할 수 있는 클래스나 인터페이스 |
| aspectj             | `org.example..*Service+`     | 해당 컴포넌트에 매칭되는 AspectJ 타입 표현식                 |
| regex               | `org\.example\.Default.*`    | 해당 컴포넌트의 클래스 이름에 매칭되는 정규표현식            |
| custom              | `org.example.MyTypeFilter`   | `TypeFilter`인터페이스의 커스텀 구현                         |

다음 예제는 `@Repository`를 제외하고 'stub'를 사용하는 예제이다.

```java
@Configuration
@ComponentScan(basePackages = "org.example",
        includeFilters = @Filter(type = FilterType.REGEX, pattern = ".*Stub.*Repository"),
        excludeFilters = @Filter(Repository.class))
public class AppConfig {
    ...
}
```

### Component안에서 Bean 메타데이터 정의

`@Bean` annotion을 사용하여 컴포넌트 안에서 Bean 메타데이터를 정의 할 수 있다.

```java
@Component
public class FactoryMethodComponent {

    @Bean
    @Qualifier("public")
    public TestBean publicInstance() {
        return new TestBean("publicInstance");
    }

    public void doWork() {
        // Component method implementation omitted
    }
}
```

필드와 메소드에 autowirng이 지원되며 `@Bean` 메소드에 autowring이 지원된다.

```java
@Component
public class FactoryMethodComponent {

    private static int i;

    @Bean
    @Qualifier("public")
    public TestBean publicInstance() {
        return new TestBean("publicInstance");
    }

    // use of a custom qualifier and autowiring of method parameters
    @Bean
    protected TestBean protectedInstance(
            @Qualifier("public") TestBean spouse,
            @Value("#{privateInstance.age}") String country) {
        TestBean tb = new TestBean("protectedInstance", 1);
        tb.setSpouse(spouse);
        tb.setCountry(country);
        return tb;
    }

    @Bean
    private TestBean privateInstance() {
        return new TestBean("privateInstance", i++);
    }

    @Bean
    @RequestScope
    public TestBean requestScopedInstance() {
        return new TestBean("requestScopedInstance", 3);
    }
}
```

스프링 컴퍼넌트안에 있는 `@Bean`메소드는 `@Configuration` 클래스의 다른 메소드와 다르게 처리된다.  차이점은 `@Component` 클래스들은 메소드 및 필드의 호출을 가로채기 위해 CGLIB프록시를 사용하지 않는다. CGLIB 프록시는 `@Configuration ` 안에서 `@Bean` 메소드내에서 메소드나 필드를 호출시 다른 빈들을 위한 메타데이터 참조를 만든다.  `@Bean`메소드를 통하여 다른 빈들을 참조할때 이러한 메소드들은 일반적인 자바 시멘틱으로 호출되지  않고 Spring Beans의 프록시와 라이프 사이플을 제공하기 위해 contatiner를 이용한다. 반대로 `@Component`안에서 `@Bean` 메소드 안에서 메소드나 필드 호출은 프록시 없이 자바의 일반적인 시멘틱을 따른다.

### 자동 감지된 컴포넌트 네이밍

스캐닝 프로세스가 컴포넌트를 자동 감지 할때, 빙 이름은 `BeanNameGenerator` 전략에의 해 생성된다. 기본적으로  모든 스프링 스테레오타입 annotation은 `value`를 통하여 bean definition에 이름을 제공한다.

`value` 가 없거나 다른 걸로 감지된 컴포넌트는 기본적으로 소문자로 시작하는 클래스 이름을 사용한다. 다음 예시에서 이름은 각각 `myMovieLister`과 `movieFinderImpl`이다

```java
@Service("myMovieLister")
public class SimpleMovieLister {
    // ...
}
```

```java
@Repository
public class MovieFinderImpl implements MovieFinder {
    // ...
}
```

### 자동 감지된 컴포넌트의 Scope 설정

기본적으로 대부분의 자동 감지된 컴포넌트는 `singleton`이다. `@Scope`를 사용하여 다른 스코프를 사용할 수 있다.

```java
@Scope("prototype")
@Repository
public class MovieFinderImpl implements MovieFinder {
    // ...
}
```

### Annotation으로 Qualifier 메타데이터 설정

컴포넌트 자동감지를 위해 classpath 스캐닝을 사용하는 경우 `@Qualifier`를 사용해 Qualifier 메타데이터를 설정 할 수 있다.

```java
@Component
@Qualifier("Action")
public class ActionMovieCatalog implements MovieCatalog {
    // ...
}
```

