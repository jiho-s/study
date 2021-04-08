# 쿠버네티스

> 쿠버네티스 공부한 내용 정리
>
> [쿠버네티스 레퍼런스]: https://kubernetes.io/ko/docs/

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 목차

1. [개념](#개념)

## 개념

- 개요
  - [쿠버네티스란 무엇인가?](./01_Concepts/01_Overview/01_WhatIsKubernetes.md)
  - [쿠버네티스의 구성요소](./01_Concepts/01_Overview/02_KubernetesComponents.md)
  - [쿠버네티스 API](./01_Concepts/01_Overview/03_TheKubernetesAPI.md)
  - 쿠버네티스 오브젝트로 작업하기
    - [쿠버네티스 오브젝트 이해하기](./01_Concepts/01_Overview/04_WorkingWithKubernetesObjects/01_UnderstandingKubernetesObjects.md)
    - [쿠버네티스 오브젝트 관리](./01_Concepts/01_Overview/04_WorkingWithKubernetesObjects/02_KubernetesObjectManagement.md)
    - [오브젝트 이름과 ID](./01_Concepts/01_Overview/04_WorkingWithKubernetesObjects/03_ObjectNamesAndIDs.md)
    - [네임스페이스](./01_Concepts/01_Overview/04_WorkingWithKubernetesObjects/04_Namespaces.md)
    - [레이블과 셀렉터](./01_Concepts/01_Overview/04_WorkingWithKubernetesObjects/05_LabelsAndSelectors.md)

- 클러스터 아키텍처
  - [노드](./02_ClusterArchitecture/01_Nodes.md)
  - [컨트롤 플레인-노드 간 통신](./02_ClusterArchitecture/02_ControlPlane_NodeCommunication.md)
  - [컨트롤러](./02_ClusterArchitecture/03_Controllers.md)