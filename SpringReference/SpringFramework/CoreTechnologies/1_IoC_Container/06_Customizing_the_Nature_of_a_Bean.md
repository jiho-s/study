## Customizing the Nature of a Bean

Spring Framework는 빈의 여러 특성을 커스텀 할 수 있는 여러 인터페이스를 제공한다

- Lifecycle Callbacks
- `ApplicationContextAware`과 `BeanNameAware`
- 다른 `Aware` 인터페이스

### Lifecycle Callbacks

`InitializingBean`과 `DisposableBean` 인터페이스를 설정하여 컨네이너가 `afterPropertiesSet()`과 `destroy()`를 실행할때 특정 작업을 수행하게 할 수 있다

> JSR-250 의 `@PostConstruct`, `@PreDestroy`를 사용하여 수행할 수 있다.

Spring Framework는 `BeanPostProcessor` 구현을 사용하여 모든 콜백 인터페이스를 처리한다. Spring이 기본적으로 제공하지 않는 라이프 사이클 콜백이 필요한경우 `BeanPostProcessor`을 구현하여 사용할 수 있다.

#### Initialization Callbacks

`InitializingBean`인터페이스는 컨테이너가 프로퍼티를 설정한 이후 초기화 작업을 수행하게 설정할 수 있다

`InitializingBean`인터페이스는 한개의 메소드만 존재한다

```java
void afterPropertiesSet() throws Exception;
```

을 사용하면 코드가 스프링에 종속되므로 `@PostConstruct` annotaion을 사용하거나 XML기반 설정을 사용해 `init-method`를 사용하는 것이 좋다

```xml
<bean id="exampleInitBean" class="examples.ExampleBean" init-method="init"/>
```

```java
public class ExampleBean {

    public void init() {
        // do some initialization work
    }
}
```

`InitializingBean`을 이용한 예시

```xml
<bean id="exampleInitBean" class="examples.AnotherExampleBean"/>
```

```java
public class AnotherExampleBean implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        // do some initialization work
    }
}

```

#### Destruction Callbacks

`DisposableBean` 인터페이스를 구현하면 빈을 포함하는 컨테이너가 파괴될때 빈이 콜백을 받을 수 있다

`DisposableBean`은 하나의 메소드를 가지고 있다

```java
void destroy() throws Exception;
```

`DisposableBean`은 코드를 스프링에 종속 시키기 때문에 사용하지 않는것이 좋다 `@PreDestroy`주석을 사용하거나 XML 구성시 `destroy-method`를 사용하는 것이 좋다

```xml
<bean id="exampleInitBean" class="examples.ExampleBean" destroy-method="cleanup"/>
```

```java
public class ExampleBean {

    public void cleanup() {
        // do some destruction work (like releasing pooled connections)
    }
}
```

`DisposableBean`을 사용한 예

```xml
<bean id="exampleInitBean" class="examples.AnotherExampleBean"/>
```

```java
public class AnotherExampleBean implements DisposableBean {

    @Override
    public void destroy() {
        // do some destruction work (like releasing pooled connections)
    }
}
```

#### Default Initialization and Destroy Methods

프로젝트 전체에서 Initialization, Destroy Methods를 설정 할수 있다.

##### Initialization 메소드 등록

`init-method="init"`형식으로 등록 할 수 있다. Bean이 생성될때 해당 메소드가 있으면 메소드를 실행한다.

```java
public class DefaultBlogService implements BlogService {

    private BlogDao blogDao;

    public void setBlogDao(BlogDao blogDao) {
        this.blogDao = blogDao;
    }

    // this is (unsurprisingly) the initialization callback method
    public void init() {
        if (this.blogDao == null) {
            throw new IllegalStateException("The [blogDao] property must be set.");
        }
    }
}
```

```xml
<beans default-init-method="init">

    <bean id="blogService" class="com.something.DefaultBlogService">
        <property name="blogDao" ref="blogDao" />
    </bean>

</beans>
```

