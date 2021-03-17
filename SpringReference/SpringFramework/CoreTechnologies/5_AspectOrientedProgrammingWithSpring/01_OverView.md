## Aspect Oriented Programming with Spring

AOP (Aspect-Oriented Programming)는 프로그램 구조에 대한 또 다른 사고 방식을 제공하여 객체 지향 프로그래밍 (OOP)을 보완한다. OOP에서 모듈화의 핵심 단위가 **Class**인것 처럼  AOP에서는 모듈화 단위가 **Aspect** 이다. Aspect는 여러 유형과 객체에 걸친 우려 사항 (예 : 트랜잭션 관리)의 모듈화를 가능하게 한다.

Spring의 핵심 구성 요소 중 하나는 AOP 프레임워크이다. Spring IoC 컨테이너는 AOP에 의존하지 않지만  AOP는 Spring IoC를 보완하여 매우 유능한 미들웨어 솔루션을 제공한다.

> #### AspectJ pointcuts 와 Spring AOP
>
> Spring은 *schema-based approach* 와 @AspectJ annotation style를 사용해 간단하고 강력한 커스텀 Aspect 방법을 제공한다. 이 두 스타일은 모두 완벽하게 정형화 된 방법을 제공하고 AspectJ pointcut 언어를 사용한다

AOP는 Spring Framework에서 다음을 위해 사용된다.

- 선언적 엔터프라이즈 서비스를 제공한다. 대표적인 예시로는 선언적 트렌젝션 관리가 있다
- OOP를 보안하는 AOP를 사용자가 커스텀 Aspects를 구현하여 사용할 수 있다.

