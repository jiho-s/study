## 스프링에서 HTTP 요청 맵핑하기

### RequestMapping

- @RequestMapping(value , method )

  - @RequestMapping annotation을 이용해 HTTP 요청을 맵핑 할 수 있다  method에 해당하는 HTTP 메소드에 value에 해당하는 url에 매핑된다
  - method를 쓰지 않은 경우 모든 HTTP 요청에 맵핑된다. method가 여러개인경우 배열 형태로 RequstMethod를 넣어 줄 수도 있다
  - @RequestMapping annotation을 클래스에 설정할 수 있는데 이 경우 클래스 안의 모든 메소드에 적용 시킬수 있다

  ```java
  import org.springframework.stereotype.Controller;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.RequestMethod;
  import org.springframework.web.bind.annotation.ResponseBody;
  
  @Controller
  public class SampleController {
  
      @RequestMapping(value = "/hello", method = RequestMethod.GET) //http메소드를 지정하지 않으면 모든 http 메소드 허용
      @ResponseBody // 없으면 이름에 해당하는 뷰로 돌아간다, 있으면 문자열을 응답으로 보냄
      public String hello() {
          return "hello";
      }
  }
  ```

  url에 해당하는 HTTP 메소드가 없는경우 테스트를 진행해 보았다 "/hello"라는 url이 있어도 해당 HTTP 메소드가 없는 경우 테스트가 실패하는 것을 확인 할 수 있다

  ```java
  @ExtendWith(SpringExtension.class)
  @WebMvcTest //mock을 주입해준다
  class SampleControllerTest {
  
      @Autowired
      MockMvc mockMvc;
  
      @Test
      public void helloTest() throws Exception{
          mockMvc.perform(put("/hello"))
                  .andDo(print())
                  .andExpect(status().isOk())
                  .andExpect(content().string("hello"));
      }
  }
  ```

  ![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/5c0873b6-a411-4656-a5ef-efa8aebcfb63/_2020-05-19__7.22.47.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/5c0873b6-a411-4656-a5ef-efa8aebcfb63/_2020-05-19__7.22.47.png)

- @GetMapping, @PostMapping

  하나의 HTTP 요청만을 처리하고 싶은경우 @GetMapping 등의 annotation을 사용 할 수 있다

  ```java
  import org.springframework.stereotype.Controller;
  import org.springframework.web.bind.annotation.*;
  
  @Controller
  public class SampleController {
  
      @GetMapping("/hello")
      @RequestBody
      public String hello() {
          return "hello";
      }
  }
  ```

### HTTP Method

GET, POST, PUT, PATCH, DELETE

- GET Request

  - 클라이언트가 서버의 리소스를 요청할 때 사용한다

  - 캐싱 할 수 있다

    응답을 보낼때 캐시와 관련된 헤더를 응답에 넣어서 보낼 수 있다 동일 한 요청을 브라우저에서 보내면 조건적인 GET으로 바뀌어, if-not-modified, modified-since 헤더를 사용 하여 not-modified인 경우 서버가 응답에 ResponseBody를 없이 보내 클라이언트가 캐싱하고 있던 값을 그대로 사용가능하다

  - 브라우저 기록에 남는다

  - 북마크 할 수 있다

  - 민감한 데이터를 보낼 때 사용하지 말아야 한다

    url에 정보가 노출된다

  - idemponent

    동일한 GET요청은 항상 동일한 응답을 응답한다.

    POST 요청은 idemponent하지 않다.

- POST Request

  - 클라이언트가 서버의 리소스를 수정하거나 새로 만들 때 사용
  - 서버에 보내는 데이터를 POST 본문에 담는다
  - 캐싱 할 수 없다
  - 브라우저 기록에 남지 않는다
  - 북마크 할 수 없다
  - 데이터 길이 제한이 없다

- PUT Request

  - URI에 해당하는 데이터를 새로 만들거나 수정할 때 사용한다
  - POST와 다른 점은 URI에 대한 의미가 다르다
    - POST의 URI는 보내는 데이터를 처리할 리소스를 지칭한다
    - PUT의 URI는 보내는 데이터에 해당하는 리소스를 지칭한다
  - idemponent

- PATCH Request

  - PUT과 비슷하지만, 기존 엔티티와 새 데이터의 차이점만 보낸다
  - idemponent

- DELETE Request

  - URI에 해당하는 리소스를 삭제할 때 사용한다
  - idemponent