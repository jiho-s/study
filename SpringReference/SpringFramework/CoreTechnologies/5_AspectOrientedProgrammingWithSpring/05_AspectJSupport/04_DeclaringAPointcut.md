### Declaring a Pointcut

Pointcut은 Join point를 결정하므로 Advice 실행시기를 제어 할 수 있다. Spring AOP는 Spring Bean에 대한 메소드 실행 Join Point만 지원하므로 Pointcut은 Spring Bean의 메소드 실행과 일치하는 것으로 생각할 수 있다. Pointcut 선언은 두 부분으로 구성된다. 이름과 매개 변수로 구성된 signature와 관심있는 메소드 실행을 정확히 결정하는 pointcut 표현식이다. @AspectJ annotation 스타일의 AOP에서 pointcut 서명은 일반 메소드 정의에 의해 제공된다. pointcut 표현식은 `@Pointcut` annotation  을 사용하여 표시한다.(pointcut 서명 역할을하는 메서드는 return이  `void` 여야 함)

```java
@Pointcut("execution(* transfer(..))") // the pointcut expression
private void anyOldTransfer() {} // the pointcut signature
```

`@Pointcut` annotation의 값을 형성하는 Pointcut 표현식은 정규 AspectJ 5 Pointcut 표현식이다.

#### 지원되는 Pointcut 지정자

Spring AOP는 Pointcut 표현식에서 사용하기 위해 다음 AspectJ Pointcut designators (PCD)를 지원한다.

- `execution`

  메소드 실행 조인 포인트 매칭용. Spring AOP로 작업 할 때 사용하는 주요 Pointcut 지정자다.

- `within`

  특정 타입(interface, class)에 속하는 가진 join point으로 매칭을 제한한다 (Spring AOP 사용시 매칭되는 타입안에 선언 된 메소드 실행).

- `this`

  빈 reference(Spring AOP 프록시)가 주어진 타입의 인스턴스인  join point(Spring AOP 사용시 메서드 실행)에 대한 매칭으로 제한한다.

- `target`

  대상 객체 (프록시되는 애플리케이션 객체)가 주어진 타입의 인스턴스인  join point(Spring AOP 사용시 메서드 실행)에 대한 매칭으로 제한한다.

- `args`

  인수가 주어진 타입의 인스턴스인  join point(Spring AOP 사용시 메서드 실행)에 대한 매칭으로 제한합니다.

- `@target`

  실행 객체의 클래스가 주어진 타입의 어노테이션을 가지고있는 join point (Spring AOP 사용시 메소드 실행)에 매칭이로 제한한다.

- `@args`

  전달 된 실제 인수의 런타임 유형에 주어진 타입의 어노테이션을 가지고있는 join point (Spring AOP 사용시 메소드 실행)에 대한 매칭으로 제한한다.

- `@within`

  주어진 어노테이션 (Spring AOP를 사용할 때 주어진 어노테이션이있는 타입에서 선언 된 메소드의 실행)이있는 타입 내에서 join point에 대한 매칭으로 제한합니다.

- `@annotation`

  Join point의 주제 (Spring AOP에서 실행되는 메소드)가 주어진 어노테이션을 가지고있는 join point로 매칭을 제한한다.

Spring AOP는 메소드 실행 조인 포인트로만 매칭을 제한하기 때문에 포인트 컷 지정자는 AspectJ 프로그래밍 가이드에서 찾을 수있는 것보다 조금 제공한다. 추가로 AspectJ는 타입 기반 구조를 가지기 때문에 `this` and `target` join point의 실행은 모두 같은 객체의 메소드 실행을 의미한다. 하지만 Spring AOP는 프록시 기반 시스템이기 때문에 프록시 객체 자체(`this`)와 프록시 뒤에있는 대상 객체 (`target`)를 구분한다.

Spring AOP는 또한 `bean` PCD를 지원한다. `bean` 을 사용하면 특정이름으로 지정된 bean또는 집합(와일드 카드 사용시)에 대한 매칭으로 제한 할 수 있다.

```java
bean(idOrNameOfBean)
```

#### Combining Pointcut Expressions

 `&&,` `||`, `!` 를 사용하여 Pointcut expression을 결합할 수 있다.  Pointcut expression을 이름으로 참조 할 수도 있다.

```java
@Pointcut("execution(public * *(..))")
private void anyPublicOperation() {} 

@Pointcut("within(com.xyz.myapp.trading..*)")
private void inTrading() {} 

@Pointcut("anyPublicOperation() && inTrading()")
private void tradingOperation() {}
```

####  Sharing Common Pointcut Definitions

엔터프라이즈 애플리케이션으로 작업 할 때 개발자는 종종 여러 측면에서 애플리케이션의 모듈과 특정 작업 집합을 참조할수 있다. 