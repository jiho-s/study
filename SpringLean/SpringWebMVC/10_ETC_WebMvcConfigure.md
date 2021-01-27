## 그밖의 WebMvcConfigurer 설정

- `addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers)`

  핸들러의 아규먼트에 스프링 MVC가 제공하는 기본 아규먼트 외에 커스텀한 아규먼트를 추가하고 싶을때 설정한다

  ```java
  @GetMapping("/hello")
      public String hello(@RequestParam("name") Person person) {
          return "hello " + person.getName();
      }
  ```

- `addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers)`

  핸들러의 리턴 값에 스프링 MVC가 제공하는 기본 아규먼트 외에 커스텀한 리턴 값을 다루는 핸들러를 추가할떄 설정

- `addViewControllers(ViewControllerRegistry registry)`

  url을 특정 뷰로 매핑 시킬 때 사용, 핸들러를 작성하지 않고 간단하게 매핑 할 수 있다

  ```java
  @Configuration
  public class WebConfig implements WebMvcConfigurer {
      @Override
      public void addViewControllers(ViewControllerRegistry registry) {
          registry.addViewController("/hi").setViewName("hi");
      }
  }
  ```

- `configureAsyncSupport(AsyncSupportConfigurer configurer)`

  비동기 요청 처리에 사용할 타임아웃이나 TaskExeculor를 설정 할 수 있다

- `configureContentNegotiation(ContentNegotiationConfigurer configurer)`

  url에 "hello.json" 같은 요청을 처리하고 싶을 때 ContentNegotiation을 설정하면 처리할 수 있다