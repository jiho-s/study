##  Spring Field Formatting

이전 섹션에서 얘기했듯이 `core.convert`는 범용적인 타입변환 시스템이다. 이는 한 타입에서 다른 타입으로 변환하는 로직을 구현하는 강력한 타입의 `Converter` SPI처럼 일관된 `ConversionService` API를 제공한다. 스프링 컨테이너는 빈 프로퍼티의 값을 바인딩하는데 이 시스템을 사용한다. 게다가 스프링 표현언어 (SpEL)와 `DataBinder`는 둘 다 필드 값을 바인딩 하는데 이 시스템을 사용한다. 예를 들어 `expression.setValue(Object bean, Object value)`를 실행하기 위해 SpEL이 `Short`를 `Long`으로 강제해야 할 때 `core.convert` 시스템이 강제한다.

웹 어플리케이션이나 데스크톱 어플리케이션같은 전형적인 클라이언트 환경에서의 타입변환 요구사항을 생각해 보자. 이러한 환경에서는 보통 `String`을 클라이언트의 포스트백(postback) 과정을 지원하도록 변환하고 뷰 렌더링 과정을 위해 다시 String로 변환한다. 또한 때로는 문자열 값을 로컬라이징할 필요가 있다. 더 일반적인 `core.convert` Converter SPI는 포매팅같은 요구사항을 직접 다루지 않는다. 이러한 것들을 직접 다루기 위해 스프링 3는 `PropertyEditor` 대신 클라이언트 환경에서 사용할 수 있는 간단하고 신뢰할 수 있으며 편리한 `Formatter` SPI를 도입했다.

보통 범용적인 타입 변환 로직을 구현할 때 Converter SPI를 사용하는데 `java.util.Date`와 `java.lang.Long` 간에 변환을 하는 경우이다. 웹 어플리케이션같은 클라이언트 환경에서 작업하고 로컬라이징된 필드값을 파싱해서 출력해야 할 때 `Formatter` SPI를 사용해라. `ConversionService`는 두 SPI에 대한 일관된 타입변환 API를 제공한다.

### The `Formatter` SPI

필드 포매팅 로직을 구현하는 Formatter SPI는 간단하면서도 강력한 형식이다. 다음 목록은 `Formatter`인터페이스 정의를 보여준다.

```java
package org.springframework.format;

public interface Formatter<T> extends Printer<T>, Parser<T> {
}
```

`Formatter`는 `Printer`와 `Parser` 인터페이스를 상속받는다.

```java
public interface Printer<T> {

    String print(T fieldValue, Locale locale);
}
```

```java
import java.text.ParseException;

public interface Parser<T> {

    T parse(String clientValue, Locale locale) throws ParseException;
}
```

위의 `Formatter` 인터페이스를 구현해서 자신만의 `Formatter`를 만들 수 있다. 파라미터 `T`는 포매팅 하기를 원하는 객체의 타입이다. `T`의 인스턴스를 클라이언트 로케일(locale)로 출력하려면 print()를 구현해야 한다. 클라이언트 로케일이 반환한 포매팅된 표현에서 `T`의 인스턴스를 파싱하려면 parse()를 구현해야한다. 작성한 `Formatter`가 파싱에 실패하면 `ParseException`나 `IllegalArgumentException`를 던져야 한다. `Formatter`가 쓰레드세이프하게 구현되도록 해야한다.

여러가지 포매터 구현체는 format 하위패키지 아래 편의를 위해 제공된다.`number` 패키지는 `java.text.NumberFormat`을 사용해서 `Number` 객체를 포매팅하는 `NumberFormatter`, `CurrencyFormatter`, `PercentFormatter`를 제공한다. `datetime` 패키지는 `java.util.Date` 객체를 `java.text.DateFormat`로 포매팅하는 `DateFormatter`를 제공한다.

`Formatter` 구현체의 예제로 `DateFormatter`를 보자.

```java
package org.springframework.format.datetime;

public final class DateFormatter implements Formatter<Date> {

    private String pattern;

    public DateFormatter(String pattern) {
        this.pattern = pattern;
    }

    public String print(Date date, Locale locale) {
        if (date == null) {
            return "";
        }
        return getDateFormat(locale).format(date);
    }

    public Date parse(String formatted, Locale locale) throws ParseException {
        if (formatted.length() == 0) {
            return null;
        }
        return getDateFormat(locale).parse(formatted);
    }

    protected DateFormat getDateFormat(Locale locale) {
        DateFormat dateFormat = new SimpleDateFormat(this.pattern, locale);
        dateFormat.setLenient(false);
        return dateFormat;
    }
}
```

### Annotation-driven Formatting

필드 포맷팅은 필드타입 또는 어노테이션으로 설정할 수 있다. `Formatter`에 어노테이션을 바인딩하려면, `AnnotationFormatterFactory`를 구현해야한다. 다음은 `AnnotationFormatterFactory` 의 정의를 보여준다.

```java
package org.springframework.format;

public interface AnnotationFormatterFactory<A extends Annotation> {

    Set<Class<?>> getFieldTypes();

    Printer<?> getPrinter(A annotation, Class<?> fieldType);

    Parser<?> getParser(A annotation, Class<?> fieldType);
}
```

