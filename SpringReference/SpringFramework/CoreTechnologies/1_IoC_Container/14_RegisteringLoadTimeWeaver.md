## Registering a `LoadTimeWeaver`

`LoadTimeWeaver`는 스프링에서 자바 가상머신에 클래스를 동적으로 변환할 때 사용한다.

`@Configuration` 클래스에 `@EnableLoadTimeWeaving` 을 활성화 하명 사용할 수 있다.

```java
@Configuration
@EnableLoadTimeWeaving
public class AppConfig {
}
```

JPA 클래스 변환을 위해 로드 타임 위빙이 필요한 SPring JPA지원에서 사용한다.

