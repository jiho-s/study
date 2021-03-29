### Declaring Advice

Advice는 pointcut 표현식과 연관되어 있으며 pointcut과 일치하는 메소드 실행 전, 후, 감싸서 동작한다. Advice에 있는 pointcut 표현식은 명명 된 pointcut에 대한 간단한 참조이거나 선언 된 pointcut 표현식 그 자체 일 수 있다. 

#### Before Advice

`@Before`어노테이션 을 사용하여 aspect로 Before Advice를 선언 할 수 있다.

```java
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class BeforeExample {

    @Before("com.xyz.myapp.CommonPointcuts.dataAccessOperation()")
    public void doAccessCheck() {
        // ...
    }
}
```

pointcut expression을 바로 안에 사용하여 다음과 같이 표현할 수 있다

```java
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class BeforeExample {

    @Before("execution(* com.xyz.myapp.dao.*.*(..))")
    public void doAccessCheck() {
        // ...
    }
}
```

#### After Returning Advice

After Returning Advice는 매칭되는 메소드 실행이 정상적으로 반환된경우 동작한다. `@AfterReturning` annotation을 사용하여 선언 할 수 있다.

```java
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;

@Aspect
public class AfterReturningExample {

    @AfterReturning("com.xyz.myapp.CommonPointcuts.dataAccessOperation()")
    public void doAccessCheck() {
        // ...
    }
}
```

때로는 반환 된 실제 값을 Advice 본문에서 접근해야하는 경우,  `@AfterReturnin`를 사용하여 다음 예제와 같이 반환 값을 바인딩 하는 형식으로 접근할 수 있다.

```java
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;

@Aspect
public class AfterReturningExample {

    @AfterReturning(
        pointcut="com.xyz.myapp.CommonPointcuts.dataAccessOperation()",
        returning="retVal")
    public void doAccessCheck(Object retVal) {
        // ...
    }
}
```

`returning`속성에 사용 된 이름은 advice 메소드의 매개 변수 이름과 일치해야한다. 메서드 실행이 반환되면 반환 값이 해당 인수 값으로 advice 메서드에 전달됩니다. 또한 `returning`절은 지정된 유형 (이 경우 `Object`) 의 값을 반환하는 메서드 실행으로 만 매칭을 제한한다 .

After Returning Advice 사용시 완전히 다른 참조를 반환하는 것은 불가능합니다.

#### After Throwing Advice

After Throwing Advice는 매칭되는 메소드 실행에 예외가 발생한 경우 동작한다. `@AfterThrowing` annotation을 사용하여 선언 할 수 있다.

```java
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterThrowing;

@Aspect
public class AfterThrowingExample {

    @AfterThrowing("com.xyz.myapp.CommonPointcuts.dataAccessOperation()")
    public void doRecoveryActions() {
        // ...
    }
}
```

종종 특정 타입의 예외가 던져 질 때만 Advice가 실행되기를 원하며 Advice 본문에서 던져진 예외에 접근해야하는 경우도 있다. `throwing`속성을 사용하여 매칭를 제한하고 (원하는 경우 `Throwable` 예외 유형으로 사용 ) throw 된 예외를 advice 매개 변수에 바인딩 할 수 있다.

```java
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterThrowing;

@Aspect
public class AfterThrowingExample {

    @AfterThrowing(
        pointcut="com.xyz.myapp.CommonPointcuts.dataAccessOperation()",
        throwing="ex")
    public void doRecoveryActions(DataAccessException ex) {
        // ...
    }
}
```

`throwing`속성에 사용 된 이름은 advice 메소드의 매개 변수 이름과 일치해야한다. 예외가 발생해 메서드 실행이 종료되면 예외가 해당 인수 값으로 Advice 메서드에 전달됩니다. 또한 `throwing`절은 지정된 타입 ( 이 경우 `DataAccessException`) 의 예외를 발생시키는 메서드 실행으로 만 매칭을 제한한다.

#### After (Finally) Advice

After (finally) advice는 매칭되는 메소드의 실행이 종료될때 발생한다 `@After` annotation을 사용하여 선언할 수 있다. After (finally) advice는 정상 및 예외 반환 조건을 모두 처리 할 수 있도록 준비해야한다. 일반적으로 리소스 해제 및 유사한 목적으로 사용된다.

```java
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.After;

@Aspect
public class AfterFinallyExample {

    @After("com.xyz.myapp.CommonPointcuts.dataAccessOperation()")
    public void doReleaseLock() {
        // ...
    }
}
```

#### Around Advice

