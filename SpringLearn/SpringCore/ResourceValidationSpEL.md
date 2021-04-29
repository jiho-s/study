## Resource / Validation / SpEL

### 목차

1. [Resource 추상화](#resource-추상화)
2. [Validation 추상화](#validation-추상화)
3. [데이터 바인딩 추상화](#데이터-바인딩-추상화)
   - [PropertyEditor](#propertyeditor)
   - [Converter와 Formatter](#converter와-formatter)
4. [SpEL(Spring Expression Language)](#spel(spring-expression-language))

### Resource 추상화

- Resource 추상화

  java.net.URL을 추상화 한것, org.springframework.core.io.Resource로 추상화 시킴 Resource 자체를 추상화 시킴 스프링 내부에서 많이 사용한다.

- 추상화 이유

  1. jaba.net.URL이 클래스패스 기준으로 리소스 읽어오는 기능 부재
  2. ServletVontext를 기준으로 상대 경로로 읽어오는 기능 부재
  3. 구현이 복잡하고 편의성 메소드 부족

- 구현체

  1. UrlResource: http. https. ftp, file, jar 지원
  2. ClassPathResource: 'classpath:' 지원
  3. FileSystemResource
  4. ServletContextResource: 웹 애플리케이션 루트에서 상대 경로로 리소스를 찾는다.

- 리소스 읽어오기

  1. Resource의 타입이 locaion 문자열과 ApplicationContext의 타입에 따라 결정

     - ClassPathXmlApplicationContext → classPathResource
     - FileSystemXmlAppicationContext → FileSystemResource
     - WebApplicationContext → ServletContextResource

     다음과 같은 경우 classPathXmlApplicationContext를 사용 했기 때문에 resource은 ClassPathResource가 된다.

     ```java
     @Component
     public class AppRunner implements ApplicationRunner {
     
         @Autowired
         ResourceLoader resourceLoader;
         @Override
         public void run(ApplicationArguments args) throws Exception {
             var ctx = new ClassPathXmlApplicationContext("xxx.xml");
             Resource resource = resourceLoader.getResource("test.txt");
             System.out.println(resource.exists());
             System.out.println(resource.getDescription());
             System.out.println(Files.readString(Path.of(resource.getURI())));
     
         }
     }
     ```

  2. ApplicationContext의 타입에 상관없이 리소스 타입을 강제

     java.net.URL 접두어 중 하나를 사용한다.

     - classpath:me/jiho/config.xml → ClassPathResource
     - file:///some/resource/path/config.xml → FileSystemResource

     다음과 같이 ApplicationContext의 타입이 WebApplicationContext지만 classpath: 접두어를 이용해 Resource 타입이 ClassPathResource가 된 경우 이다.

     ```java
     @Component
     public class AppRunner implements ApplicationRunner {
     
         @Autowired
         ResourceLoader resourceLoader;
         @Override
         public void run(ApplicationArguments args) throws Exception {
             System.out.println(resourceLoader.getClass());
     
             Resource resource = resourceLoader.getResource("classpath:test.txt");
     
             System.out.println(resource.getClass());
             System.out.println(resource.exists());
             System.out.println(resource.getDescription());
             System.out.println(Files.readString(Path.of(resource.getURI())));
     
         }
     }
     ```

### Validation 추상화

- Validation 추상화

  Validator 인터페이스를 제공하여 객체를 검증하는데 사용,  웹, 서비스, 데이터 등 모든 계층에서 사용할 수 있다. Java EE 표준인 Bean Validation을 이용해 객체를 검증할 수 있다.

- Validator에서 구현해야할 메서드

  1. boolean supports(Class clazz)

     어떤 타입의 객체를 검증 할 때 사용할 것인지 결정

  2. void validate(Object obj, Errors e)

     실제 검증 로직을 이 안에서 구현한다.

  아래와 같은 클래스 Event가 있다.

  ```java
  package me.jiho.springdemo;
  
  public class Event {
  
      Integer id;
  
      String title;
  
      public Integer getId() {
          return id;
      }
  
      public void setId(Integer id) {
          this.id = id;
      }
  
      public String getTitle() {
          return title;
      }
  
      public void setTitle(String title) {
          this.title = title;
      }
  }
  ```

  Event라는 클래스에 title이 비어 있으면 안된다고 하자 supports를 이용해 타입이 Event인지 확인을 하고, validate 메소드를 이용해 비어 있는지 확인을 하고 비어 있거나 공백인 경우에 errors에 error 정보를 넣는다.

  ```java
  public class EventValidator implements Validator {
  
      @Override
      public boolean supports(Class<?> clazz) {
          return Event.class.equals(clazz);
      }
  
      @Override
      public void validate(Object target, Errors errors) {
          ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "notempty", "Empty title is not allowed");
      }
  }
  ```

  아래는 ApplicationRunner를 이용해 Validator가 잘 동작하고 있는 것을 확인 할 수 있다. BeanPropertyBindingResult는 Errors를 받는 클래스로 스프링부트 사용시에는 자동으로 주입받지만 테스트를위해 만들었다.

  ```java
  @Component
  public class AppRunner implements ApplicationRunner {
  
      @Override
      public void run(ApplicationArguments args) throws Exception {
          Event event = new Event();
          EventValidator eventValidator = new EventValidator();
          Errors errors = new BeanPropertyBindingResult(event, "event");
  
          eventValidator.validate(event, errors);
  
          System.out.println(errors.hasErrors());
  
          errors.getAllErrors().forEach(e -> {
              System.out.println("=========error code========");
              Arrays.stream(e.getCodes()).forEach(System.out::println);
              System.out.println(e.getDefaultMessage());
          });
      }
  }
  ```

- Bean Validation을 이용한 검증

  스프링 부트 2.0.5 이상에서는 LocalValidatorFactoryBean을 빈으로 자동으로 등록해 준다. Event 클래스에 다음과 같이 Validation annotation인 @NotEmpty @NutNull @Min @Max 등을 써주고

  ```java
  package me.jiho.springdemo;
  
  import javax.validation.constraints.Min;
  import javax.validation.constraints.NotEmpty;
  
  public class Event {
  
      Integer id;
  
      @NotEmpty
      String title;
  
      @Min(0)
      Integer limit;
  
      public Integer getLimit() {
          return limit;
      }
  
      public void setLimit(Integer limit) {
          this.limit = limit;
      }
  
      public Integer getId() {
          return id;
      }
  
      public void setId(Integer id) {
          this.id = id;
      }
  
      public String getTitle() {
          return title;
      }
  
      public void setTitle(String title) {
          this.title = title;
      }
  }
  ```

  AppRunner에서 Validator을 주입받아 사용할 수 있다.

  ```java
  @Component
  public class AppRunner implements ApplicationRunner {
  
      @Autowired
      Validator validator;
  
      @Override
      public void run(ApplicationArguments args) throws Exception {
          Event event = new Event();
          event.setLimit(-1);
  
          Errors errors = new BeanPropertyBindingResult(event, "event");
  
          validator.validate(event, errors);
  
          System.out.println(errors.hasErrors());
  
          errors.getAllErrors().forEach(e -> {
              System.out.println("=========error code========");
              Arrays.stream(e.getCodes()).forEach(System.out::println);
              System.out.println(e.getDefaultMessage());
          });
      }
  }
  ```

  ![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/36a0a9c0-2076-4713-81b8-d9e12481e198/_2020-05-07__10.34.21.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/36a0a9c0-2076-4713-81b8-d9e12481e198/_2020-05-07__10.34.21.png)

### 데이터 바인딩 추상화

#### PropertyEditor

- 데이터 바인딩

  사용자가 입력한 값을 애플리케이션 도메인 모델에 동적으로 변환해 넣어주는 기능, 문자열을 객체가 가지고 있는 여러 데이터 타입으로 변환해서 넣어주는 기능

- PropertyEditor

  가장 고전적인 방식의 데이터 바인딩, 쓰레드 세이프 하지 않아 빈으로 등록해서 사용하면 안된다. 먼저 아래와 같이 Event 클래스와 EventController를 만든다.

  ```java
  package me.jiho.springdemo;
  
  public class Event {
      private Integer id;
      private String title;
  
      public Event(Integer id) {
          this.id = id;
      }
  
      public Integer getId() {
          return id;
      }
  
      public void setId(Integer id) {
          this.id = id;
      }
  
      public String getTitle() {
          return title;
      }
  
      public void setTitle(String title) {
          this.title = title;
      }
  }
  ```

  ```java
  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.PathVariable;
  import org.springframework.web.bind.annotation.RestController;
  
  @RestController
  public class EventController {
  
      @GetMapping("/event/{event}")
      public String getEvent(@PathVariable Event event) {
          System.out.println(event);
          return event.getId().toString();
      }
  }
  ```

  다음으로 PropertyEditor를 구현한 클래스인 PropertyEditorSupport를 상속한 EventPropertyEditor를 만들어 준다. PropertyEditorSupport에서 getAsText()와 setAsText(String text) 두 개의 메소드만 오버로딩 해주면 된다. getAsText()는 getValue()로 받은 객체를 Event 타입으로 변환 시킨후 String 타입으로 return 할 수 있고, setAsText를 이용해 text를 Event 타입으로 변환 할 수 있다.

  ```java
  package me.jiho.springdemo;
  
  import java.beans.PropertyEditorSupport;
  
  public class EventEditor extends PropertyEditorSupport {
      @Override
      public String getAsText() {
          Event event = (Event)getValue()
          return event.getId().toString();
      }
  
      @Override
      public void setAsText(String text) throws IllegalArgumentException {
          setValue(new Event(Integer.parseInt(text)));
      }
  }
  ```

  스레드 세이프 하지 않아 아래와 같이 컨트롤러에 등록해서 사용한다.

  ```java
  @RestController
  public class EventController {
      
      @InitBinder
      public void init(WebDataBinder webDataBinder) {
          webDataBinder.registerCustomEditor(Event.class, new EventEditor());
      }
      
      @GetMapping("/event/{event}")
      public String getEvent(@PathVariable Event event) {
          System.out.println(event);
          return event.getId().toString();
      }
  }
  ```

  아래와 같이 Test를 이용하면 PropertyEditor가 문자로 들어온 1을 Event로 변환 시켜 문제 없이 동작함을 확인 할 수 있다.

  ```java
  @ExtendWith(SpringExtension.class)
  @WebMvcTest
  class EventControllerTest {
  
      @Autowired
      MockMvc mockMvc;
  
      @Test
      public void getTest() throws Exception {
          mockMvc.perform(get("/event/1"))
                  .andExpect(status().isOk())
                  .andExpect(content().string("1"));
      }
  }
  ```

#### Converter와 Formatter

- Converter

  PropertyEditor가 String과 Object 사이의 변환에만 사용할 수 있는 것에 비해 모든 타입으로 변환 할 수 없다. 또한 쓰레드 세이프 하다. Converter는 아래와 같이 구현한다. Converter<S, T>를 상속 받아 S convert(T) 메소드를 구현하면 S → T로 바꾸어 주는 Converter를 만들 수 있다.

  ```java
  package me.jiho.springdemo;
  
  import org.springframework.core.convert.converter.Converter;
  
  public class EventConverter {
      public static class StringToEventConverter implements Converter<String, Event> {
          @Override
          public Event convert(String source) {
              return new Event(Integer.parseInt(source));
          }
      }
  
      public static class EventToStringConverter implements Converter<Event, String> {
          @Override
          public String convert(Event source) {
              return source.getId().toString();
          }
      }
  }
  ```

  Converter는 쓰레드 세이프 하기 때문에 Bean으로 등록해서 사용 할 수 있는데 등록하는 방법은 WebMvcConfigurer를 구현한 클래스를 만들고 addFormatters 메서드를 이용해 등록하면 된다. PropertyEditor에서 만든 EventControllerTest를 실행 시키면 문제 없이 동작하는 것을 확인 할 수 있다.

  ```java
  import org.springframework.context.annotation.Configuration;
  import org.springframework.format.FormatterRegistry;
  import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
  
  @Configuration
  public class WebConfig implements WebMvcConfigurer {
      @Override
      public void addFormatters(FormatterRegistry registry) {
          registry.addConverter(new EventConverter.StringToEventConverter());
      }
  }
  ```

- Formatter

  Web의 경우 String과 Object의 변환만 사용, 따라서 문자열 다국화 기능 등을 포함하여 Web에 특화된 기능을 제공하는 Interface, Converter와 마찬가지로 쓰레드 세이프하다.

  ```java
  import org.springframework.format.Formatter;
  
  import java.text.ParseException;
  import java.util.Locale;
  
  public class EventFormatter implements Formatter<Event> {
      @Override
      public Event parse(String text, Locale locale) throws ParseException {
          return new Event(Integer.parseInt(text));
      }
  
      @Override
      public String print(Event object, Locale locale) {
          return object.getId().toString();
      }
  }
  ```

  다음과 같이 MessageSource를 주입받아 전달받은 Locale 정보를 이용할 수 도 있다.

  ```java
  @Component
  public class EventFormatter implements Formatter<Event> {
  
      @Autowired
      MessageSource messageSource;
  
      @Override
      public Event parse(String text, Locale locale) throws ParseException {
          return new Event(Integer.parseInt(text));
      }
  
      @Override
      public String print(Event object, Locale locale) {
          messageSource.getMessage("title", locale);
          return object.getId().toString();
      }
  }
  ```

  등록은 다음과 같은 방법으로 한다.

  ```java
  @Configuration
  public class WebConfig implements WebMvcConfigurer {
      @Override
      public void addFormatters(FormatterRegistry registry) {
          registry.addFormatter(new EventFormatter());
      }
  }
  ```

- ConversionService

  Converter, Formatter등은 실제 ConversionService에 등록되어 사용되고 실제 ConversionService에서 변환이 일어난다. 실제 사용은 DefaultFormattingConversionService라는 구현체를 사용하는데 FormatterRegistry, ConversionService를 모두 구현하였다.

  스프링부트에는 WebConversionService를 빈으로 등록해서 사용하는데 이는 DefaultFOrmattingConversionService를 상속해서 만든 것으로 Formatter와 Converter 빈을 찾아 자동으로 ConversionService로 등록해서 @Configuration에 등록하지 않아도 사용할 수 있다.

  ```java
  public class EventConverter {
      @Component
      public static class StringToEventConverter implements Converter<String, Event> {
          @Override
          public Event convert(String source) {
              return new Event(Integer.parseInt(source));
          }
      }
      @Component
      public static class EventToStringConverter implements Converter<Event, String> {
          @Override
          public String convert(Event source) {
              return source.getId().toString();
          }
      }
  }
  ```

  테스트를 통해 확인해 보면 @Configuration에 등록하지 않아도 잘 동작하는 것을 확인 할 수 있다.

  ```java
  @ExtendWith(SpringExtension.class)
  @WebMvcTest({EventFormatter.class, EventController.class})
  class EventControllerTest {
  
      @Autowired
      MockMvc mockMvc;
  
      @Test
      public void getTest() throws Exception {
          mockMvc.perform(get("/event/1"))
                  .andExpect(status().isOk())
                  .andExpect(content().string("1"));
      }
  }
  ```

### SpEL(Spring Expression Language)

- SpEL

  객체 그래프를 조회하고 조작하는 기능릉 제공, 메소드호출 및 문자열 템플릿 기능 제공, 자바에서 사용할 수 있는 여러 EL이 있지만, SpEL은 모든 스프링 프로젝트 전반에 걸쳐 사용할 EL로 만들어짐

- SpEL 구성

  SpEL은 ExpressionParser을 통해 SpEL의 표현식을 파싱하며 StandardEvaluationContext를 통해 스피링 빈이 가지고 있는 객체 정보를 얻는다. 이를 Expression을 통해 사용한다.

  ```java
  @Component
  public class AppRunner implements ApplicationRunner {
  
      @Override
      public void run(ApplicationArguments args) throws Exception {
          ExpressionParser parser = new SpelExpressionParser();
          Expression expression = parser.parseExpression("2 + 100");
          Integer value = expression.getValue(Integer.class);
          System.out.println(value);
  
      }
  }
  ```

- 실제 사용

  1. @Value annotation

     @Value에 #{""}을이용해 표현식을 이용, 혹은 ${""}를 이용하여 프로퍼티를 참조하여 사용할 수 있다. 또한 빈의 메소드 및 값을 #{""}을 이용해 부를수도 있다.

     ```java
     package me.jiho.springdemo;
     
     import org.springframework.beans.factory.annotation.Value;
     import org.springframework.boot.ApplicationArguments;
     import org.springframework.boot.ApplicationRunner;
     import org.springframework.stereotype.Component;
     
     @Component
     public class AppRunner implements ApplicationRunner {
     
         @Value("#{1+1}") //표현
         int value;
     
         @Value("#{'hello' + 'world'}")
         String greeting;
     
         @Value("hello")
         String hello;
     
         @Value("#{1 eq 1}")
         boolean trueOrFalse;
     
         @Value("${my.value}")
         int myValue;
     
         @Value("#{${my.value} eq 100}")
         boolean isMyValue100;
     
         @Value("#{sample.data}")
         int sampleData;
     
         @Override
         public void run(ApplicationArguments args) throws Exception {
             System.out.println(value);
             System.out.println(greeting);
             System.out.println(hello);
             System.out.println(trueOrFalse);
             System.out.println(myValue);
             System.out.println(isMyValue100);
             System.out.println(sampleData);
         }
     }
     ```

  2. @ConditionalOnExpression annotation

     선택적으로 빈을 등록하거나 빈 설정파일을 읽어 올 경우 사용 SpEL기반으로 선별적으로 불러 올 수 있다.

  3. Spring Security

     메소드 시큐리티, @PreAuthorize, @PostAuthozie, @PreFilter, @PostFilter등에 사용

  4. 스프링 데이터

     @Query annotation

     ```java
     @Query("select u from User u where u.age = ?#{[0]}")
     List<User> findUsersByAge(int age);
     
     @Query("select u from User u where u.firstname = :#{#customer.firstname}")
     List<User> findUsersByCustomersFirstname(@Param("customer") Customer customer);
     ```