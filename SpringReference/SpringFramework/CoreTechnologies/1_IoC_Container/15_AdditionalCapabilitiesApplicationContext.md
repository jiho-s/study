## Additional Capabilities of the `ApplicationContext`

`ApplicationContext` 인터페이스의 추가된  `org.springframework.context` 는 추가적인 기능을 제공한다.

- `MessageSource` 인터페이스를 통해 i18n 스타일 제공
- `ResourceLoader` 인터페이스를 통해 URL및 파일과 같은 리소스에 접근
- `ApplicationListener` 인터페이스 구현을 통해 이밴트 발행
- `HierachicalBeanFactory` 인터페이스를 통해 애플리케이션을 여러 계층으로 나누어 하나의 계층에 집중

### `MessageSource` 를 사용하여 국제화

`ApplicationContext` 인터페이스는 `MessageSource` 인터페이스를 상속받아 i18n을 기능을 제공한다. Spring은 또한 메세지를 계층적으로 적용할수 있는 `HierachicalMessageSource`를 제공한다. 인터페이스에 정의된 메소드는 다음과 같다.

- `String getMessage(String code, Object[] args, String default, Locale loc)`

  `MessageSource`에서 메시지를 검색하는데 사용하는 기본 방법이다. 지정된 locale에 대한 메시지가 없으면 기본 메시지가 사용된다.  전달된 모든 인수는 `MessageFormat` 을 사용하여 표준 라이브러리에서 제공하는 기능을 사용해 값이 대체된다.

- `String getMessage(String code, Object[] args, Locale loc)`

  이전 메시지와 동일하지만, 기본 메시지를 지정할 수 없다. 메시지를 찾을 수 없으면 `NoSuchMessageException`이 발생한다.

- `String getMessage(MessageSourceResolvable resolvable, Locale locale)`

  앞의 메소드에 사용된 모든 속성이 `MessageSourceResolvable`로 래핑되어있다.

`ApplicationCOntext` 가 로드되면 자동적으로 컨텍스트에 정의된  `MessageSource` 빈을 자동으로 검색한다. 빈의 이름은 `messageSource` 이여야 한다. 이러한 빈이 발견되면 앞의 메소드에 대한 모든 호출이 메시지 소스로 위임된다. 메시지소스가 발견되지 않으면 `ApplicationContext`가 상위 컨텍스트에 포함되어 있는지 찾는다. 메시지 소스를 찾을수 없는 경우 위에 정의된 메서드의 호출을 받을 수 있게 비어있는 `DelegatingMessageSource`  인스턴스를 생성한다.

스프링은 `ResourceBundleMessageSource`, `StaticMessageSource` 두개의 `MessageSource` 구현체를 제공한다. 두 개 모두 중첩 메시징을 수행하기 위해 `HierarchicalMessageSource ` 를 구현하였다. `StaticaMessageSource` 는 거의 사용하지 않지만 소스에 메시지를 추가할 수 있는 프로그래밍 적인 방법을 제공한다. 다음은 `ReSourceBundleMessageSource` 의 예시를 보여준다

```xml
<beans>
    <bean id="messageSource"
            class="org.springframework.context.support.ResourceBundleMessageSource">
        <property name="basenames">
            <list>
                <value>format</value>
                <value>exceptions</value>
                <value>windows</value>
            </list>
        </property>
    </bean>
</beans>
```

위 예제는 `format`, `exceptions`, `windows` 세 개의 리소습 번들이 클래스 패스에 정의되어있다고 가정하고 있다. 메시지 resolving은 `ResourceBundle` 객체를 통해 JDK 표준 메시지 resolving 객체에 의 해 처리된다. 리소스 번들 파일 중 두가 지 내용이 다음과 같다고 가정하자

```properties
		# in format.properties
    message=Alligators rock!
```

```properties
    # in exceptions.properties
    argument.required=The {0} argument is required.
```

다음 예제는 `MessageSource` 가 기능을 수행하는 프로그램을 보여준다. `ApplicationContext`의 구현체도 `MessageSource`의 구현체이므로 `MessageSource`인터페이스로 타입 캐스팅 할 수 있다.

```java
public static void main(String[] args) {
    MessageSource resources = new ClassPathXmlApplicationContext("beans.xml");
    String message = resources.getMessage("message", null, "Default", Locale.ENGLISH);
    System.out.println(message);
}
```

i18n에 관련하여 스프링의 다양한 `MessageSource` 구현체는 JDK 표준과 동일한 로케일 resolution 규칙을 따른다. 즉 영국 로케일을 하고 싶으면 `format_en_GB.properties`, `exceptions_en_GB.properties`, `andwindows_en_GB.properties` 파일을 생성해야 한다.

