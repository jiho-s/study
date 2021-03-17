## AOP 개념

AOP의 개념과 용어를 설명해보자. 이 용어들은 Spring에 제한되지 않는다.

- Aspect

  여러 클래스에 걸쳐있는 관심사의 모듈화 Spring AOP에서 Aspect는 일반 클래스 (스키마 기반 접근) 또는 주석이 달린 일반 클래스 `@Aspect`( @AspectJ 스타일 ) 를 사용하여 구현된다

- Join point

  메소드 실행 또는 예외 처리와 같은 프로그램 실행 중 특정 포인트이다. Spring AOP에서 조인 포인트는 항상 메소드 실행을 나타낸다

- Advice

  특정 Join point에서 Aspect가 취한 조치이다. Advice에는 “around”, “before”, “after” 등의 조언이 있다. Spring을 포함한 많은 AOP 프레임 워크는 Advice를 인터셉터로 모델링하고 조인 포인트 주변에 인터셉터 체인을 유지한다.

- Pointcut

  Join point의 상세. Advice는 Pointcut 표현식과 연관되어 있으며 Pointcut과 일치하는 모든 결합 지점에서 실행된다. Pointcut 표현식과 일치하는 join point 개념은 AOP의 핵심이며 Spring은 기본적으로 AspectJ Pointcut 표현식 언어를 사용한다.

- Introduction

  해당 타입을 대신하여 추가 메소드 또는 필드를 선언한다. Spring AOP를 사용하면 조언 된 객체에 새로운 인터페이스 (및 해당 구현)를 도입 할 수 있다.

- Target object

  하나이상의 Aspect에서 Advice 받는 객체이다. Spring AOP는 런타임 프록시를 사용하여 구현되므로이 객체는 항상 프록시 된 객체이다.

- AOP proxy

   Aspect 계약을 구현하기 위해 AOP 프레임 워크에 의해 생성 된 객체이다. Spring Framework에서 AOP 프록시는 JDK 동적 프록시 또는 CGLIB 프록시이다.

- Weaving

  다른 애플리케이션 타입 또는 객체와 Aspect를 연결하여 Advice된 객체를 만든다. 이는 컴파일 타임 (AspectJ 컴파일러 사용),로드 타임 또는 런타임에 수행 할 수 있다. 다른 Java AOP 프레임 워크와 마찬가지로 Spring AOP는 런타임에 위빙을 수행한다.

