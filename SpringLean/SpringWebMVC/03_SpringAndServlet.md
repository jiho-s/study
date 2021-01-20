## 스프링 IoC 컨테이너 연동

- 서블릿 애플리케이션에 스프링 연동의 의미

  - 서블릿에서 스프링이 제공하는 IoC 컨테이너를 활용
  - 스프링이 제공하는 서블릿 구현체 DispatcherServlet 사용

- spring-webmvc 추가

  pom.xml에 다음과 같이 spring-webmvc 의존성을 추가 해준다

  ```xml
  <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>5.2.6.RELEASE</version>
      </dependency>
  ```

- ContextLoaderListener 추가

  ContextLoaderListener의 역할

  - ApplicationContext를 서블릿 애플리케이션의 생명주기에 맞추어 바인딩
  - ApplicationContext를 서블릿이 사용할 수 있도록 ServletContext에 등록
  - Servlet 종료시점에 ApplicationContext 제거
  - ApplicationContext를 만들어야 하므로 스프링 설정 파일이 필요

  web.xml에서 리스너를 스프링이 지원해주는 ContextLoaderListener로 바꾸어준다

  ```xml
  	<listener>
      <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
  ```

- 자바 기반 스프링 설정 파일 만들기

  web.xml에서 context-param의 contextClass를 AnnotationConfigWebApplicationContext로 설정해준다.

  ```xml
  	<context-param>
      <param-name>contextClass</param-name>
      <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
    </context-param>
  ```

  java 패키지 안에 AppConfig 파일을 만들고 @Configuration을 통해 설정파일인것을 알려주고 @ComponentScan을 통해 현재 패키지 안에 있는Component를 찾아Bean으로 등록한다

  ```java
  import org.springframework.context.annotation.ComponentScan;
  import org.springframework.context.annotation.Configuration;
  
  @Configuration
  @ComponentScan
  public class AppConfig {
  }
  ```

  web.xml에 자바 설정파일의 위치를 등록한다

  ```xml
  	<context-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>me.jiho.AppConfig</param-value>
    </context-param>
  ```

  간단한 HelloService를 다음과 같이 만든다

  ```java
  import org.springframework.stereotype.Service;
  
  @Service
  public class HelloService {
      public String getName() {
          return "jiho";
      }
  }
  ```

  ContextLoaderListener가 AnnotationConfigWebApplicationContext를 만들면서 contextConfigLocation의 value 값인 AppConfig를 가지고 만든다.  이때 HelloService가 빈으로 등록된다 서블릿에서는 ApplicationContext를 통해서 HelloService를 주입받아 사용할 수 있다.

- 서블릿에서 빈 사용하기

  ContextLoaderListener는 ApplicationContext를 ServletContext라는 모든 서블릿의 정보를 모아둔 곳에 WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE 라는 이름의 key로 저장한다 따라서 HelloServlet의 doGet을 다음과 같이 변경한다.

  ```java
  @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
  													throws ServletException, IOException {
          ApplicationContext context = (ApplicationContext) getServletContext()
  					.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
          HelloService helloService = context.getBean(HelloService.class);
          System.out.println("doGet");
          resp.getWriter().println("<html>");
          resp.getWriter().println("<head>");
          resp.getWriter().println("</head>");
          resp.getWriter().println("<body>");
          resp.getWriter().println("<h1>Hello " + helloService.getName() + "</h1>");
          resp.getWriter().println("</body>");
          resp.getWriter().println("</html>");
      }
  ```