## 쿠버네티스 API

쿠버네티스 [컨트롤 플레인](https://kubernetes.io/ko/docs/reference/glossary/?all=true#term-control-plane)의 핵심은 [API 서버](https://kubernetes.io/ko/docs/concepts/overview/components/#kube-apiserver)이다. API 서버는 최종 사용자, 클러스터의 다른 부분 그리고 외부 컴포넌트가 서로 통신할 수 있도록 HTTP API를 제공한다.

쿠버네티스 API를 사용하면 쿠버네티스의 API 오브젝트(예: 파드(Pod), 네임스페이스(Namespace), 컨피그맵(ConfigMap) 그리고 이벤트(Event))를 질의(query)하고 조작할 수 있다.

대부분의 작업은 [kubectl](https://kubernetes.io/docs/reference/kubectl/overview/) 커맨드 라인 인터페이스 또는 API를 사용하는 [kubeadm](https://kubernetes.io/ko/docs/reference/setup-tools/kubeadm/)과 같은 다른 커맨드 라인 도구를 통해 수행할 수 있다. 그러나, REST 호출을 사용하여 API에 직접 접근할 수도 있다.

쿠버네티스 API를 사용하여 애플리케이션을 작성하는 경우 [클라이언트 라이브러리](https://kubernetes.io/docs/reference/using-api/client-libraries/) 중 하나를 사용하는 것이 좋다

### OpenAPI 명세

쿠버네티스 API 서버는 `/openAPI/v2` 를 통해 OpenAPI 스팩을 따른다.

쿠버네티스는 주로 클러스터 내부 통신을 위해 대안적인 Protobuf에 기반한 직렬화 형식을 구현한다

### 영속성

쿠버네티스는 오브젝트의 직렬화된 상태를 etcd에 기록하여 저장한다.

### API 그룹과 버전 규칙

필드를 쉽게 제거하거나 리소스 표현을 재구성하기 위해, 쿠버네티스는 각각 `/api/v1` 또는 `/apis/rbac.authorization.k8s.io/v1alpha1` 과 같은 여러 API 버전을 지원한다.

버전 규칙은 리소스나 필드 수준이 아닌 API 수준에서 수행되어 API가 시스템 리소스 및 동작에 대한 명확하고 일관된 보기를 제공하고 수명 종료 및/또는 실험적 API에 대한 접근을 제어할 수 있도록 한다

보다 쉽게 API를 발전하고 확장하기 위해, 쿠버네티스는 활성화 또는 비활성화가 가능한 API 그룹을 구현한다.

API 리소스는 API 그룹, 리소스 타입, 네임스페이스 및 이름으로 구분된다. API 서버는 API 버전 간의 변환을 투명하게 처리한다. 서로 다른 모든 버전은 실제로 동일한 영속 데이터의 표현이다. API 서버는 여러 API 버전을 통해 동일한 기본 데이터를 제공할 수 있다.

#### API 변경

성공적인 시스템은 새로운 유스케이스가 등장하거나 기존 사례가 변경됨에 따라 성장하고 변화해야 한다. 따라서, 쿠버네티스는 쿠버네티스 API가 지속적으로 변경되고 성장할 수 있도록 설계했다. 쿠버네티스 프로젝트는 기존 클라이언트와의 호환성을 깨지 *않고* 다른 프로젝트가 적응할 기회를 가질 수 있도록 장기간 해당 호환성을 유지하는 것을 목표로 한다.

일반적으로, 새 API 리소스와 새 리소스 필드는 자주 추가될 수 있다. 리소스 또는 필드를 제거하려면 API 지원 중단 정책을 따라야 한다.

쿠버네티스는 일반적으로 API 버전 `v1` 에서 안정 버전(GA)에 도달하면, 공식 쿠버네티스 API에 대한 호환성 유지를 강력하게 이행한다. 또한, 쿠버네티스는 가능한 경우 *베타* API 버전에서도 호환성을 유지한다. 베타 API를 채택하면 기능이 안정된 후에도 해당 API를 사용하여 클러스터와 계속 상호 작용할 수 있다.

### API 확장

쿠버네티스 API는 다음 두 가지 방법 중 하나로 확장할 수 있다.

1. [커스텀 리소스](https://kubernetes.io/ko/docs/concepts/extend-kubernetes/api-extension/custom-resources/)를 사용하면 API 서버가 선택한 리소스 API를 제공하는 방법을 선언적으로 정의할 수 있다.
2. [애그리게이션 레이어(aggregation layer)](https://kubernetes.io/ko/docs/concepts/extend-kubernetes/api-extension/apiserver-aggregation/)를 구현하여 쿠버네티스 API를 확장할 수도 있다