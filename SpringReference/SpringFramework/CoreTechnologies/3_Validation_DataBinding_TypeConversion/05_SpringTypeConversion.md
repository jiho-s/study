## Spring Type Conversion

스프링 3는 일반적인 타입변환 시스템을 위해 `core.convert` 패키지를 도입했다. 타입변환 시스템은 타입변환 로직을 구현하는 SPI와 런타임시에 타입변환을 실행하는 API를 정의한다. 스프링 컨테이너에서 구체적인 빈 프로퍼티값을 필요한 프로퍼티 타입으로 변환하는 프로퍼티 에디터 대신에 타입변환 시스템을 사용할 수 있다. 어플리케이션내에서 타입변환이 필요한 어디서든 퍼블릭 API를 사용할 수 있다.

### Converter SPI

타입 변환 로직을 구현하는 SPI는 다음 인터페이스 정의에 표시된 것처럼 간단하고 강력한 형식이다.

```java
package org.springframework.core.convert.converter;

public interface Converter<S, T> {

    T convert(S source);
}
```

자신만의 Converter를 만들려면 위의 인터페이스를 구현하면 된다. 파라미터 S는 변환되기 전의 타입이고 파라미터 T는 변환할 타입이다. 또한, 위임 배열 또는 컬랙션 converter가 등록되어 있는 경우(`DefaultConversionService`가 기본적으로 수행) S의 배열 또는 컬렉션을 T의  배열 또는 컬렉션으로 변환할때도  이러한 converter를 투명하게 적용할 수 있다.

convert(S)를 호출할 때 source 아규먼트는 null이 아니라는 것을 보장해야 한다. 작성한 Converter는 변환에 실패했을 때 Exception을 던질 것이다. source의 값이 유효하지 않은 경우 IllegalArgumentException을 던져야 한다. 작성한 Converter 구현체가 쓰레드 세이프하도록 해야 한다.

편의를 위해 여러 converter 구현이 `core.convert.support` 패키지에 제공된다. 여기에는 문자열에서 숫자 및 기타 일반적인 유형으로의 converter가 포함됩니다. 다음 목록은 일반적인 `Converter`구현 인 `StringToInteger`클래스를 보여준다.

```java
package org.springframework.core.convert.support;

final class StringToInteger implements Converter<String, Integer> {

    public Integer convert(String source) {
        return Integer.valueOf(source);
    }
}
```

### Using `ConverterFactory`

전체 클래스 계층 구조에 대한 변환로직을 한 곳으로 모으려는 경우 (예 : `String`에서 `Enum`객체 로 변환 할 때 ) 다음 예제와 같이를 `ConverterFactory`를 구현할 수 있다.

```java
package org.springframework.core.convert.converter;

public interface ConverterFactory<S, R> {

    <T extends R> Converter<S, T> getConverter(Class<T> targetType);
}
```

파라미터 S는 변환하기 전의 타입이고 파라미터 R은 변환할 클래스의 범위를 정의하는 기본타입이다. 이제 `getConverter(Class<T>)`를 구현해라. T는 R의 슈퍼클래스다.

`StringToEnumConverterFactory` 예제를 보자.

```java
package org.springframework.core.convert.support;

final class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {

    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter(targetType);
    }

    private final class StringToEnumConverter<T extends Enum> implements Converter<String, T> {

        private Class<T> enumType;

        public StringToEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        public T convert(String source) {
            return (T) Enum.valueOf(this.enumType, source.trim());
        }
    }
}
```

### Using `GenericConverter`

세련된 `Converter` 구현이 필요하다면 `GenericConverter` 인터페이스를 고려해봐라. 훨씬 유연하면서도 타입 제약이 적은 `GenericConverter`는 여러 가지 타입의 소스와 타겟간의 변환을 지원한다. 게다가 `GenericConverter`는 자신만의 변환 로직을 구현할 때 소스와 타겟 필드 컨텍스트를 사용할 수 있게 해준다. 이러한 컨텍스트로 필드 어노테이션이나 필드정의에 선언된 제너릭 정보로 타입변환을 할 수 있다.

```java
package org.springframework.core.convert.converter;

public interface GenericConverter {

    public Set<ConvertiblePair> getConvertibleTypes();

    Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);
}
```

`GenericConverter`를 구현하려면 지원하는 source->target 타입의 쌍을 반환하는 `getConvertibleTypes()`를 정의하고 변환 로직을 위해 `convert(Object, TypeDescriptor, TypeDescriptor)`를 구현한다. 소스 `TypeDescriptor`는 변환될 값이 있는 소스 필드에 접근하게 해주고 타겟 `TypeDescriptor`는 변환된 값이 할당될 타겟 필드에 접근하게 해준다.

자바 배열과 컬렉션을 변환해주는 컨버터는 `GenericConverter`의 좋은 예제다. 이 `ArrayToCollectionConverter`는 컬렉션의 엘리먼트 타입을 위한 타겟 컬렉션 타입을 선언한 필드를 가진다. 이는 타겟 필드에 컬렉션을 할당하기 전에 소스 배열의 각 엘리먼트를 컬렉션 엘리먼트 타입으로 변환한다.

#### Using `ConditionalGenericConverter`

