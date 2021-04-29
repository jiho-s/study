# Spring Learn

> 스프링 강의 정리

## 목차

1. [예제로 배우는 스프링 입문](#예제로-배우는-스프링-입문)
2. [스프링 프레임워크 핵심기술](#스프링-프레임워크-핵심기술)
3. [스프링 웹 MVC](#스프링-웹-mvc)
4. [스프링 데이터 JPA]()

## [예제로 배우는 스프링 입문](./SpringByExample)

> [강의 링크](https://www.inflearn.com/course/spring_revised_edition/dashboard)

- [예제로 배우는 스프링 입문](./SpringByExample/README.md)

## [스프링 프레임워크 핵심기술](./SpringCore)

> [강의 링크](https://www.inflearn.com/course/spring-framework_core/dashboard)

- [IoC 컨테이너](./SpringCore/IoCContainer.md)
- [Resource / Validation / SpEL](./SpringCore/ResourceValidationSpEL.md)

- [스프링 AOP](./SpringCore/SpringAOPNullSafety.md)

## [스프링 웹 MVC](./SpringWebMVC)

> [강의 링크](https://www.inflearn.com/course/웹-mvc/dashboard)

- [스프링 웹 MVC 소개](./SpringWebMVC/01_Overview.md)
- [서블릿](./SpringWebMVC/02_Servlet.md)
- [스프링과 서블릿](./SpringWebMVC/03_SpringAndServlet.md)
- [DispatcherServlet](./SpringWebMVC/04_DispatcherServlet.md)
- MVC Config
  - [스프링 MVC 구성요소 등록](./SpringWebMVC/05_MVCConfig_EnableMVCConfig.md)
  - [Formatter](./SpringWebMVC/06_MVCConfig_Formatter.md)
  - [HandlerInterceptor](./SpringWebMVC/07_HandlerInterceptor.md)
  - [ResourceHandler](./SpringWebMVC/08_ResourceHandler.md)
  - [HTTP Message Converter](./SpringWebMVC/09_HTTPMessageConverter.md)
  - [그 밖의 WebMvcConfigurer 설정](./SpringWebMVC/10_ETC_WebMvcConfigure.md)

- HTTP 요청 맵핑하기
  - [스프링에서 HTTP 요청 맵핑하기](./SpringWebMVC/11_RequestMapping_RequestMapping.md)
  - [URI 패턴 맵핑](./SpringWebMVC/12_RequestMapping_UriPatterns.md)
  - [컨텐츠 타입 맵핑](./SpringWebMVC/13_RequestMapping_ConsumableMediaTypes.md)
  - [헤더와 파라미터 맵핑](./SpringWebMVC/14_Parameters_Headers.md)
  - [HEAD와 OPTION 요청 처리](./SpringWebMVC/15_HTTP_HEAD_OPTIONS.md)
  - [커스텀 annotation](./SpringWebMVC/16_RequestMapping_CustomAnnotations.md)

- 핸들러 메소드
  - [핸들러 메소드 아규먼트](./SpringWebMVC/17_HandlerMethods_MethodArguments.md)
  - [핸들러 메소드 리턴](./SpringWebMVC/18_HandlerMethods_ReturnValues.md)
  - [URI 패턴 맵핑](./SpringWebMVC/19_HandlerMethods_UriPatternsMapping.md)
  - [요청 매개변수](./SpringWebMVC/20_HandlerMethods_RequestParams.md)
  - [ModelAttribute](./SpringWebMVC/21_HandlerMethods_ModelAttribute.md)
  - [@Validated](./SpringWebMVC/22_HandlerMethods_Validate.md)
  - [폼 서브밋 에러처리](./SpringWebMVC/23_HandlerMethods_FormSubmitError.md)
  - [@SessionAttributes & MultiFormSubmit](./SpringWebMVC/24_HandlerMethods_SessionAttributes.md)
  - [@SessionAttribute](./SpringWebMVC/25_HandlerMethods_SessionAttribute.md)
  - [RedirectAttributes](./SpringWebMVC/26_HandlerMethods_RedirectAttributes.md)
  - [FlashAttributes](./SpringWebMVC/27_HandlerMethods_FlashAttributes.md)
  - [MultipartFile](./SpringWebMVC/28_HandlerMethods_MultipartFile.md)
  - [ResponseEntity](./SpringWebMVC/29_HandlerMethods_ResponseEntity.md)