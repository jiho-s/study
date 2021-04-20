# Java

> 자바 공부한 내용 정리

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)[![Java: 11](https://img.shields.io/badge/Java-11-brightgreen)](https://docs.oracle.com/en/java/javase/11/books.html)

## 목차

1. [Garbage Collector](#garbage-collector)
3. [Lambda](#lambda)
4. [Java Virtual Machine](#java-virtual-machine)
5. [java.lang 패키지](#java.lang-패키지)
5. [java.util 패키지](#java.util-패키지)

## [Garbage Collector](./GarbageCollector)

- [Garbage Collector](./GarbageCollector/1_GarbageCollector.md)
- [Garbage Collector Implementation](./GarbageCollector/2_GarbageCollectorImplementation.md)
- [Available Collector](./GarbageCollector/3_AvailableCollectors)
- [Parallel Collector](./GarbageCollector/4_ParallelCollector.md)
- [Concurrent Mark Sweep(CMS) Collector](./GarbageCollector/5_ConcurrentMarkSweepCollector.md)
- [Garbage-First Garbage Collector](./GarbageCollector/6_GarbageFirstGarbageCollector.md)

## [Java Language](./JavaLanguage)

- [Lambda](./JavaLanguage/01_Lambda.md)
-  [Generics](./JavaLanguage/02_Generics.md)

## [Java Virtual Machine](./JavaVirtualMachine)

- [소개](./JavaVirtualMachine/01_Intruduction.md)
- Java Virtual Machine의 구조
  - [데이터 타입](./JavaVirtualMachine/02_StructureOfJVM/01_DataType.md)
  - [기본 타입과 값](./JavaVirtualMachine/02_StructureOfJVM/02_PrimitiveTypesAndValues.md)
  - [참조 타입과 값](./JavaVirtualMachine/02_StructureOfJVM/03_ReferenceTypesAndValues.md)
  - [런타임 데이터 영역](./JavaVirtualMachine/02_StructureOfJVM/04_Run-TimeDataAreas.md)
  - [프레임](./JavaVirtualMachine/02_StructureOfJVM/05_Frams.md)
- 로딩, 링킹, 초기화
  - [소개](./JavaVirtualMachine/05_LoadingLinkingAndInitializing/01_Overview.md)
  - [런타임 상수 풀](./JavaVirtualMachine/05_LoadingLinkingAndInitializing/02_TheRun-TimeConstantPool.md)
  - [자바 가상머신 시작](./JavaVirtualMachine/05_LoadingLinkingAndInitializing/03_JavaVirtualMachineStartup.md)
  - [생성과 로딩](./JavaVirtualMachine/05_LoadingLinkingAndInitializing/04_CreationAndLoading.md)
  - [링킹](./JavaVirtualMachine/05_LoadingLinkingAndInitializing/05_Linking.md)
  - [초기화](./JavaVirtualMachine/05_LoadingLinkingAndInitializing/06_Initialization.md)

## [java.lang 패키지](./lang)

- [Wrapper Classes](./lang/01_WrapperClasses.md)
- [Reflect](./lang/02_Reflect)
  - [Reflection API](./lang/02_Reflect/01_Overview.md)
  - [Classes](./lang/02_Reflect/02_Classes.md)
  - [Members](./lang/02_Reflect/03_Members.md)

## [java.util 패키지](./util)

- Stream
  - [Stream](./util/Stream/01_Stream.md)
- [Concurrent](./util/Concurrent)
  - [Flow](./util/Concurrent/01_Flow.md)