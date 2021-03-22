## Spring AOP 기능 및 목표

Spring AOP는 순수 Java로 구현되고 특별한 컴파일 과정이 필요하지 않다. Spring AOP는 클래스 로더 계층을 제어 할 필요가 없으므로 서블릿 컨테이너와 애플리케이션 서버에서 사용하기에 적합하다.

Spring AOP는 현재 메소드 실행 Join Point 만 지원한다 (Spring Bean의 메소드 실행에 대한 Advice).  코어 스프링 AOP API를 구조를 유지하고 필드 인터 셉션에 대한 지원을 추가 할 수 있지만 필드 인터 셉션은 구현되지 않았다. 필드에 접근하고 조인 포인트를 업데이트하는 Advice가 필요한 경우 AspectJ와 같은 언어를 사용해야한다.

AOP에 대한 Spring AOP의 접근 방식은 대부분의 다른 AOP 프레임 워크의 접근 방식과 다르다. Spring AOP의 목표는 가장 완전한 AOP 구현을 제공하는 것이 아니라, AOP 구현과 Spring IoC 간의 긴밀한 통합을 제공하여 엔터프라이즈 애플리케이션의 일반적인 문제를 해결하는 것이다.

Spring Framework의 AOP 기능은 일반적으로 Spring IoC 컨테이너와 함께 사용된다. Aspect는 일반 빈 정의 문법을 사용하여 구성된다. 이것은 다른 AOP 구현과의 중요한 차이점입니다. 매우 세분화 된 객체 (예 도메인 객체)를 Advice하는 것은 Spring AOP로 쉽고 효율적으로 수행 할 수 없다. 이러한 경우 AspectJ가 최선의 선택이다. 하지만 Spring AOP는 AOP를 따르는 엔터프라이즈 Java 애플리케이션의 대부분의 문제에 대한 탁월한 솔루션을 제공한다.

Spring AOP는 포괄적인 AOP 솔루션을 제공하기 위해 AspectJ와 경쟁하지 않는다. Spring AOP와 같은 프록시 기반 프레임워크와 AspectJ와 같은 완전한 프레임워크 모두 가치가 있으며 경쟁보다는 보완 적인 관계이다. Spring은 Spring AOP, IoC, AspectJ를 통합하여 일관된 Spring 기반의 애플리케이션 아키텍처 내에서 AOP의 모든 사용을 가능하게한다.

