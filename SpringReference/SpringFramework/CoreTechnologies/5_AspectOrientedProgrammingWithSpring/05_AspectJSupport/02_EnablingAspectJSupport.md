###  Enabling @AspectJ Support

Spring 구성으로 `@AspectJ` 를 Aspect로 사용하려면, @AspectJ와 Aspect가 Advice가 있든 없든 auto-proxying bean을 기반으로Spring AOP를 구성하려면 Spring 지원을 활성화해야 한다. auto-proxying이란 Spring이 Bean이 하나 이상의 Aspect에서 Advice를 받는다고 판단하면 해당 Bean에 대한 프록시를 자동으로 생성하여 메소드 호출을 가로채고 Advice 필요에 따라 실행되도록 보장한다.

@AspectJ 지원은 XML 또는 Java 스타일 구성으로 활성화 할 수 있습니다. 두 경우 모두 AspectJ의 `aspectjweaver.jar`라이브러리가 애플리케이션의 클래스 경로에 있는지 확인해야한다.

#### Java Configuration으로 @AspectJ 지원 활성화

Java `@Configuration`에서 @AspectJ 지원을 활성화하려면 다음 예제와 같이 `@EnableAspectJAutoProxy` annotation을 추가한다.

```java
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {

}
```

#### XML Configuration으로 @AspectJ 지원 활성화

XML 기반 구성에서 @AspectJ 지원을 활성화하려면 다음 예제와 같이 `aop:aspectj-autoproxy`  요소를 사용한다.

```xml
<aop:aspectj-autoproxy/>
```

