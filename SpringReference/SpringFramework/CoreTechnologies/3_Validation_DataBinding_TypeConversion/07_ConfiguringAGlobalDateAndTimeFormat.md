## Configuring a Global Date and Time Format

기본적으로 `@DateTimeFormat` 어노테이션이 추가되지 않은 날짜 및 시간 필드 는 `DateFormat.SHORT`스타일 을 사용하여 문자열에서 날짜 및 시간으로 변환된다 . 커스텀 글로벌 포맷을 정의하여 이를 변경할 수 있다.

Spring이 기본 포맷터를 등록하지 않도록 해야한다. 대신 다음을 사용하여 수동으로 포맷터를 등록할수 있다.

- `org.springframework.format.datetime.standard.DateTimeFormatterRegistrar`
- `org.springframework.format.datetime.DateFormatterRegistrar`

예를 들어 다음 Java 구성은 글로벌 `yyyyMMdd` 포맷을 등록한다.

```java
@Configuration
public class AppConfig {

    @Bean
    public FormattingConversionService conversionService() {

        // Use the DefaultFormattingConversionService but do not register defaults
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);

        // Ensure @NumberFormat is still supported
        conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());

        // Register JSR-310 date conversion with a specific global format
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyyMMdd"));
        registrar.registerFormatters(conversionService);

        // Register date conversion with a specific global format
        DateFormatterRegistrar registrar = new DateFormatterRegistrar();
        registrar.setFormatter(new DateFormatter("yyyyMMdd"));
        registrar.registerFormatters(conversionService);

        return conversionService;
    }
}
```

