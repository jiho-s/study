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

### Around Advice

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

