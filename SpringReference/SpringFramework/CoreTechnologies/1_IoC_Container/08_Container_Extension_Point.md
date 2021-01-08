## Container Extension Point

특정 통합 인터페이스 구현을 Spring IoC container에 확장 시킬 수 있다.

### `BeanPostProcessor`를 이용한 Bean Custom

`BeanPostProcessor` 인터페이스는 callback method가 있고, 콜백 메서드를 구현 하여 인스턴스화 로직, 의존성 구성 로직 등을 설정 할 수 있다. Spring Container가 인스턴스를 만들고 빈에 대한 구성과 초기화가 끝난후 로직을 추가시키고 싶으면 BeanPostProcessor 구현체를 이용해 추가할 수 있다

여러개의 `BeanPostProcessor` 인스턴스를 구성할수 있으며, `order` 속성을 이용하여 순서를 설정 할 수 있다. `BeanPostProcesser`인스턴스가 `Ordered` 인터페이를 구현 한 경우에만 속성을 설정 할 수 있다.

`BeanPostProcessor`는 두개의 콜백 메소드로 구성되어 있다. 컨테이너에 post-processor로 등록이 되면, 각각의 bean이 컨테이너에 의 해 생성될때 initialization methods 전에 콜백을 받고 initialization methods 후에 콜백을 받는다. Post-processor는 bean 인스턴스에 관련한 모든 것을 할 수 있다. 일반적으로 콜백 인스턴스를 확인하거나 프록시로 빈을 래핑 할 수 있다. 일부 Spring AOP 인프라 클래스는 프록시 랩핑 로직을 제공하기 위해 post-processor를 사용한다.

`@Bean`을 이용해 `BeanPostProcessor`를 선언 할때, 리턴 타입은 `BeanPostProcessor`의 구현체 이거나 또는 `BeanPostProcessor`인터페이스 여야한다. 그렇지 않으면 `ApplicationContext` 가 구성될 때 자동으로 등록 할 수 없다.

> ##### 프로그램 방식으로 `BeanPostProcessor` 인터페이스 등록
>
> `ApplicationContext`자동 검색으로 등록되는 것을 권장하지만, `ConfigurableBeanFactory`의 `addBeanPostProcessor` 메소드를 이용하여 등록할 수 있다. 그러나 `Ordered`를 고려하지 않고 등록이 된다.

##### `BeanPostProcessor` 예시: `RequiredAnnotationBeanPostProcessor`

`BeanPostProcessor` 구현체로 annotation으로 표시된 자바 빈의 프로퍼티의 빈들의 의조선 주입을 보장한다.

### `BeanFactoryPostProcessor`를 이용한 설정 데이터 Custom

`BeanFactoryPostProcessor`는 `BeanPostProcessor`와 비슷하지만 가장큰 차이는 `BeanFactoryPostProcessor`는 빈 설정데이터에서 작동한다. Spring IoC container가 `BeanFactoryPostProcessor`이외의 빈들의 인스턴스를 만들기 전에 `BeanPostProcessor`가 설정 데이터를 읽고 이를 변경할 수 있다.

여러개의 `BeanFactoryPostProcessor`를 설정 할 수 있으며 `Orderd`인터페이스를 구현한 경우 `order`를 이용해 순서를 정할 수 있다.

> 실제 빈 인스턴스를 변경 하려는 경우 `BeanPostProcessor`를 이용해야한다. `BeanFactoryPostProcessor`를 이용해서도 변경 할 수 있지만 컨테이너 표준 라이프 사이클에 위반된다.

`ApplicationContext`는 자동으로 `BeanFactoryPostProcessor` 구현체를 감지하여 등록한다.

> `Bean(Factory)PostProcessor`는 지연초기화를 설정해도 무시된다.

##### BeanFactoryPostProcessor 예시: `PropertySourcePlaceholderConfigurer`

`PropertySourcePlaceholderConfigurer`를 사용하여 Java `Properties` 포맷을 사용하여 별도의 파일에 있는 bean definition의 프로퍼티를 사용할 수 있다. 이를 이용해 데이터베이스 url 같은 환경 변수 설정을 XML 정의 파일 또는 컨테이너 파일 수정없이 변경 할 수 있다.

다음 `DataSource`에는 placeholder 값이 정의 되어 있다.

```xml
<bean class="org.springframework.context.support.PropertySourcesPlaceholderConfigurer">
    <property name="locations" value="classpath:com/something/jdbc.properties"/>
</bean>

<bean id="dataSource" destroy-method="close"
        class="org.apache.commons.dbcp.BasicDataSource">
    <property name="driverClassName" value="${jdbc.driverClassName}"/>
    <property name="url" value="${jdbc.url}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>
```

런타임시 `PropertySourcePlaceholderConfigurer`는 다른 값으로 `DataSource`의 프로퍼티로 변경한다.

```properties
jdbc.driverClassName=org.hsqldb.jdbcDriver
jdbc.url=jdbc:hsqldb:hsql://production:9002
jdbc.username=sa
jdbc.password=root
```

`${jdbc.username}`은 'sa'로 바뀐다.

`PropertySourcePlcaeholderConfigurer`는 `Properties` file에서 프로퍼티를 찾을 뿐아니라, Spring `Environment` 프로퍼티와 자바의 `System` 프로퍼티도 찾는다.

### `FactoryBean`으로 인스턴스 로직 Custom

`FactoryBean`을 사용하면 생성자나 정적 메소드가 아닌 다른 방법으로 생성되는 객체를 스프링 빈으로 사용할 수 있게 된다.

`FactoryBean` 는 세개의 메소드를 제공한다

- `Object getObject()`: 팩토리가 생산하는 객체를 반환한다. 팩토리가 싱글톤 또는 프로토 타입의 빈을 리턴하는지에 따라 공유할 수 있는지 정해진다.
- `boolean isSingleton()`: 싱글톤 타입의 빈인경우 `true`를 리턴한다.
- `Class getObjectType()`: 객체의 타입을 리턴한다.

`ApplicationContext`의 `getBean` 메소드에 `FactoryBean`을 알고 싶은 `Bean`의 `id`에 `&`를 붙이면 해당 빈의 `FactoryBean`을 알 수 있다.