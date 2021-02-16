# Study

> 공부한 내용 정리

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 목차

1. [Spring Reference](#spring-reference)
2. [Spring Lean](#spring-lean)
3. [Java](#java)
4. [Algorithm](#algorithm)

## [Spring Reference](./SpringReference)

> 스프링 레퍼런스 정리

- [Spring Framework](./SpringReference/SpringFramework)
  - [Over View](./SpringReference/SpringFramework/OverView.md)
  - [Core Technologies](./SpringReference/SpringFramework/CoreTechnologies)
    - [IoC Container](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container)
      - [Spring IoC Container와 Beans](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container/01_Spring_IoC_Container와_Beans.md)
      - [Container 개요](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container/02_Container_Overview.md)
      - [Bean 개요](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container/03_Bean_Overview.md)
      - [Dependencies](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container/04_Dependencies.md)
      - [Bean Scope](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container/05_Bean_Scope.md)
      - [Customizing the Nature of a Bean](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container/06_Customizing_the_Nature_of_a_Bean.md)
      - [Bean Definition Inheritance](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container/07_Bean_Definition_Inheritance.md)
      - [Container Extension Point](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container/08_Container_Extension_Point.md)
      - [Annotation-based Container Configuration](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container/09_Annotation-based_Container_Configuration.md)
      - [Classpath Scanning and Managed Components](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container/10_ClasspathScanningAndManagedComponents.md)
      - [Using JSR 330 Standard Annotations](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container/11_UsingJSR330StandardAnnotations)
      - [Java-based Container Configuration](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container/12_Java-basedContainerConfiguration)
      - [Envivonment Abstraction](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container/13_EnvironmentAbstraction.md)
      - [Registering a `LoadTimeWeaver`](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container/14_RegisteringLoadTimeWeaver.md)
      - [Additional Capabilities of the `ApplicationContext`](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container/15_AdditionalCapabilitiesApplicationContext.md)
    - [Resources](./SpringReference/SpringFramework/CoreTechnologies/2_Resources)
      - [Resource Interface](./SpringReference/SpringFramework/CoreTechnologies/2_Resources/01_ResourceInterface.md)
      - [Built-in Resource Implementations](./SpringReference/SpringFramework/CoreTechnologies/2_Resources/02_Built-inResourceImplementations)
      - [ResourceLoader](./SpringReference/SpringFramework/CoreTechnologies/2_Resources/03_ResourceLoader.md)
      - [ResourceLoaderAware Interface](./SpringReference/SpringFramework/CoreTechnologies/2_Resources/04_ResourceLoaderAware.md)

## [Spring Lean](./SpringLean)

> 스프링 강의 정리

- [예제로 배우는 스프링 입문](./SpringLean/SpringByExample)
- [스프링 프레임워크 핵심기술](./SpringLean/SpringCore)
  - [IoC 컨테이너](./SpringLean/SpringCore/IoCContainer.md)
  - [Resource / Validation / SpEL](./SpringLean/SpringCore/ResourceValidationSpEL.md)
  - [스프링 AOP](./SpringLean/SpringCore/SpringAOPNullSafety.md)
- [스프링 웹 MVC](./SpringLean/SpringWebMVC)
  - [스프링 웹 MVC 소개](./SpringLean/SpringWebMVC/01_OverView.md)
  - [서블릿](./SpringLean/SpringWebMVC/02_Servlet.md)
  - [스프링과 서블릿](./SpringLean/SpringWebMVC/03_SpringAndServlet.md)
  - [DispatcherServlet](./SpringLean/SpringWebMVC/04_DispatcherServlet.md)
  - MVC Config
    - [스프링 MVC 구성요소 등록](./SpringLean/SpringWebMVC/05_MVCConfig_EnableMVCConfig.md)
    - [Formatter](./SpringLean/SpringWebMVC/06_MVCConfig_Formatter.md)
    - [HandlerInterceptor](./SpringLean/SpringWebMVC/07_HandlerInterceptor.md)
    - [ResourceHandler](./SpringLean/SpringWebMVC/08_ResourceHandler.md)
    - [HTTP Message Converter](./SpringLean/SpringWebMVC/09_HTTPMessageConverter.md)
    - [그밖의 WebMvcConfigurer 설정](./SpringLean/SpringWebMVC/10_ETC_WebMvcConfigure.md)
  - HTTP 요청 맵핑하기
    - [스프링에서 HTTP 요청 맵핑하기](./SpringLean/SpringWebMVC/11_RequestMapping_RequestMapping.md)
    - [URI 패턴 맵핑](./SpringLean/SpringWebMVC/12_RequestMapping_UriPatterns.md)
    - [컨텐츠 타입 맵핑](./SpringLean/SpringWebMVC/13_RequestMapping_ConsumableMediaTypes.md)
    - [헤더와 파라미터 맵핑](./SpringLean/SpringWebMVC/14_Parameters_Headers.md)
    - [HEAD와 OPTION 요청처리](./SpringLean/SpringWebMVC/15_HTTP_HEAD_OPTIONS.md)
    - [커스텀 annotation](./SpringLean/SpringWebMVC/16_RequestMapping_CustomAnnotations.md)
  - 핸들러 메소드
    - [핸들러 메소드 아규먼트](./SpringLean/SpringWebMVC/17_HandlerMethods_MethodArguments.md)
    - [핸들러 메소드 리턴](./SpringLean/SpringWebMVC/18_HandlerMethods_ReturnValues.md)
    - [URI 패턴 맵핑](./SpringLean/SpringWebMVC/19_HandlerMethods_UriPatternsMapping.md)
    - [요청 매개변수](./SpringLean/SpringWebMVC/20_HandlerMethods_RequestParams.md)

## [Java](./Java)

- [Garbage Collector](./Java/GarbageCollector)

## [Algorithm](./Algorithm)

- [알고리즘 문제 해결 전략](./Algorithm/Book)
  - [문제 해결 개관](./Algorithm/Book/Chapter2)
  - [코딩과 디버깅에 관하여](./Algorithm/Book/Chapter3)
  - [알고리즘의 시간 복잡도 분석](./Algorithm/Book/Chapter4)
  - [무식하게 풀기](./Algorithm/Book/Chapter6)

- [it 취업을 위한 알고리즘 문제풀이](./Algorithm/Solve)
  - [코드 구현력 기르기](./Algorithm/Solve/01_IncreaseCodeImplementation.md)
  - [정렬, 이분탐색, 스택](./Algorithm/Solve/2_SortBinarySearchStack.md)