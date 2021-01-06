## Bean 개요

Spring IoC Container은 한 개 이상의 Bean을 관리하고 Bean은 Bean 설정에 의해 생성된다

#### BeanDefinition

빈의 정의에는 다음 프로퍼티 포함

- Class
- Name
- Scope
- Constructor arguments
- Properties
- Autowiring mode
- Lazy initialization mode
- Initialization method
- Destruction method

### 빈 이름 설정

XML 기반 구성에서 id, name 속성을 이용해서 설정한다. 이름을 여러개 설정하려는 경우 name에 `,` `;` 를 사용해서 지정할 수 있다

### Bean 인스턴스 생성

container는 요청시 BeanDefinition을 보고 실제 객체를 생성한다

빈은 두 가지 방법 중 하나로 생성할 수 있다

- `new` 로 생성자를 이용해 생성
- `static` 팩토리 메소드를 이용해 생성 

#### 생성자로 인스턴스 생성

생성자를 통해 인스턴스를 생성하는 경우 코드 작성 없이 생성 할 수 있다

Xml 기반 구성을 사용하는 경우 다음과 같이 Bean을 정의 할 수 있다

```xml
<bean id="exampleBean" class="examples.ExampleBean"/>

<bean name="anotherExample" class="examples.ExampleBeanTwo"/>
```

#### Static 팩토리 메서드로 인스턴스 생성

`static` 팩토리 메서드로 인스턴스를 생성하는 경우 `factory-method` 를 이용해 사용할 팩토리 메서드를 지정 할 수 있다

```xml
<bean id="clientService"
    class="examples.ClientService"
    factory-method="createInstance"/>
```

```java
public class ClientService {
    private static ClientService clientService = new ClientService();
    private ClientService() {}

    public static ClientService createInstance() {
        return clientService;
    }
}
```

#### 인스턴스 팩토리 메서드로 인스턴스 생성

기존에 존재하는 Bean에서 비 `static` 팩토리 메서드로 객체를 생성하는 경우 `class` 속성을 사용하지 않고 `factory-bean` 에서 빈 이름을 정한다

```xml
<!-- the factory bean, which contains a method called createInstance() -->
<bean id="serviceLocator" class="examples.DefaultServiceLocator">
    <!-- inject any dependencies required by this locator bean -->
</bean>

<!-- the bean to be created via the factory bean -->
<bean id="clientService"
    factory-bean="serviceLocator"
    factory-method="createClientServiceInstance"/>
```

```java
public class DefaultServiceLocator {

    private static ClientService clientService = new ClientServiceImpl();

    public ClientService createClientServiceInstance() {
        return clientService;
    }
}
```

#### Bean의 런타임 Type

빈의 정의에 type은 초기 reference일 뿐이고 실제 타입과 일치하지 않을 수  있다

런타임 타입을 알기 위해서는 `BeanFactory.getType` 을 이용해 알 수 있다.