## 커스텀 annotation

@RequestMapping annotation을 메타 annotation으로 사용하여 @GetMapping 같은 커스텀한 annotaion을 만들 수 있다

- 메타 annotation

  - annotation에 사용할 수 있는 annotation
  - 스프링이 제공하는 대부분의 annotation은 메타 annotation으로 사용할 수 있다

- 조합 annotation

  - 한 개 또는 여러 메타 annotation을 조합해서 만든 annotation
  - 코드를 간결하게 줄일 수 있다
  - 보다 구체적인 의미를 부여할 수 있다

- @Retention

  해당 annotation 정보를 언제까지 유지할지 설정하는 메타 annotation

  - Source: 소스코드까지만 유지, 컴파일 하면 해당 annotation 정보는 사라진다, 주석으로 사용할때 사용
  - Class: 컴파일한 .class 파일에도 유지, 클래스를 로딩하여 메모리로 읽어오면 사라진다
  - Runtime: 클래스를 메모리에 읽어 왔을 때까지 유지

- @Target

  - 해당 annotation을 어디에 사용할 수 있는지 결정하는 메타 annotation

- @Documentd

  - 해당 annotation을 사용한 코드의 문서에 그 annotation에 대한 정보를 표기할지 결정
  - Java Doc에 이 Annotation도 문서에 남도록 하기 위해 설정

  ```java
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.RequestMethod;
  
  import java.lang.annotation.ElementType;
  import java.lang.annotation.Retention;
  import java.lang.annotation.RetentionPolicy;
  import java.lang.annotation.Target;
  
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @RequestMapping(method = RequestMethod.GET, value = "/hello")
  public @interface GetHelloMapping {
  
  }
  ```

  ```java
  @Controller
  public class SampleController {
  
      @GetHelloMapping
      @ResponseBody // 없으면 이름에 해당하는 뷰로 돌아간다, 있으면 문자열을 응답으로 보냄
      public String hello() {
          return "hello";
      }
  }
  ```

  