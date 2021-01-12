# Garbage Collector

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![Java: 11](https://img.shields.io/badge/Java-11-brightgreen)](https://docs.oracle.com/en/java/javase/11/books.html)

## 목차

1. [Garbage Collector](#garbage-collector)
2. [Garbage Collector Implementation](#garbage-collector-implementation)
3. [Availabe Collectors](#available-collector)
4. [Parallel Collector](#parallel-collector)
5. [Concurrent Mark Sweep(CMS) Collector](#concurrent-mark-sweep(cms)-collector)

## [Garbage Collector](./1_GarbageCollector.md)

- Garbage Collector

## [Garbage Collector Implementation](./2_GarbageCollectorImplementation.md)

- 세대별 가비지 수집

- 세대

- 성능 고려사항

## [Availabe Collectors](./3_AvailableCollectors)

- Serial Collector
- Parallel Collector
- Mostly Concurrent Collectors
- Z Garbage Collector

## [Parallel Collector](./4_ParallelCollector.md)

- 스레드 개수
- Parallel Collector 설정

## [Concurrent Mark Sweep(CMS) Collector](./5_ConcurrentMarkSweepCollector.md)

- CMS 컬렉터의 성능과 구조

- 동시 수집 모드 실패
- CMS 컬렉터와 플로팅 가비지
- CMS 컬렉터 일시 중지
- CMS 컬렉터 동시 추적
- 동시 수집 사이클 시작

