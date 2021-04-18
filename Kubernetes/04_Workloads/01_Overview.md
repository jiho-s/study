# 워크로드

워크로드는 쿠버네티스에서 구동되는 애플리케이션이다. 쿠버네티스에서는 워크로드를  `Pod`  집합 내에서 실행한다.  `Pod` 는 클러스터에서 실행 중인 컨테이너 집합을 나타낸다.

쿠버네티스 파드에는 [정의된 라이프사이클](https://kubernetes.io/ko/docs/concepts/workloads/pods/pod-lifecycle/)이 있다. 예를 들어, 일단 파드가 클러스터에서 실행되고 나서 해당 파드가 동작 중인 [노드](https://kubernetes.io/ko/docs/concepts/architecture/nodes/)에 심각한 오류가 발생하면 해당 노드의 모든 파드가 실패한다. 쿠버네티스는 이 수준의 실패를 최종(final)으로 취급한다. 사용자는 향후 노드가 복구되는 것과 상관 없이 `Pod` 를 새로 생성해야 한다.

그러나, 각 `Pod` 를 직접 관리할 필요는 없도록 만들었다. 대신, 사용자를 대신하여 파드 집합을 관리하는 *워크로드 리소스* 를 사용할 수 있다. 이러한 리소스는 지정한 상태와 일치하도록 올바른 수의 올바른 파드 유형이 실행되고 있는지 확인하는 컨트롤러를 구성한다.

쿠버네티스는 다음과 같이 여러 가지 빌트인(built-in) 워크로드 리소스를 제공한다.

- [`Deployment`](https://kubernetes.io/ko/docs/concepts/workloads/controllers/deployment/) 및 [`ReplicaSet`](https://kubernetes.io/ko/docs/concepts/workloads/controllers/replicaset/). `Deployment` 는 클러스터의 상태가 없는 애플리케이션 워크로드를 관리하기에 적합하다. `Deployment`에 있는 `Pod`는  필요 시 교체 또는 상호 교체 가능하다.
- [`StatefulSet`](https://kubernetes.io/ko/docs/concepts/workloads/controllers/statefulset/)는 스테이트(state)를 추적하는 하나 이상의 파드를 동작하게 해준다. 예를 들면, 워크로드가 데이터를 영속적으로 기록하는 경우, 사용자는 `Pod` 와 [`PersistentVolume`](https://kubernetes.io/ko/docs/concepts/storage/persistent-volumes/)을 연계하는 `StatefulSet` 을 실행할 수 있다. 전체적인 복원력 향상을 위해서, `StatefulSet` 의 `Pods` 에서 동작 중인 코드는 동일한 `StatefulSet` 의 다른 `Pods` 로 데이터를 복제할 수 있다.
- [`DaemonSet`](https://kubernetes.io/ko/docs/concepts/workloads/controllers/daemonset/)은 노드-로컬 기능(node-local facilities)을 제공하는 `Pods` 를 정의한다. 이러한 기능들은 클러스터를 운용하는 데 기본적인 것일 것이다. 예를 들면, 네트워킹 지원 도구 또는 [add-on](https://kubernetes.io/docs/concepts/cluster-administration/addons/)등이 있다. `DaemonSet` 의 명세에 맞는 노드를 클러스터에 추가할 때마다, 컨트롤 플레인은 해당 신규 노드에 `DaemonSet` 을 위한 `Pod` 를 스케줄한다.
- [`Job`](https://kubernetes.io/ko/docs/concepts/workloads/controllers/job/) 및 [`CronJob`](https://kubernetes.io/ko/docs/concepts/workloads/controllers/cron-jobs/)은 실행 완료 후 중단되는 작업을 정의한다. `CronJobs` 이 스케줄에 따라 반복되는 반면, 잡은 단 한 번의 작업을 나타낸다.

더 넓은 쿠버네티스 에코시스템 내에서는 추가적인 동작을 제공하는 서드파티의 워크로드 리소스도 찾을 수 있다. [커스텀 리소스 정의](https://kubernetes.io/ko/docs/concepts/extend-kubernetes/api-extension/custom-resources/)를 사용하면, 쿠버네티스 코어에서 제공하지 않는 특별한 동작을 원하는 경우 서드파티의 워크로드 리소스를 추가할 수 있다. 예를 들어, 사용자 애플리케이션을 위한 `Pods` 의 그룹을 실행하되 *모든* 파드가 가용한 경우가 아닌 경우 멈추고 싶다면(아마도 높은 처리량의 분산 처리를 하는 상황 같은), 사용자는 해당 기능을 제공하는 확장을 구현하거나 설치할 수 있다.