##### Destroy 메소드 등록

`default-destroy-method`를 사용하여 등록할 수 있다.

#### Lifecycle Mechanisms 결합

##### Bean의 Lifecycle 동작을 제어하는 방법

- `InitializingBean` 및 `DisposableBean` 인터페이스
- Custom `init()`, `destroy()` 메소드
- `@PostConstruct`, `@PreDestroy` annotation

##### 결합하여 사용하는 경우 호출 순서

1. `@PostConstruct`
2. `InitializingBean`에 정의된 `afterPropertiesSet()`
3. custom `init()`

4. `@PreDestroy`
5. `DisposableBean`에 정의된 `destroy()`
6. custom `destroy()`

#### Startup and Shutdown Callbacks

```java
public interface Lifecycle {

    void start();

    void stop();

    boolean isRunning();
}
```

스프링이 관리하는 모든 객체는 `Lifecycle` 인터페이스를 구현 가능하다. `ApplicationContext`가 시작 및 중지 신호를 받으면 context 안에 정의되어 있는 `Lifecycle` 구현체에 이를 전달한다. `LifecycleProcessor` 가 이를 위임 받아 처리한다.

```java
public interface LifecycleProcessor extends Lifecycle {

    void onRefresh();

    void onClose();
}
```

##### SmartLifecycle

시작과 종료 순서가 중요 할 수 있다. 특정 타입의 객체가 다른 타입의 객체보다 먼저 시작되어야하는 경우 `SmartLifecycle`을 이용하여 순서를 지정할 수 있다

```java
public interface Phased {

    int getPhase();
}
```

```java
public interface SmartLifecycle extends Lifecycle, Phased {

    boolean isAutoStartup();

    void stop(Runnable callback);
}
```

`getPhase()`를 이용하여 시작 순서를 정할 수 있다 낮은 순서부터 시작되고 종료시에는 역순으로 종료된다. `SmartLifecycle`을 구현하지 않은 일반적인 객체는 기본값이 0으로 설정된다.

`SmartLifecycle`의 `stop`메소드는 callback을 인자로 받는다. 모든 `SmartLifecycle` 구현체는 

종료가 완료되면 이를 실행해야한다. 이를 이용하여 필요한경우 비동기 종료가 가능하다. `LifecycleProcessor`의 기본 구현체인 `DefaultLifecycleProcessor`를 이용하여 timeout 시간만 다음과 같이 수정할 수 있다. 기본 값은 30초이다.

```xml
<bean id="lifecycleProcessor" class="org.springframework.context.support.DefaultLifecycleProcessor">
    <!-- timeout value in milliseconds -->
    <property name="timeoutPerShutdownPhase" value="10000"/>
</bean>
```

### `ApplicationContextAware`과 `BeanNameAware`

`ApplicationContext`가 `ApplicationContextAware`의 구현체를 생성할때 구현체는 `ApplicationContext`에게 정보를 제공할 수 있다

```java
public interface ApplicationContextAware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
```

빈은 `ApplicationContext`가 빈을 생성할때 `ApplicationContext`인터페이스 또는 하위 인터페이스를 조작할 수 있다. 이를 이용해 다른 빈들의 프로그램 방식을 알수 있다. 그러나 코드가 스프링에 종속되고 IoC 스타일을 따르지 않기 때문에 사용을 피하는 것이 좋다. 또한 `ApplicationContext`의 메소드는 파일 리소스에 접근 이벤트 발생, `MessageSource`에 접근을 제공한다.

다른 방법으로는 Autowiring을 통해 `ApplicationContext`의 정보를 얻을 수 있다

`ApplicationContext`가 `BeanNameAware` 인터페이스의 구현체를 생성 할때 클래스는 객체 정의에서 정의된 이름에 관한 정보를 제공해준다.

```java
public interface BeanNameAware {

    void setBeanName(String name) throws BeansException;
}
```