마지막 종류의 Advice는 Around Advice이다.  Around advice는 매칭 된 메소드의 실행을 "주위"로 동작한다. 메서드 실행 전후에 작업을 수행하고 메서드가 실제로 실행되는시기, 방법 및 여부를 결정할 수있다. Around advice는 스레드로부터 안전한 방식으로 메소드 실행 전후의 상태를 공유해야하는 경우에 자주 사용된다. (예 : 타이머 시작 및 중지). 가장 강력하지 않은 형태의 Advice를 사용해야한다.

`@Around`어노테이션 을 사용하여 Around advice를 선언한다 . advice 메서드의 첫 번째 매개 변수는 `ProceedingJoinPoint`타입이어야한다 . Advice의 내부에서,  `ProceedingJoinPoint` 의 `proceed()` 메소드는 기존의 매소드를 실행시킬수 있다.  

```java
자바Kotlin
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;

@Aspect
public class AroundExample {

    @Around("com.xyz.myapp.CommonPointcuts.businessService()")
    public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
        // start stopwatch
        Object retVal = pjp.proceed();
        // stop stopwatch
        return retVal;
  
```

주위 어드바이스에 의해 반환 된 값은 메서드 호출자가 보는 반환 값이다. 예를 들어 간단한 캐싱 측면은 캐시에 값이있는 경우 캐시에서 값을 반환하고 그렇지 않은 경우  `proceed()`를 호출 할 수 있다. 참고로 `proceed`는 Around Adivce 내에서 여러 번 호출할수 있다.

#### Advice Parameters

Spring은 완전히 형식화 된 어드바이스를 제공합니다. 즉, 필요한 Advice 시그니처 매개 변수를 `Object[]`보다 매개변수들로 선언한다.

##### **Access to the Current** `JoinPoint`

모든 advice 메서드는 첫 번째 매개 변수로  `org.aspectj.lang.JoinPoint`  타입의 매개 변수로 선언 할 수 있다.  `JoinPoint`인터페이스는 여러 가지 유용한 메소드를 제공한다

- `getArgs()`: 메서드 인수를 반환합니다.
- `getThis()`: 프록시 개체를 반환합니다.
- `getTarget()`: 대상 객체를 반환합니다.
- `getSignature()`: 권장되는 메서드에 대한 설명을 반환합니다.
- `toString()`: 권장되는 방법에 대한 유용한 설명을 인쇄합니다.

##### Passing Parameters to Advice

인수 값을 advice body에서 사용하려면, `args` 를 사용해 바인딩할 수 있다. 

```java
@Before("com.xyz.myapp.CommonPointcuts.dataAccessOperation() && args(account,..)")
public void validateAccount(Account account) {
    // ...
}
```

`args(account,..)`pointcut 표현 의 일부는 두 가지 용도로 사용된다. 첫째, 메서드가 하나 이상의 매개 변수를 사용하고 해당 매개 변수에 전달 된 인수의 인스턴스가 `Account`인 메서드로 일치를 제한한다. 둘째,  `account ` 매개 변수를 통해 Advice에 실제  `Account` 객체를 제공한다.

이것을 작성하는 또 다른 방법은 조인 포인트와 일치 할 때  `Account`  객체 값 을 "제공"하는 포인트 컷을 선언 한 다음 어드바이스에서 명명 된 포인트 컷을 참조하는 것이다.

```java
@Pointcut("com.xyz.myapp.CommonPointcuts.dataAccessOperation() && args(account,..)")
private void accountDataAccessOperation(Account account) {}

@Before("accountDataAccessOperation(account)")
public void validateAccount(Account account) {
    // ...
}
```

프록시 객체 ( `this`), 대상 객체 ( `target`) 및 annotation 들과 ( `@within`, `@target`, `@annotation`,과 `@args`) 모두 비슷한 방식으로 결합 될 수있다.

##### Advice Parameters and Generics

Spring AOP는 클래스 선언 및 메소드 매개 변수에 사용되는 제네릭을 처리 할 수 있다.

```java
public interface Sample<T> {
    void sampleGenericMethod(T param);
    void sampleGenericCollectionMethod(Collection<T> param);
}
```

 가로 채려는 메소드의 매개 변수 타입을 advice 매개 변수로 입력하여 메소드 타입의 가로 채기를 특정 매개 변수 타입으로 제한 할 수 있다.

```java
@Before("execution(* ..Sample+.sampleGenericMethod(*)) && args(param)")
public void beforeSampleMethod(MyType param) {
    // Advice implementation
}
```

컬렉션에는 아래와 같이 적용할 수 없다

```java
@Before("execution(* ..Sample+.sampleGenericCollectionMethod(*)) && args(param)")
public void beforeSampleMethod(Collection<MyType> param) {
    // Advice implementation
}
```

이와 비슷한 결과를 얻으려면 매개 변수를 `Collection<?>`입력하고 내부를 수동으로 확인해야한다.