지정한 상태가 참일 경우에만 `Converter`를 실행하고 싶은 경우. 예를 들어 타겟 필드에 어노테이션을 지정했을 때만 `Converter`를 실행하거나. 또는 특정 메소드(정적 valueOf 메서드처럼)가 지정한 타켓클래스에 정의되었을 때만 `Converter`를 실행하기를 원하는 경우. `GenericConverter`의 하위인터페이스인 `ConditionalGenericConverter`는 이러한 커스텀 크리테리아 검사를 정의할 수 있다.

```java
public interface ConditionalConverter {

    boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType);
}

public interface ConditionalGenericConverter extends GenericConverter, ConditionalConverter {
}
```

persistent 엔티티 식별자와 엔티티 참조간의 변환을 하는 `IdToEntityConverter`가 `ConditionalGenericConverter`의 좋은 예이다. `IdToEntityConverter`는 타겟 엔티티 타입이 정적 finder 메서드(예: `findAccount(Long)`)를 정의했을 때만 수행된다. `matches(TypeDescriptor, TypeDescriptor)`의 구현에서 이러한 finder 메서드 검사를 수행한다.

### The `ConversionService` API

`ConversionService`는 런타임시에 타입 변환 로직을 실행을 위한 규격화된 API를 정의한다. 때로는 이러한 인터페이스 뒤에서 컨버터가 실행된다.

```java
package org.springframework.core.convert;

public interface ConversionService {

    boolean canConvert(Class<?> sourceType, Class<?> targetType);

    <T> T convert(Object source, Class<T> targetType);

    boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType);

    Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);
}
```

대부분의 `ConversionService` 구현체는 컨버터를 등록하는 SPI를 제공하는 `ConverterRegistry`도 구현하고 있다. 내부적으로 `ConversionService` 구현체는 타입변환 로직 수행을 등록된 컨버터에 위임한다.

풍부한 `ConversionService` 구현체는 `core.convert.support` 패키지에 있다. `GenericConversionService`는 대부분에 환경에서 사용할 수 있는 범용적인 구현체이다. `ConversionServiceFactory`는 공통적인 `ConversionService` 설정을 생성하는 편리한 팩토리를 제공한다.

### Configuring a `ConversionService`

`ConversionService`는 어플리케이션 구동시에 인스턴스화되고 여러 쓰레드 사이에서 공유되도록 설계된 무상태의 객체이다. 스프링 어플리케이션에서는 보통 스프링 컨테이너(또는 ApplicationContext)마다 `ConversionService` 인스턴스를 설정한다. 설정한 `ConversionService`를 스프링이 선택해서 프레임워크가 타입변환을 수행해야 할 때마다 사용할 것이다. 이 `ConversionService`를 어떤 빈에라도 주입해서 직접 호출할 수도 있다.

>스프링에 등록된 `ConversionService`가 없으면 원래의 `PropertyEditor`기반 시스템을 사용한다.

빈 정의의 id를 `conversionService`으로 추가해서 스프링에 기본 `ConversionService`를 등록한다.

```xml
<bean id="conversionService"
    class="org.springframework.context.support.ConversionServiceFactoryBean"/>
```

기본 `ConversionService`는 문자열, 숫자, enums, 컬렉션, 맵 등의 타입을 변환한다. `converters` 프로퍼티를 설정해서 자신의 커스텀 컴버터로 기본 컨버터를 보완하거나 오버라이드할 수 있다. 프로퍼티 값은 `Converter`, `ConverterFactory`, `GenericConverter` 인터페이스를 구현할 것이다.

```xml
<bean id="conversionService"
        class="org.springframework.context.support.ConversionServiceFactoryBean">
    <property name="converters">
        <set>
            <bean class="example.MyCustomConverter"/>
        </set>
    </property>
</bean>
```

### Using a `ConversionService` Programmatically

다른 빈에 `ConversionService` 인스턴스의 참조를 주입해서 프로그래밍적으로 `ConversionService` 인스턴스를 사용할 수 있다.

```java
@Service
public class MyService {

    public MyService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public void doIt() {
        this.conversionService.convert(...)
    }
}
```

대부분의 경우에, convert 메소드를 특정한 `targetType`에 사용할 수 있다. 하지만 컬랙션 같은 복합적인 타입에서는 작동하지 않는다. 예를들어 integer 리스트를 string 리스트로 프로그램적으로 변환하기를 원하는 경우, 소스와 타겟 타입에 대한 정의를 해야한다.

다행히 `TypeDescriptor`는 다음 예에서와 같이 간단히 작업을 수행할 수 있는 다양한 옵션을 제공한다.

```java
DefaultConversionService cs = new DefaultConversionService();

List<Integer> input = ...
cs.convert(input,
    TypeDescriptor.forObject(input), // List<Integer> type descriptor
    TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(String.class)));
```

`DefaultConversionService`는 대부분의 환경에 적합한 converter를 자동으로 등록한다. 이는 컬랙션컨버터, 스칼라 컨버터, 그리고 기본적인 `Object` 와 `String`사이의 컨버터도 포함한다. `DefaultConversionService` 의 `addDefaultConverters` static 메소드를 이용하여, `ConverterRegistry`로  컨버터를 등록할 수 있다.

요소 타입에 대한 컨버터는 컬랙션, 배열 타입에서 재사용된다. 따라서, 표존 컬랙션 처리가 적절하다면,  `Collection`  `S` 에서 `Collection`  `T`로 변환하는 특정한 컨버터를 만들 필요는 없다. 