##  Java Bean Validation

Spring Framework는 [Java Bean Validation](https://beanvalidation.org/) API에 대한 지원을 제공한다.

### Overview of Bean Validation

Bean Validation은 Java 애플리케이션에 대한 제약 조건 선언과 메타 데이터를 통해 일반적인 유효성 검사 방법을 제공한다. 이를 사용하려면 런타임에 적용되는 선언적 validation 제약 조건으로 도메인 모델 속성에 어노테이션을 추가한다. 기본 제공 제약 조건이 있으며 커스텀 제약 조건을 정의 할 수 있다.

두 가지 속성이 있는 간단한 `PersonForm` 모델을 보여주는 다음 예제를 보아라.

```java
public class PersonForm {
    private String name;
    private int age;
}
```

Bean Validation을 사용하면 다음 예제와 같이 제약 조건을 선언 할 수 있다.

```java
public class PersonForm {

    @NotNull
    @Size(max=64)
    private String name;

    @Min(0)
    private int age;
}
```

Bean Validation validator는 선언 된 제약 조건을 기반으로 이 클래스의 인스턴스를 유효성을 검사한다.API에 대한 일반 정보는 [Bean Validation](https://beanvalidation.org/) 을 참조해라 . 특정 제약에 대해서는 [Hibernate Validator](https://hibernate.org/validator/) 문서를 참조해라. Bean validation provider를 Spring Bean으로 설정하는 방법은 아래에 나와있다.

### Configuring a Bean Validation Provider

Spring은 Bean Validation Provider를 Spring Bean으로 부트 스트랩하는 것을 포함하여 Bean Validation API에 대한 완전한 지원을 제공한다. 애플리케이션에 validation이 필요한 어디든 `javax.validation.ValidatorFactory` 또는 `javax.validation.Validator` 를 주입할수 있다.

다음 예제와 같이,  `LocalValidatorFactoryBean` 를 스프링의 기본 Validator로 스프링의 빈으로 설정할 수 있다.

```java
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class AppConfig {

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}
```

이전 예제의 기본 구성은 기본 부트 스트랩 메커니즘을 사용하여 초기화하도록 Bean 유효성 검증을 트리거한다. Hibernate Validator와 같은 Bean Validation 공급자는 클래스 경로에 있을 것으로 예상되며 자동으로 감지된다.

#### Injecting a Validator

`LocalValidatorFactoryBean` 는 스프링의 `org.springframework.validation.Validator` 뿐 아니라, `javax.validation.ValidatorFactory` 와 `javax.validation.Validator`도 구현한다. validation 로직을 호출해야하는 Bean에 이러한 인터페이스 중 하나에 대한 참조를 주입 할 수 있다.

다음 예제와 같이 Bean Validation API로 직접 작업하려는 경우에  `javax.validation.Validator`참조를 주입 할 수 있다.

```java
import javax.validation.Validator;

@Service
public class MyService {

    @Autowired
    private Validator validator;
}
```

다음 예제와 같이 Bean에 Spring Validation API가 필요한 경우에 `org.springframework.validation.Validator` 대한  참조를 주입 할 수 있다.

```java
import org.springframework.validation.Validator;

@Service
public class MyService {

    @Autowired
    private Validator validator;
}
```

#### Configuring Custom Constraints

각 bean validation 제약 조건은 두 부분으로 구성된다.

- 제약조건과 구성 가능한 속성을 선언하는  `@Constraint`  어노테이션
- 제약 조건의 동작을 구현 하는  `javax.validation.ConstraintValidator` 인터페이스의 구현

선언을 구현과 연관시키기 위해 각 `@Constraint`어노테이션은 해당 `ConstraintValidator`구현 클래스를 참조한다. 런타임시에 `ConstraintValidatorFactory`는 Constraint 어노테이션이 도메인 모델을 만났을 때 참조된 구현체를 인스턴스화한다.

기본적으로 `LocalValidatorFactoryBean`는 스프링이 `ConstraintValidator` 인스턴스를 생성하려고 사용하는 `SpringConstraintValidatorFactory`를 설정한다. 이는 다른 스프링 빈처럼 의존성 주입의 이점을 가진 커스텀 `ConstraintValidator`를 사용할 수 있다.

다음은 의존성주입을 위해 스프링을 사용한 커스텀 @`Constraint` 선언과 이와 관련된  `ConstraintValidator` 구현체에 대한 예제이다.

```java
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=MyConstraintValidator.class)
public @interface MyConstraint {
}
```

```java
import javax.validation.ConstraintValidator;

public class MyConstraintValidator implements ConstraintValidator {

    @Autowired;
    private Foo aDependency;

    // ...
}
```

여기서 보듯이 `ConstraintValidator` 구현체는 다른 스프링 빈처럼 `@Autowired`로 의존성을 가질수 있다.

#### Spring-driven Method Validation

`MethodValidationPostProcessor` Bean 정의를 통해 Bean Validation 1.1 (사용자 정의 확장으로 Hibernate Validator 4.3에서도 사용할 수 있음)에서 지원하는 메소드 유효성 검사 기능을 Spring 컨텍스트에 통합 할 수 있다.

```java
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
public class AppConfig {

    @Bean
    public MethodValidationPostProcessor validationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
```

Spring 기반 메서드 유효성 검사를 사용하려면 모든 대상 클래스에 Spring의 `@Validated` 어노테이션을 추가해야한다. 이는 사용할 유효성 검사 그룹을 선택적으로 선언 할 수도 있다. Hibernate Validator 및 Bean Validation 1.1 제공 업체의 설정 세부 사항은 [`MethodValidationPostProcessor`](https://docs.spring.io/spring-framework/docs/5.3.5/javadoc-api/org/springframework/validation/beanvalidation/MethodValidationPostProcessor.html) 를 참조해라.

### Configuring a `DataBinder`

스프링3부터 `DataBinder` 인스턴스는 `Validator`와 함께 설정할 수 있다. 일단 설정되면 `binder.validate()` 호출에 의해서 `Validator`가 실행된다. 유효성 검사 오류는 자동적으로 바인더의 `BindingResult`에 추가된다.

다음 예제는 `DataBinder` 가 타겟 객체에 바인딩후 validation 로직이 프로그램적으로 어떻게 호출되는지 보여준다.

```java
Foo target = new Foo();
DataBinder binder = new DataBinder(target);
binder.setValidator(new FooValidator());

// bind to the target object
binder.bind(propertyValues);

// validate the target object
binder.validate();

// get BindingResult that includes any validation errors
BindingResult results = binder.getBindingResult();
```

또한 `dataBinder.addValidators` 및 `dataBinder.replaceValidators`를 통해 여러 `Validator` 인스턴스를 사용하여 `DataBinder`를 구성할 수도 있다. 이 기능은 글로벌로 구성된 been 검증과, `DataBinder` 인스턴스에 로컬로 구성된 Spring `Validator`를 결합할 때 유용합니다.

### Spring MVC 3 Validation

기본적으로, 클래스 경로에 Bean Validation(예: Hibernate Validator)이 있는 경우 `LocalValidatorFactoryBean`(컨트롤러 메서드 인자에서 `@Valid` 및 `Validated`와 함께 사용할 수 있는)은  글로벌 `Validator`로 등록됩니다.

Java 구성에서 다음 예제와 같이 글로벌   `Validator`인스턴스를 커스텀 할 수 있다.

```java
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public Validator getValidator() {
        // ...
    }
}
```

다음 예제와 같이 로컬로  `Validator` 구현을 등록 할 수도 있다.