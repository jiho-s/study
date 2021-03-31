## `ResourceLoaderAware` 인터페이스

`ResourceLoaderAware` 인터페이스는 `ResourceLoader` 참조를 제공할것 이라 예상되는 컴포넌트를 식별하기위한 콜백 인터페이스이다.

```java
public interface ResourceLoaderAware {

    void setResourceLoader(ResourceLoader resourceLoader);
}
```

클래스가 `ResourceLoaderAware` 를 구현하고 애플리케이션 컨텍스트로 배포될때, 애플리케이션 컨텍스트에 의해 `ResourceLoaderAware` 로 인식된다. 애플리케이션 컨텍스트는 `setResourceLoader(ResourceLoader)` 를 실행하고 애플리케이션 컨텍스트 자신을 인자로 넣어준다.

