### Declaring an Aspect

@AspectJ 지원이 활성화되면 `@Aspect`어노테이션이 포함되있는 애플리케이션 컨텍스트에 정의 된 모든 Bean 이 Spring에서 자동으로 감지되어 Spring AOP를 구성하는데 사용된다.

다음 두 예는 최소한의 정의를 보여준다.

`@Aspect`어노테이션 이있는 Bean 클래스를 가리키는 애플리케이션 컨텍스트의 일반 Bean 정의를 보여준다.

```xml
<bean id="myAspect" class="org.xyz.NotVeryUsefulAspect">
    <!-- configure properties of the aspect here -->
</bean>
```

`NotVeryUsefulAspect`는 어노테이션으로 `org.aspectj.lang.annotation.Aspect`어노테이션 이 있는 클래스 정의를 보여준다.

```java
package org.xyz;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class NotVeryUsefulAspect {

}
```

