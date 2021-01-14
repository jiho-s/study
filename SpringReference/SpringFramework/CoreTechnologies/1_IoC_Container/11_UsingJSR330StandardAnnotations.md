## Using JSR 330 Standard Annotations

Spring 3.0부터 JSR-3330을 이용한 의존성 주입을 지원한다. 이런 annotation은  Spring annotation과 동일한 방법으로 스캔된다. JSR-330을 사용하려면 다음 의존성을 추가해야 한다.

```xml
<dependency>
    <groupId>javax.inject</groupId>
    <artifactId>javax.inject</artifactId>
    <version>1</version>
</dependency>
```

### `@Inject` 와 `@Named`로 의존성 주입

`@Autowired` 대신에 `@Inject` 를 사용할 수 있다.

```java
import javax.inject.Inject;

public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Inject
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    public void listMovies() {
        this.movieFinder.findMovies(...);
        // ...
    }
}
```

`@Autowired` 처럼 필드, 메소드, 생성자 매개변수에 `@Inject`를 사용할 수 있다. `Provider` 로 주입 포인트를 설정해  짧은 스코프를 가진 빈에 접근 또는 다른 빈들의 지연 접근을 할 수 있다.

```java
import javax.inject.Inject;
import javax.inject.Provider;

public class SimpleMovieLister {

    private Provider<MovieFinder> movieFinder;

    @Inject
    public void setMovieFinder(Provider<MovieFinder> movieFinder) {
        this.movieFinder = movieFinder;
    }

    public void listMovies() {
        this.movieFinder.get().findMovies(...);
        // ...
    }
}
```

`qualifer`를 설정하려면 `@Named` 를 사용한다.

```java
import javax.inject.Inject;
import javax.inject.Named;

public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Inject
    public void setMovieFinder(@Named("main") MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // ...
}
```

또한 `@Nullable`과 `Optional`과 같이 사용할 수 있다.

```java
public class SimpleMovieLister {

    @Inject
    public void setMovieFinder(Optional<MovieFinder> movieFinder) {
        // ...
    }
}
```

```java
public class SimpleMovieLister {

    @Inject
    public void setMovieFinder(@Nullable MovieFinder movieFinder) {
        // ...
    }
}
```

### `@Named` 와 `@ManagedBean` 을 `@Component` 대신 사용

`@Component` 대신에 `@Named` 나 `@ManagedBean` 을 사용하여 bean을 등록할 수 있다.

```java
import javax.inject.Inject;
import javax.inject.Named;

@Named("movieListener")  // @ManagedBean("movieListener") could be used as well
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Inject
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // ...
}
```

`@Component` 처럼 이름을 등록하지 않고 사용할 수 있다.

```java
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Inject
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // ...
}
```

> `@Component` 와 달리,  JSR-330의 `@Named` 나 JSR-250의 `@ManagedBean` 을 사용해 커스텀 annotation을 만들 수 없다.

### JSR-330 의 한계

| Spring              | javax.inject.*        | 차이점                                                       |
| ------------------- | --------------------- | ------------------------------------------------------------ |
| @Autowired          | @Inject               | 'required'속성이 없다. 대신에 `Optional` 을 사용할 수 있다.  |
| @Component          | @Named / @ManagedBean | `@Named` 나 `@ManagedBean` 을 이용해 커스텀 annotation을 만들 수 없다. |
| @Scope("singleton") | @Singleton            | JSR-330의 기본 스코프는 prototype이다. 하지만 스프링의 기본값고 일치시키기 위해 JSR-330 빈은 Spring Container에서 기본값으로 `singleton`으로 설정된다. 싱글톤 이외의 스코프를 설정하려면 Spring의 `@Scope` 를 사용해야 한다. |
| @Qualifier          | @Qualifier / @Named   | `javax.inject.qualifier` 은 커스텀 qualifier를 만들기 위한 meta-annotation일 뿐이다. 스프링의 `@Qualifier` 같은 효과를 내려면 `@Named` annotaion을 사용해야 한다. |
| @Value              | -                     |                                                              |
| @Required           | -                     |                                                              |
| @Lazy               | -                     |                                                              |
| ObjectFactory       | Provider              | `Provider` 은 `ObjectFatory` 의 직접적인 대안이다. 스프링의 `@Autowired` 나 생성자, setter와 같이 사용할 수 있다. |

