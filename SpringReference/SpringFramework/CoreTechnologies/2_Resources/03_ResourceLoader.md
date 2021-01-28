## `ResourceLoader`

`ResourceLoader`는 해당 인터페이스로 구현된 객채가 `Resource` 인터페이스를 리턴할 수 있는 것을 의미한다.

```java
public interface ResourceLoader {

    Resource getResource(String location);
}
```

모든 application context는 `ResourceLoader` 인터페이스를 구현한다. 그러므로 모든 application context 는 `Resource` 인스턴스를 얻을 수 있다.

`getReousrce()`를 호출하여 지정된 위치 경로에 특정한 접두사가 없으면 특정 application context에 적합한 `Resource` 타입을 반환한다. 

```java
Resource template = ctx.getResource("some/resource/path/myTemplate.txt");

```

`ClassPathXmlApplicationContext`는 `ClassPathReousrce`를 리턴한다. `FileSystemApplicationContext`는 `FileSystemResource` 를 리턴하고 `WebApplicationContext`는 `ServletContextResource`를 리턴한다.

툭정 접두사를 사용하여 applicationContext의 타입에 관계없이 특정 타입의 `Resource` 구현체를 리턴 받을 수 있다.

```java
Resource template = ctx.getResource("classpath:some/resource/path/myTemplate.txt");

```

```java
Resource template = ctx.getResource("file:///some/resource/path/myTemplate.txt");

```

```java
Resource template = ctx.getResource("https://myhost.com/resource/path/myTemplate.txt");

```