> `ResourceBundleMessageSource`의 대안으로 스프링은 `ReloadableResourceBundleMessageSource`를 제공한다. `ReloadableResourceBundleMessageSource`는 모든 스프링 리소스 위치에서 파일을 읽을 수 있으며 번들 프로퍼티 파일의 즉시 리로딩을 지원한다.

### Standard and Custom Events

`ApplicationContext`에서 이벤트 처리는 `ApplcationEvent`클래스와 `ApplicationListener`인터페이스를 통하여 제공된다. `ApplicationListener`를 구현한 빈이 컨텍스트에 등록되면, `ApplicationEvent`가 `ApplicationContext`에 게시 될때마다 해당 빈에 알림이 전송된다. 기본적으로 표준 Observer 디자인 패턴이다.

##### 스프링이 제공하는 기본 이벤트

| Event                        | 설명                                                         |
| ---------------------------- | ------------------------------------------------------------ |
| `ContextRefreshedEvent`      | AppicationContext를 초기화 하거나 리프레시 했을때 발생       |
| `ContextStartedEvent`        | ApplicationContext를 start()하여 라이프사이클 빈들이 시작 신호를 받은 시점에 발생. |
| `ContextStoppedEvent`        | ApplicationContext를 stop()하여 라이프사이클 빈들이 정지 신호를 받은 시점에 발생. |
| `ContextClosedEvent`         | ApplicationContext를 close() 하여 싱글톤 빈이 소멸되는 시점에 발생 |
| `RequestHandledEvent`        | HTTP 요청을 처리했을 때 발생                                 |
| `ServletRequestHnadledEvnet` | `RequestHandledEvent`에 서블릿 관련 컨텍스트 정보를 추가     |

다음 예제는 Spring의 `ApplicationEvent` 기본 클래스를 확장한 간단한 클래스를 보여준다.

```java
public class BlockedListEvent extends ApplicationEvent {

    private final String address;
    private final String content;

    public BlockedListEvent(Object source, String address, String content) {
        super(source);
        this.address = address;
        this.content = content;
    }

    // accessor and other methods...
}
```

커스텀 이벤트를 발행하려면 `ApplicationEventPublisher`의 `publishEvent()`메소드를 이용한다. 일반적으로 `ApplicationEventPublisherAware`  인터페이스를 구현한 클래스를 만들고 Spring 빈으로 등록한후 이를 수행한다. 

```java
public class EmailService implements ApplicationEventPublisherAware {

    private List<String> blockedList;
    private ApplicationEventPublisher publisher;

    public void setBlockedList(List<String> blockedList) {
        this.blockedList = blockedList;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void sendEmail(String address, String content) {
        if (blockedList.contains(address)) {
            publisher.publishEvent(new BlockedListEvent(this, address, content));
            return;
        }
        // send email...
    }
}
```

빈을 생성할 떄, 스프링 컨페이너는 `EmailService`가 `ApplicationEventPublisherAware`를 구현 한것을 감지하고 자동으로 `setApplicationEvnetPublisher()`를 호출한다. 실제로 인자로 들어가는 것은 컨테이너 자체로 컨텍스트를 `ApplicationEventPublisher` 인터페이스 형태로 사용할 수 있다.

커스텀 `ApplicationEvent`를 받기위해 `ApplicationListener`를 구현하여 빈에 등록할 수 있다.

```java
public class BlockedListNotifier implements ApplicationListener<BlockedListEvent> {

    private String notificationAddress;

    public void setNotificationAddress(String notificationAddress) {
        this.notificationAddress = notificationAddress;
    }

    public void onApplicationEvent(BlockedListEvent event) {
        // notify appropriate parties via notificationAddress...
    }
}
```

`ApplicationListener`는 커스텀 이벤트 타입으로 매개 변수화 되어 있다. 즉 `onApplicationEvent()`메서드는 다운 캐스팅없이 타입 세이프하게 사용할 수 있다. 원하는 만큼 이벤트 리스너를 등록 할 수 있지만 기본 값으로 이벤트리스너는 이벤트를 동기적으로 처리한다. `publishEvent()`메소드가 모든 메소드가 이벤트 처리를 끝낼때 까지 블록  된다. 이 방법의 장점은 트랜잭션하게 처리할 수 있다. 다른 전략을 선택하고 싶으면 `ApplicationEventMulticaster` 인터페이스와 `SimpleApplicationEventMulticaster` 구현체를 이용해 처리할 수 있다.

다음 예제는 위의 각 클래스를 등록하고 구성하는데 사용되는 Bean definitions를 보여준다.