파라미터 A는 포매팅 로직(`org.springframework.format.annotation.DateTimeFormat` 같은)과 연결될 `annotationType` 필드이다. `getFieldTypes()`는 어노테이션이 사용될수 있는 필드의 타입을 반환한다. `getPrinter()`는 어노테이션이 붙은 필드의 값을 출력하는 `Printer`를 반환한다. getParser()는 어노테이션이 붙은 필트의 `clientValue`를 파싱하는 Parser를 반환한다.

다음 예제 `AnnotationFormatterFactory`구현은 `@NumberFormat` 주석을 포맷터에 바인딩하여 숫자 스타일 또는 패턴을 지정할 수 있도록합니다.

```java
public final class NumberFormatAnnotationFormatterFactory
        implements AnnotationFormatterFactory<NumberFormat> {

    public Set<Class<?>> getFieldTypes() {
        return new HashSet<Class<?>>(asList(new Class<?>[] {
            Short.class, Integer.class, Long.class, Float.class,
            Double.class, BigDecimal.class, BigInteger.class }));
    }

    public Printer<Number> getPrinter(NumberFormat annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation, fieldType);
    }

    public Parser<Number> getParser(NumberFormat annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation, fieldType);
    }

    private Formatter<Number> configureFormatterFrom(NumberFormat annotation, Class<?> fieldType) {
        if (!annotation.pattern().isEmpty()) {
            return new NumberStyleFormatter(annotation.pattern());
        } else {
            Style style = annotation.style();
            if (style == Style.PERCENT) {
                return new PercentStyleFormatter();
            } else if (style == Style.CURRENCY) {
                return new CurrencyStyleFormatter();
            } else {
                return new NumberStyleFormatter();
            }
        }
    }
}
```

필드에 `@NumberFormat` 어노테이션을 붙혀서 포매팅을 실행한다.

```java
public class MyModel {

    @NumberFormat(style=Style.CURRENCY)
    private BigDecimal decimal;
}
```

#### Format Annotation API

편리한 포맷 어노테이션 API는 `org.springframework.format.annotation` 패키지에 있다. `Double`과 `Long` 같은 `java.lang.Number` 필드를 포매팅하려면 `@NumberFormat`를 사용하고 `java.util.Date`, `java.util.Calendar`, `java.util.Long`, `java.time` 필드를 포매팅하려면 `@DateTimeFormat`를 사용해라.

아래의 예제는 `java.util.Date`를 ISO Date (yyyy-MM-dd)로 포매팅하려고 @DateTimeFormat를 사용한다.

```java
public class MyModel {

    @DateTimeFormat(iso=ISO.DATE)
    private Date date;
}
```

###  The `FormatterRegistry` SPI

`FormatterRegistry` 는 포매터와 컨버터를 등록하는 SPI다. `FormattingConversionService`는 대부분의 환경에 적합한 `FormatterRegistry`의 구현체이다. 이 구현체는 `FormattingConversionServiceFactoryBean`를 사용해서 스프링 빈으로써 프로그래밍적으로나 선언적으로 설정할 수 있다. 이 구현체가 `ConversionService`도 구현했기 때문에 스프링의 `DataBinder`와 SpEL를 사용해서 직접설정할 수도 있다.

다음은  `FormatterRegistry` SPI를 보여준다.

```java
package org.springframework.format;

public interface FormatterRegistry extends ConverterRegistry {

    void addFormatterForFieldType(Class<?> fieldType, Printer<?> printer, Parser<?> parser);

    void addFormatterForFieldType(Class<?> fieldType, Formatter<?> formatter);

    void addFormatterForFieldType(Formatter<?> formatter);

    void addFormatterForAnnotation(AnnotationFormatterFactory<?> factory);
}
```

위에서 보았듯이 Formatter는 fieldType이나 어노테이션으로 등록할 수 있다.

`FormatterRegistry` SPI는 여러 컨트롤러에서 중복된 설정을 하는 대신에 중앙에 포매팅 규칙을 설정할 수 있다. 예를 들어 모든 `Date` 필드를 특정 방법으로 포매팅하거나 특정 어노테이션을 가진 필드를 특정 방법으로 포매팅하는 것을 강제할 수 있다. 공유된 `FormatterRegistry`로 이러한 규칙을 한번만 정의하고 포매팅이 필요한 곳마다 적용한다.

### The `FormatterRegistrar` SPI

`FormatterRegistrar`는 `FormatterRegistry`를 통해 포매터와 컨버터를 등록하는 SPI다. 다음은 인터페이스의 정의를 보여준다. 

```java
package org.springframework.format;

public interface FormatterRegistrar {

    void registerFormatters(FormatterRegistry registry);
}
```

Date 포매팅처럼 주어진 포매팅 분류에 따라 관련된 여러가지 컨버터와 포매터를 등록할 때 `FormatterRegistrar`가 유용하다. 선언적인 등록이 충분하지 않을 때도 유용하다. 예를 들어 포매터가 포매터의 `<T>`와는 다른 특정 필드 타입하에 색인되어야 하거나 `Printer`/`Parser` 쌍을 등록하는 경우이다

