# Study

> 공부한 내용 정리

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 목차

1. [Spring Reference](#spring-reference)
2. [Spring Learn](#spring-learn)
3. [Java](#java)
4. [JPA](#jpa)
5. [Algorithm](#algorithm)
6. [Software Design](#software-design)
7. [Operating Systems](#operating-systems)
8. [Kubernetes](#kubernetes)
9. [HTTP](#http)
10. [Java Reactor](#java-reactor)
11. [Netty](#netty)
12. [gRPC](#grpc)
13. [Kafka](#kafka)

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
      - [Using JSR 330 Standard Annotations](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container/11_UsingJSR330StandardAnnotations.md)
      - [Java-based Container Configuration](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container/12_Java-basedContainerConfiguration.md)
      - [Envivonment Abstraction](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container/13_EnvironmentAbstraction.md)
      - [Registering a `LoadTimeWeaver`](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container/14_RegisteringLoadTimeWeaver.md)
      - [Additional Capabilities of the `ApplicationContext`](./SpringReference/SpringFramework/CoreTechnologies/1_IoC_Container/15_AdditionalCapabilitiesApplicationContext.md)
    - [Resources](./SpringReference/SpringFramework/CoreTechnologies/2_Resources)
      - [Resource Interface](./SpringReference/SpringFramework/CoreTechnologies/2_Resources/01_ResourceInterface.md)
      - [Built-in Resource Implementations](./SpringReference/SpringFramework/CoreTechnologies/2_Resources/02_Built-inResourceImplementations.md)
      - [ResourceLoader](./SpringReference/SpringFramework/CoreTechnologies/2_Resources/03_ResourceLoader.md)
      - [ResourceLoaderAware Interface](./SpringReference/SpringFramework/CoreTechnologies/2_Resources/04_ResourceLoaderAware.md)
    - [Validation, Data Binding and Type Conversion](./SpringReference/SpringFramework/CoreTechnologies/3_Validation_DataBinding_TypeConversion)
      - [Validation, Data Binding and Type Conversion](./SpringReference/SpringFramework/CoreTechnologies/3_Validation_DataBinding_TypeConversion/01_OverView.md)
      - [Validation by Using Spring’s Validator Interface](./SpringReference/SpringFramework/CoreTechnologies/3_Validation_DataBinding_TypeConversion/02_ValidationByUsingSpringValidatorInterface.md)
      - [Resolving Codes to Error Messages](./SpringReference/SpringFramework/CoreTechnologies/3_Validation_DataBinding_TypeConversion/03_ResolvingCodesToErrorMessages.md)
      - [Bean Manipulation and the `BeanWrapper`](./SpringReference/SpringFramework/CoreTechnologies/3_Validation_DataBinding_TypeConversion/04_BeanManipulationAndTheBeanWrapper.md)
      - [Spring Type Conversion](./SpringReference/SpringFramework/CoreTechnologies/3_Validation_DataBinding_TypeConversion/05_SpringTypeConversion.md)
      - [Spring Field Formatting](./SpringReference/SpringFramework/CoreTechnologies/3_Validation_DataBinding_TypeConversion/06_SpringFieldFormatting.md)
      - [Configuring a Global Date and Time Format](./SpringReference/SpringFramework/CoreTechnologies/3_Validation_DataBinding_TypeConversion/07_ConfiguringAGlobalDateAndTimeFormat.md)
      - [Java Bean Validation](./SpringReference/SpringFramework/CoreTechnologies/3_Validation_DataBinding_TypeConversion/08_JavaBeanValidation.md)
    - [Aspect Oriented Programming with Spring](./SpringReference/SpringFramework/CoreTechnologies/5_AspectOrientedProgrammingWithSpring)
      - [Aspect Oriented Programming with Spring](./SpringReference/SpringFramework/CoreTechnologies/5_AspectOrientedProgrammingWithSpring/01_OverView.md)
      - [AOP Concepts](./SpringReference/SpringFramework/CoreTechnologies/5_AspectOrientedProgrammingWithSpring/02_AOPConcepts.md)
      - [Spring AOP Capabilities and Goals](./SpringReference/SpringFramework/CoreTechnologies/5_AspectOrientedProgrammingWithSpring/03_SpringAOPCapabilitiesAndGoals.md)
      - [AOP proxies](./SpringReference/SpringFramework/CoreTechnologies/5_AspectOrientedProgrammingWithSpring/04_AOPProxies.md)
      - [@AspectJ Support](./SpringReference/SpringFramework/CoreTechnologies/5_AspectOrientedProgrammingWithSpring/05_AspectJSupport)
        - [Introduction](./SpringReference/SpringFramework/CoreTechnologies/5_AspectOrientedProgrammingWithSpring/05_AspectJSupport/01_OverView.md)
        - [Enabling @AspectJ Support](./SpringReference/SpringFramework/CoreTechnologies/5_AspectOrientedProgrammingWithSpring/05_AspectJSupport/02_EnablingAspectJSupport.md)
        - [Declaring an Aspect](./SpringReference/SpringFramework/CoreTechnologies/5_AspectOrientedProgrammingWithSpring/05_AspectJSupport/03_DeclaringAnAspect.md)
        - [Declaring a Pointcut](./SpringReference/SpringFramework/CoreTechnologies/5_AspectOrientedProgrammingWithSpring/05_AspectJSupport/04_DeclaringAPointcut.md)
        - [Declaring Advice](./SpringReference/SpringFramework/CoreTechnologies/5_AspectOrientedProgrammingWithSpring/05_AspectJSupport/05_DeclaringAdvice.md)
        - [An AOP Example](./SpringReference/SpringFramework/CoreTechnologies/5_AspectOrientedProgrammingWithSpring/05_AspectJSupport/07_AnAOPExample.md)

## [Spring Learn](./SpringLearn)

> 스프링 강의 정리

- [예제로 배우는 스프링 입문](./SpringLearn/SpringByExample)
- [스프링 프레임워크 핵심기술](./SpringLearn/SpringCore)
  - [IoC 컨테이너](./SpringLearn/SpringCore/IoCContainer.md)
  - [Resource / Validation / SpEL](./SpringLearn/SpringCore/ResourceValidationSpEL.md)
  - [스프링 AOP](./SpringLearn/SpringCore/SpringAOPNullSafety.md)
- [스프링 웹 MVC](./SpringLearn/SpringWebMVC)
  - [스프링 웹 MVC 소개](./SpringLearn/SpringWebMVC/01_Overview.md)
  - [서블릿](./SpringLearn/SpringWebMVC/02_Servlet.md)
  - [스프링과 서블릿](./SpringLearn/SpringWebMVC/03_SpringAndServlet.md)
  - [DispatcherServlet](./SpringLearn/SpringWebMVC/04_DispatcherServlet.md)
  - MVC Config
    - [스프링 MVC 구성요소 등록](./SpringLearn/SpringWebMVC/05_MVCConfig_EnableMVCConfig.md)
    - [Formatter](./SpringLearn/SpringWebMVC/06_MVCConfig_Formatter.md)
    - [HandlerInterceptor](./SpringLearn/SpringWebMVC/07_HandlerInterceptor.md)
    - [ResourceHandler](./SpringLearn/SpringWebMVC/08_ResourceHandler.md)
    - [HTTP Message Converter](./SpringLearn/SpringWebMVC/09_HTTPMessageConverter.md)
    - [그밖의 WebMvcConfigurer 설정](./SpringLearn/SpringWebMVC/10_ETC_WebMvcConfigure.md)
  - HTTP 요청 맵핑하기
    - [스프링에서 HTTP 요청 맵핑하기](./SpringLearn/SpringWebMVC/11_RequestMapping_RequestMapping.md)
    - [URI 패턴 맵핑](./SpringLearn/SpringWebMVC/12_RequestMapping_UriPatterns.md)
    - [컨텐츠 타입 맵핑](./SpringLearn/SpringWebMVC/13_RequestMapping_ConsumableMediaTypes.md)
    - [헤더와 파라미터 맵핑](./SpringLearn/SpringWebMVC/14_Parameters_Headers.md)
    - [HEAD와 OPTION 요청처리](./SpringLearn/SpringWebMVC/15_HTTP_HEAD_OPTIONS.md)
    - [커스텀 annotation](./SpringLearn/SpringWebMVC/16_RequestMapping_CustomAnnotations.md)
  - 핸들러 메소드
    - [핸들러 메소드 아규먼트](./SpringLearn/SpringWebMVC/17_HandlerMethods_MethodArguments.md)
    - [핸들러 메소드 리턴](./SpringLearn/SpringWebMVC/18_HandlerMethods_ReturnValues.md)
    - [URI 패턴 맵핑](./SpringLearn/SpringWebMVC/19_HandlerMethods_UriPatternsMapping.md)
    - [요청 매개변수](./SpringLearn/SpringWebMVC/20_HandlerMethods_RequestParams.md)
    - [ModelAttribute](./SpringLearn/SpringWebMVC/21_HandlerMethods_ModelAttribute.md)
    - [@Validated](./SpringLearn/SpringWebMVC/22_HandlerMethods_Validate.md)
    - [폼 서브밋 에러처리](./SpringLearn/SpringWebMVC/23_HandlerMethods_FormSubmitError)
    - [@SessionAttributes & MultiFormSubmit](./SpringLearn/SpringWebMVC/24_HandlerMethods_SessionAttributes.md)
    - [@SessionAttribute](./SpringLearn/SpringWebMVC/25_HandlerMethods_SessionAttribute.md)
    - [RedirectAttributes](./SpringLearn/SpringWebMVC/26_HandlerMethods_RedirectAttributes.md)
    - [FlashAttributes](./SpringLearn/SpringWebMVC/27_HandlerMethods_FlashAttributes.md)
    - [MultipartFile](./SpringLearn/SpringWebMVC/28_HandlerMethods_MultipartFile.md)
    - [ResponseEntity](./SpringLearn/SpringWebMVC/29_HandlerMethods_ResponseEntity.md)

## [Java](./Java)

- [Garbage Collector](./Java/GarbageCollector)
  - [Garbage Collector](./Java/GarbageCollector/1_GarbageCollector.md)
  - [Garbage Collector Implementation](./Java/GarbageCollector/2_GarbageCollectorImplementation.md)
  - [Available Collector](./Java/GarbageCollector/3_AvailableCollectors)
  - [Parallel Collector](./Java/GarbageCollector/4_ParallelCollector.md)
  - [Concurrent Mark Sweep(CMS) Collector](./Java/GarbageCollector/5_ConcurrentMarkSweepCollector.md)
  - [Garbage-First Garbage Collector](./Java/GarbageCollector/6_GarbageFirstGarbageCollector.md)
  - [Z Garbage Collector](./Java/GarbageCollector/7_ZGarbageCollector.md)
- [Java Language](./Java/JavaLanguage)
  - [Lambda](./Java/JavaLanguage/01_Lambda.md)
  - [Generics](./Java/JavaLanguage/02_Generics.md)
  - [Static](./Java/JavaLanguage/03_Static.md)
  - [Interface and Inheritance](./Java/JavaLanguage/04_InterfacesAndInheritance.md)
  - [Enum](./Java/JavaLanguage/05_EnumTypes.md)
- [Java Virtual Machine](./Java/JavaVirtualMachine)
  - [소개](./Java/JavaVirtualMachine/01_Intruduction.md)
  - Java Virtual Machine의 구조
    - [데이터 타입](./Java/JavaVirtualMachine/02_StructureOfJVM/01_DataType.md)
    - [기본 타입과 값](./Java/JavaVirtualMachine/02_StructureOfJVM/02_PrimitiveTypesAndValues.md)
    - [참조 타입과 값](./Java/JavaVirtualMachine/02_StructureOfJVM/03_ReferenceTypesAndValues.md)
    - [런타임 데이터 영역](./Java/JavaVirtualMachine/02_StructureOfJVM/04_Run-TimeDataAreas.md)
  - [JVM의 자바 코드 실행 과정](./Java/JavaVirtualMachine/03_ExecuteJavaCode)
  - 로딩, 링킹, 초기화
    - [소개](./Java/JavaVirtualMachine/05_LoadingLinkingAndInitializing/01_Overview.md)
    - [런타임 상수 풀](./Java/JavaVirtualMachine/05_LoadingLinkingAndInitializing/02_TheRun-TimeConstantPool.md)
    - [자바 가상머신 시작](./Java/JavaVirtualMachine/05_LoadingLinkingAndInitializing/03_JavaVirtualMachineStartup.md)
    - [생성과 로딩](./Java/JavaVirtualMachine/05_LoadingLinkingAndInitializing/04_CreationAndLoading.md)
    - [링킹](./Java/JavaVirtualMachine/05_LoadingLinkingAndInitializing/05_Linking.md)
    - [초기화](./Java/JavaVirtualMachine/05_LoadingLinkingAndInitializing/06_Initialization.md)
- [Java.lang 패키지](./Java/lang)
  - [Wrapper Classes](./Java/lang/01_WrapperClasses.md)
  - [Reflect](./Java/lang/02_Reflect)
    - [Reflection API](./Java/lang/02_Reflect/01_Overview.md)
    - [Classes](./Java/lang/02_Reflect/02_Classes.md)
    - [Members](./Java/lang/02_Reflect/03_Members.md)
- [java.util 패키지](./Java/util)
  - Stream
    - [Stream](./Java/util/Stream/01_Stream.md)
  - [Concurrent](./util/Concurrent)
    - [Flow](./Java/util/Concurrent/01_Flow.md)

## [JPA](./JPA)

- Hibernate ORM Guide
  - [Architecture](./JPA/01_Architecture.md)
  - [Domain Model](./JPA/02_DomainModel)
    - [Mapping Types](./JPA/02_DomainModel/01_MappingTypes.md)
- 자바 ORM 표준 JPA 프로그래밍
  - [영속성 관리](./JPA/03_PersistenceManaging.md)
  - [프록시와 연관관계 관리](./JPA/08_ProxyAndRelationalMapping.md)
  - [웹 애플리케이션과 영속성 관리](./JPA/13_WebApplicationAndPersistenceManage.md)
  - [고급 주제와 성능 최적화](./JPA/15.md)
  - [트랜잭션과 락, 2차 캐시](./JPA/16.md)

## [Algorithm](./Algorithm)

- [알고리즘 문제 해결 전략](./Algorithm/Book)
  - [문제 해결 개관](./Algorithm/Book/Chapter2)
  - [코딩과 디버깅에 관하여](./Algorithm/Book/Chapter3)
  - [알고리즘의 시간 복잡도 분석](./Algorithm/Book/Chapter4)
  - [무식하게 풀기](./Algorithm/Book/Chapter6)

- [it 취업을 위한 알고리즘 문제풀이](./Algorithm/Solve)
  - [코드 구현력 기르기](./Algorithm/Solve/01_IncreaseCodeImplementation.md)
  - [정렬, 이분탐색, 스택](./Algorithm/Solve/02_SortBinarySearchStack.md)
  - [재귀, DFS, BFS](./Algorithm/Solve/03_Recursion_DFS_BFS.md)
  - [그래프, DFS, BFS 관련 보충문제](./Algorithm/Solve/04_Graph_DFS_BFS.md)
  - [동적 계획법](./Algorithm/Solve/05_DynamicProgramming.md)

## [Software Design](./SoftwareDesign)

- [객체지향 프로그래밍](./SoftwareDesign/01_ObjectOrientedPrograming)
  - [일급 컬렉션](./SoftwareDesign/01_ObjectOrientedPrograming/01_FirstClassCollection.md)

## [Operating Systems](./OperatingSystems)

- [Processes and Threads](./OperatingSystems/02_ProcessesAndThreads)
  - [Processes](./OperatingSystems/02_ProcessesAndThreads/01_Processes.md)
  - [Threads](./OperatingSystems/02_ProcessesAndThreads/02_Threads.md)
  - [Interprocess Communication](./OperatingSystems/02_ProcessesAndThreads/03_InterprocessCommunication.md)
  - [Scheduling](./OperatingSystems/02_ProcessesAndThreads/04_Scheduling.md)
- [Memory Management](./OperatingSystems/03_MemoryManagement)
  - [No Memory Abstraction](./OperatingSystems/03_MemoryManagement/01_NoMemoryAbstraction.md)
  - [A Memory Abstraction: Address Spcaces](./OperatingSystems/03_MemoryManagement/02_AMemoryAbstraction_AddressSpaces.md)
  - [Virtual Memory](./OperatingSystems/03_MemoryManagement/03_VirtualMemory.md)
  - [Page Replacement Algorithms](./OperatingSystems/03_MemoryManagement/04_PageReplacementAlgorithms.md)
  - [Segmentation](./OperatingSystems/03_MemoryManagement/08_Segmentation.md)
- [Deadlocks](./OperatingSystems/06_Deadlocks)
  - [Resources](./OperatingSystems/06_Deadlocks/01_Resources.md)
  - [Introduction to Deadlocks](./OperatingSystems/06_Deadlocks/02_IntroductionToDeadlocks.md)
  - [Ostrich Algorithm](./OperatingSystems/06_Deadlocks/03_OstrichAlgorithm.md)
  - [Deadlock Detection And Recovery](./OperatingSystems/06_Deadlocks/04_DeadlockDetectionAndRecovery.md)

## [Kubernetes](./Kubernetes)

- 개념
  - 개요
    - [쿠버네티스란 무엇인가?](./Kubernetes/01_Concepts/01_Overview/01_WhatIsKubernetes.md)
    - [쿠버네티스의 구성요소](./Kubernetes/01_Concepts/01_Overview/02_KubernetesComponents.md)
    - 쿠버네티스 오브젝트로 작업하기
      - [쿠버네티스 오브젝트 이해하기](./Kubernetes/01_Concepts/01_Overview/04_WorkingWithKubernetesObjects/01_UnderstandingKubernetesObjects.md)
      - [쿠버네티스 오브젝트 관리](./Kubernetes/01_Concepts/01_Overview/04_WorkingWithKubernetesObjects/02_KubernetesObjectManagement.md)
      - [오브젝트 이름과 ID](./Kubernetes/01_Concepts/01_Overview/04_WorkingWithKubernetesObjects/03_ObjectNamesAndIDs.md)
      - [네임스페이스](./Kubernetes/01_Concepts/01_Overview/04_WorkingWithKubernetesObjects/04_Namespaces.md)
      - [레이블과 셀렉터](./Kubernetes/01_Concepts/01_Overview/04_WorkingWithKubernetesObjects/05_LabelsAndSelectors.md)
    - 클러스터 아키텍처
      - [노드](./Kubernetes/02_ClusterArchitecture/01_Nodes.md)
      - [컨트롤 플레인-노드 간 통신](./Kubernetes/02_ClusterArchitecture/02_ControlPlane_NodeCommunication.md)
      - [컨트롤러](./Kubernetes/02_ClusterArchitecture/03_Controllers.md)
      - [클라우드 컨트롤러 매니저](./Kubernetes/02_ClusterArchitecture/04_CloudControllerManager.md)
    - 컨테이너
      - [컨테이너](./Kubernetes/03_Containers/01_Containers.md)
      - [이미지](./Kubernetes/03_Containers/02_Images.md)
      - [Runtime Class](./Kubernetes/03_Containers/03_RuntimeClass.md)
      - [컨테이너 환경 변수](./Kubernetes/03_Containers/04_ContainerEnvironment.md)
    - 워크로드
      - [워크로드 개요](./Kubernetes/04_Workloads/01_Overview.md)
      - 파드
        - [파드 개요](./Kubernetes/04_Workloads/02_Pods/01_Overview.md)
        - [파드 라이프사이클](./Kubernetes/04_Workloads/02_Pods/02_PodLifecycle.md)
        - [초기화 컨테이너](./Kubernetes/04_Workloads/02_Pods/03_InitContainers.md)
      - 워크로드 리소스
        - [디플로이먼트](./Kubernetes/04_Workloads/03_WorkloadResources/01_Deployments.md)
        - [스테이트풀셋](./Kubernetes/04_Workloads/03_WorkloadResources/03_StatefulSets.md)
    - 서비스, 로드밸런스, 네트워크
      - [서비스](./Kubernetes/05_ServicesLoadBalancingNetworking/01_Service.md)

## [HTTP](./HTTP)

- 튜토리얼
  - [HTTP](./HTTP/01_Tutorials/01_Overview.md)
  - [HTTP caching](./HTTP/01_Tutorials/02_HTTPCaching.md)
  - [HTTP cookies](./HTTP/01_Tutorials/03_HTTPCookies.md)
  - [CORS](./HTTP/01_Tutorials/04_CrossOriginResourceSharing.md)
  - [Evolution of HTTP](./HTTP/01_Tutorials/05_EvolutionOfHTTP.md)
  - [HTTP Messages](./HTTP/01_Tutorials/07_HTTPMessages.md)
  - [A typical HTTP session](./HTTP/01_Tutorials/08_ATypicalHTTPSession.md)
- 데이터 전송방식
  - [REST](./HTTP/02_DataTransmission/01_REST.md)

## [Java Reactor](./JavaReactor)

- Reactive 프로그래밍 소개
  - [Reactive 프로그래밍 소개](./JavaReactor/01_IntoductionToReactiveProgramming.md)
  - [Reactor Core 특징](./JavaReactor/02_ReactorCoreFeatures.md)

## [Netty](./Netty)

- 네티 개념과 아키텍처
  - 네티: 비동기식 이벤트 기반 네트워크 프레임워크
    - [자바의 네트워킹](./Netty/01_NettyConceptsAndArchitecture/01_AsynchronousAndEventDriven/01_NetworkingInJava.md)
    - [네티 소개](./Netty/01_NettyConceptsAndArchitecture/01_AsynchronousAndEventDriven/02_IntroducingNetty.md)
    - [네티의 핵심 컴포넌트](./Netty/01_NettyConceptsAndArchitecture/01_AsynchronousAndEventDriven/03_NettyCoreComponents.md)

## [gRPC](./gRPC)

- gRPC
  - [gRPC 소개](./gRPC/01_Introduction.md)
  - [핵심 개념, 아키텍쳐, 라이프사이클](./gRPC/02_CoreConcepts_Architecture_Lifecycle.md)

## [Kafka](./Kafka)

- 시작하기
  - [소개](./Kafka/01_GettingStarted/01_Introduction.md)
  - [사용예시](./Kafka/01_GettingStarted/02_UseCases.md)
- 디자인
  - [동기](./Kafka/04_Design/01_Motivation.md)