```xml
<bean id="emailService" class="example.EmailService">
    <property name="blockedList">
        <list>
            <value>known.spammer@example.org</value>
            <value>known.hacker@example.org</value>
            <value>john.doe@example.org</value>
        </list>
    </property>
</bean>

<bean id="blockedListNotifier" class="example.BlockedListNotifier">
    <property name="notificationAddress" value="blockedlist@example.org"/>
</bean>
```

#### Annotation-based Event Listeners

Spring 4.2 부터 `@EventListener` annotation을 이용해 이벤트 리스너를 등록하고 빈을 관리할 수 있다. 

```java
public class BlockedListNotifier {

    private String notificationAddress;

    public void setNotificationAddress(String notificationAddress) {
        this.notificationAddress = notificationAddress;
    }

    @EventListener
    public void processBlockedListEvent(BlockedListEvent event) {
        // notify appropriate parties via notificationAddress...
    }
}
```

메서드가 여러 이벤트를 수신해야 하거나 매개변수 없이 정의하려는 경우 주석 자체에 이벤트 유형을 지정할 수 있다. 

```java
@EventListener({ContextStartedEvent.class, ContextRefreshedEvent.class})
public void handleContextStart() {
    // ...
}
```

특정 이벤트에 대한 메소드를 호출하기 위해 일치하는 SpEL 표현식을 정의하기 위해 `condition` 속성을 이용하여 런타임 필터링을 통해 추가할 수 있다.

```java
@EventListener(condition = "#blEvent.content == 'my-event'")
public void processBlockedListEvent(BlockedListEvent blockedListEvent) {
    // notify appropriate parties via notificationAddress...
}
```

#### Asynchronous Listeners

특정 리스너가 이벤트를 비동기 적으로 처리하려고 하려면 `@Async`를 이용하여 설정할 수 있다.

```java
@EventListener
@Async
public void processBlockedListEvent(BlockedListEvent event) {
    // BlockedListEvent is processed in a separate thread
}
```

- 비동기 이벤트 리스너가 예외를 발생시키는 경우 호출한 곳에 전달되지 않는다. 자세한 내용은 `AsyncUncaughtExceptionHandler`를 참조
- 비동기 이벤트 리스너 메서드는 값을 반환하여 후속 이벤트를 발행 할 수 없다. 처리결과로 다른 이벤트를 발행하는 경우 `ApplicationEventPublisher`를 주입 받아 수동으로 발행해야 한다.

#### Ordering Listeners

하나의 리스너를 다른 리스너보다 먼저 호출해야하는 경우, `@Order` annotation을 이용해 설정할 수 있다.

```java
@EventListener
@Order(42)
public void processBlockedListEvent(BlockedListEvent event) {
    // notify appropriate parties via notificationAddress...
}
```

#### Generic Events

제네릭을 사용하여 이벤트 구조를 추가로 정의할 수 있다. `T`가 실제 생성된 엔티티 타입일때 `EntityCreatedEvent<T>`를 사용할 수 있다.

```java
@EventListener
public void onPersonCreated(EntityCreatedEvent<Person> event) {
    // ...
}
```

`ResolvableTypeProvider`를 구현하여 간단하게 만들수 있다.\

```java
public class EntityCreatedEvent<T> extends ApplicationEvent implements ResolvableTypeProvider {

    public EntityCreatedEvent(T entity) {
        super(entity);
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(getSource()));
    }
}
```

### Low-level Resources에 대한 편리한 접근

Application Context는 `Resource`를 로드할수 있는 `ResourceLoader`이다. `Resource`는 JDK `java.net.URL` 클래스에 기능이 더 추가된 버전이다. `Resource`는 클래스 경로, 파일 시스템 위치, 표준URL로 표현할 수 있는 모든 위치 에서 투명한 방식으로 low-level 리소스를 얻을 수 있다.

### 애플리케이션 시작 추적

`ApplicationContext`는 스프링 애플리케이션의 수명주기를 관리하고 components에 풍부한 프로그래밍 모델을 제공한다. 

특정 메트릭을 사용하여 애플리케이션 시작 단계를 추적하면 시작단계에서 시간이 소모되는 위츠를 파악하고, 컨텍스트의 라이프사이클을 더 잘 이해하는 방법으로도 사용할 수 있다.

다음은 `AnnotationConfigApplicationContext`의 예시이다.

```java
// create a startup step and start recording
StartupStep scanPackages = this.getApplicationStartup().start("spring.context.base-packages.scan");
// add tagging information to the current step
scanPackages.tag("packages", () -> Arrays.toString(basePackages));
// perform the actual phase we're instrumenting
this.scanner.scan(basePackages);
// end the current step
scanPackages.end();
```