##### Determining Argument Names

어드바이스 호출의 매개 변수 바인딩은 어드바이스 및 포인트 컷 메소드 시그니처에서 선언 된 매개 변수 이름과 포인트 컷 표현식에 사용 된 이름을 일치시키는 것에 의존한다. 매개 변수 이름은 Java 리플렉션을 통해 사용할 수 없으므로 Spring AOP는 다음 전략을 사용하여 매개 변수 이름을 결정합니다.

- 사용자가 매개 변수 이름을 명시 적으로 지정한 경우 지정된 매개 변수 이름이 사용된다. 어드바이스와 포인트 컷 어노테이션에는 메소드의 인수 이름을 지정하는 데 사용할 수 있는 `argNames` 선택적 속성이 있습니다. 이러한 인수 이름은 런타임에 사용할 수 있다. 

  ```java
  @Before(value="com.xyz.lib.Pointcuts.anyPublicMethod() && target(bean) && @annotation(auditable)",
          argNames="bean,auditable")
  public void audit(Object bean, Auditable auditable) {
      AuditCode code = auditable.value();
      // ... use code and bean
  }
  ```

첫 번째 매개 변수가  `JoinPoint`, `ProceedingJoinPoint`또는 `JoinPoint.StaticPart` 타입인 경우, `argNames`속성 값에서 매개 변수의 이름을 생략할 수 있다 . 예를 들어, 조인 포인트 객체를 받기 위해 앞의 Advice를 수정하는 경우 `argNames`속성에 이를 포함 할 필요가 없습니다.

```java
@Before(value="com.xyz.lib.Pointcuts.anyPublicMethod() && target(bean) && @annotation(auditable)",
        argNames="bean,auditable")
public void audit(JoinPoint jp, Object bean, Auditable auditable) {
    AuditCode code = auditable.value();
    // ... use code, bean, and jp
}
```

 Advice는 `argNames`속성을 선언 할 필요가 없다.

```java
@Before("com.xyz.lib.Pointcuts.anyPublicMethod()")
public void audit(JoinPoint jp) {
    // ... use jp
}
```

`'argNames'`속성을 사용하는 것은 약간 불편하므로 `'argNames'`속성이 지정되지 않은 경우 Spring AOP는 클래스에 대한 디버그 정보를보고 지역 변수 테이블에서 매개 변수 이름을 결정하려고합니다. 이 정보는 클래스가 디버그 정보 ( `'-g:vars'`) 로 컴파일 된 한 존재한다.

- 필요한 디버그 정보없이 코드가 컴파일 된 경우 Spring AOP는 매개 변수에 대한 바인딩 변수의 쌍을 추론하려고한다. 사용 가능한 정보를 고려할 때 변수 바인딩이 모호하면 an `AmbiguousBindingException`이 발생한다.
- 위의 모든 전략이 실패하면 an `IllegalArgumentException`이 발생한다.

##### Proceeding with Arguments

advice 시그니처가 각 메소드 매개 변수를 순서대로 바인딩해야 한다.

```java
@Around("execution(List<Account> find*(..)) && " +
        "com.xyz.myapp.CommonPointcuts.inDataAccessLayer() && " +
        "args(accountHolderNamePattern)")
public Object preProcessQueryPattern(ProceedingJoinPoint pjp,
        String accountHolderNamePattern) throws Throwable {
    String newPattern = preProcess(accountHolderNamePattern);
    return pjp.proceed(new Object[] {newPattern});
}
```

#### Advice Ordering

 Spring AOP는 어드바이스 실행 순서를 결정하기 위해 AspectJ와 동일한 우선 순위 규칙을 따른다. 우선 순위가 가장 높은 어드바이스가 먼저 "진입 중"으로 실행된다 (두 개의 Before 어드바이스가 주어지면 우선 순위가 가장 높은 어드바이스가 먼저 실행). 조인 지점에서 "출발 할 때"우선 순위가 가장 높은 어드바이스가 마지막에 실행된다 (두 개의 After 어드바이스가 주어지면 우선 순위가 가장 높은 어드바이스가 두 번째로 실행).

다른 Aspect에서 정의 된 두 개의 어드바이스가 모두 동일한 조인 포인트에서 실행되어야하는 경우, 달리 지정하지 않는 한 실행 순서가 정의되지 않는다. 우선 순위를 지정하여 실행 순서를 제어 하려면,  aspect 클래스에서 `org.springframework.core.Ordered` 인터페이스를 구현 하거나  `@Order` annotation을 달아 설정할 수 있다 . 더 낮은 값 `Ordered.getOrder()`(또는 주석 값)을 반환하는 Aspect가 더 높은 우선 순위를 갖는다.