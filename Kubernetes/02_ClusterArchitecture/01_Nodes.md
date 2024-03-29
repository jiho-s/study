## 노드

쿠버네티스는 컨테이너를 파드내에 배치하고 *노드* 에서 파드를 실행함으로 워크로드를 구동한다. 노드는 클러스터에 따라 가상 또는 물리적 머신일 수 있다. 각 노드는 [컨트롤 플레인](https://kubernetes.io/ko/docs/reference/glossary/?all=true#term-control-plane)에 의해 관리되며 [파드](https://kubernetes.io/ko/docs/concepts/workloads/pods/)를 실행하는 데 필요한 서비스를 포함한다.

일반적으로 클러스터에는 여러 개의 노드가 있으며, 학습 또는 리소스가 제한되는 환경에서는 하나만 있을 수도 있다.

노드의 구성요소에는 [kubelet](https://kubernetes.io/docs/reference/generated/kubelet), [컨테이너 런타임](https://kubernetes.io/docs/setup/production-environment/container-runtimes), [kube-proxy](https://kubernetes.io/docs/reference/command-line-tools-reference/kube-proxy/)가 있다.

### 관리

api 서버에 노드를 추가하는 두가지 주요 방법이 있다.

1. 노드의 kubelet으로 컨트롤 플레인에 자체 등록
2. 사용자 또는 다른 사용자가 노드 오브젝트를 수동으로 추가

노드 오브젝트 또는 노드의 kubelet으로 자체 등록한 후 컨트롤 플레인은 새 노드 오브젝트가 유효한지 확인한다. 예를 들어 다음 JSON 매니페스트에서 노드를 만들려는 경우이다.

```json
{
  "kind": "Node",
  "apiVersion": "v1",
  "metadata": {
    "name": "10.240.79.157",
    "labels": {
      "name": "my-first-k8s-node"
    }
  }
}
```

쿠버네티스는 내부적으로 노드 오브젝트를 생성한다. 쿠버네티스는 kubelet이 노드의 `metadata.name` 필드와 일치하는 API 서버에 등록이 되어있는지 확인한다. 노드가 정상이면(필요한 모든 서비스가 실행중인 경우) 파드를 실행할 수 있게 된다. 그렇지 않으면, 해당 노드는 정상이 될때까지 모든 클러스터 활동에 대해 무시된다.

#### 노드 네임 고유성

name으로 노드를 식별한다. 두개의 노드는 동시에 같은 이름을 가질 수 없다. 쿠버네티스는 같은 이름을 가진 리소스는 같은 오브젝트라고 생각한다. 노드의 경우 같은 이름을 가지면 같은 상태를 갖는다고 생각한다. 인스턴스가 이름 변경 없이 수정된 경우 모순을 일으킨다. 노드를 교체하거나 크게 업데이트해야 하는 경우 먼저 기존 노드 개체를 API 서버에서 제거한 후 업데이트 후 다시 추가해야 한다.



#### 노드에 대한 자체-등록

kubelet 플래그 `--register-node`는 참(기본값)일 경우, kubelet 은 API 서버에 스스로 등록을 시도할 것이다. 이는 대부분의 배포판에 의해 이용되는, 선호하는 패턴이다.

자체-등록에 대해, kubelet은 다음 옵션과 함께 시작된다.

- `--kubeconfig` - apiserver에 스스로 인증하기 위한 자격증명에 대한 경로.

- `--cloud-provider` - 자신에 대한 메터데이터를 읽기 위해 어떻게 [클라우드 제공자](https://kubernetes.io/ko/docs/reference/glossary/?all=true#term-cloud-provider)와 소통할지에 대한 방법.

- `--register-node` - 자동으로 API 서버에 등록.

- `--register-with-taints` - 주어진 [테인트(taint)](https://kubernetes.io/docs/concepts/scheduling-eviction/taint-and-toleration/) 리스트(콤마로 분리된 `<key>=<value>:<effect>`)를 가진 노드 등록.

  `register-node`가 거짓이면 동작 안 함.

- `--node-ip` - 노드의 IP 주소.

- `--node-labels` - 클러스터에 노드를 등록할 때 추가 할 [레이블](https://kubernetes.io/ko/docs/concepts/overview/working-with-objects/labels)([NodeRestriction admission plugin](https://kubernetes.io/docs/reference/access-authn-authz/admission-controllers/#noderestriction)에 의해 적용되는 레이블 제한 사항 참고).

- `--node-status-update-frequency` - 얼마나 자주 kubelet이 마스터에 노드 상태를 게시할 지 정의.

[Node authorization mode](https://kubernetes.io/docs/reference/access-authn-authz/node/)와 [NodeRestriction admission plugin](https://kubernetes.io/docs/reference/access-authn-authz/admission-controllers/#noderestriction)이 활성화 되면, kubelets 은 자신의 노드 리소스를 생성/수정할 권한을 가진다.

#### 수동 노드 관리

[kubectl](https://kubernetes.io/docs/user-guide/kubectl-overview/)을 사용해서 노드 오브젝트를 생성하고 수정할 수 있다.

노드 오브젝트를 수동으로 생성하려면 kubelet 플래그를 `--register-node=false` 로 설정한다.

`--register-node` 설정과 관계 없이 노드 오브젝트를 수정할 수 있다. 예를 들어 기존 노드에 레이블을 설정하거나, 스케줄 불가로 표시할 수 있다.

파드의 노드 셀렉터와 함께 노드의 레이블을 사용해서 스케줄링을 제어할 수 있다. 예를 들어, 사용 가능한 노드의 하위 집합에서만 실행되도록 파드를 제한할 수 있다.

노드를 스케줄 불가로 표시하면 스케줄러가 해당 노드에 새 파드를 배치할 수 없지만, 노드에 있는 기존 파드에는 영향을 미치지 않는다. 이는 노드 재부팅 또는 기타 유지보수 준비 단계에서 유용하다.

노드를 스케줄 불가로 표시하려면 다음을 실행한다.

```shell
kubectl cordon $NODENAME
```

### 노드 상태

노드의 상태는 다음의 정보를 포함한다.

- 주소
- 컨디션
- 용량과 할당가능
- 정보

`kubectl` 을 사용해서 노드 상태와 기타 세부 정보를 볼수 있다.

```shell
kubectl describe node <insert-node-name-here>
```

#### 주소

이 필드의 용법은 클라우드 제공사업자 또는 하드웨어 구성에 따라 다양하다.

- HostName: 노드의 커널에 의해 알려진 호스트명이다. `--hostname-override` 파라미터를 통해 치환될 수 있다.
- ExternalIP: 일반적으로 노드의 IP 주소는 외부로 라우트 가능 (클러스터 외부에서 이용 가능) 하다 .
- InternalIP: 일반적으로 노드의 IP 주소는 클러스터 내에서만 라우트 가능하다.

##### 컨디션

`conditions` 필드는 모든 `Running` 노드의 상태를 기술한다. 컨디션의 예로 다음을 포함한다.

| 노드 컨디션          | 설명                                                         |
| -------------------- | ------------------------------------------------------------ |
| `Ready`              | 노드가 상태 양호하며 파드를 수용할 준비가 되어 있는 경우 `True`, 노드의 상태가 불량하여 파드를 수용하지 못할 경우 `False`, 그리고 노드 컨트롤러가 마지막 `node-monitor-grace-period` (기본값 40 기간 동안 노드로부터 응답을 받지 못한 경우) `Unknown` |
| `DiskPressure`       | 디스크 사이즈 상에 압박이 있는 경우, 즉 디스크 용량이 넉넉치 않은 경우 `True`, 반대의 경우 `False` |
| `MemoryPressure`     | 노드 메모리 상에 압박이 있는 경우, 즉 노드 메모리가 넉넉치 않은 경우 `True`, 반대의 경우 `False` |
| `PIDPressure`        | 프로세스 상에 압박이 있는 경우, 즉 노드 상에 많은 프로세스들이 존재하는 경우 `True`, 반대의 경우 `False` |
| `NetworkUnavailable` | 노드에 대해 네트워크가 올바르게 구성되지 않은 경우 `True`, 반대의 경우 `False` |

노드 컨디션은 JSON 오브젝트로 표현된다. 예를 들어, 다음 응답은 상태 양호한 노드를 나타낸다.

```json
"conditions": [
  {
    "type": "Ready",
    "status": "True",
    "reason": "KubeletReady",
    "message": "kubelet is posting ready status",
    "lastHeartbeatTime": "2019-06-05T18:38:35Z",
    "lastTransitionTime": "2019-06-05T11:41:27Z"
  }
]
```

ready 컨디션의 상태가 `pod-eviction-timeout` ([kube-controller-manager](https://kubernetes.io/docs/reference/command-line-tools-reference/kube-controller-manager/)에 전달된 인수) 보다 더 길게 `Unknown` 또는 `False`로 유지되는 경우, 노드 상에 모든 파드는 노드 컨트롤러에 의해 삭제되도록 스케줄 된다. 기본 축출 타임아웃 기간은 **5분** 이다. 노드에 접근이 불가할 때와 같은 경우, apiserver는 노드 상의 kubelet과 통신이 불가하다. apiserver와의 통신이 재개될 때까지 파드 삭제에 대한 결정은 kubelet에 전해질 수 없다. 그 사이, 삭제되도록 스케줄 되어진 파드는 분할된 노드 상에서 계속 동작할 수도 있다.

노드 컨트롤러가 클러스터 내 동작 중지된 것을 확신할 때까지는 파드를 강제로 삭제하지 않는다. 파드가 `Terminating` 또는 `Unknown` 상태로 있을 때 접근 불가한 노드 상에서 동작되고 있는 것을 보게 될 수도 있다. 노드가 영구적으로 클러스터에서 삭제되었는지에 대한 여부를 쿠버네티스가 기반 인프라로부터 유추할 수 없는 경우, 노드가 클러스터를 영구적으로 탈퇴하게 되면, 클러스터 관리자는 손수 노드 오브젝트를 삭제해야 할 수도 있다. 쿠버네티스에서 노드 오브젝트를 삭제하면 노드 상에서 동작중인 모든 파드 오브젝트가 apiserver로부터 삭제되어 그 이름을 사용할 수 있는 결과를 낳는다.

노드 수명주기 컨트롤러는 자동으로 컨디션을 나타내는 [테인트(taints)](https://kubernetes.io/ko/docs/concepts/scheduling-eviction/taint-and-toleration/)를 생성한다. 스케줄러는 파드를 노드에 할당 할 때 노드의 테인트를 고려한다. 또한 파드는 노드의 테인트를 극복(tolerate)할 수 있는 톨러레이션(toleration)을 가질 수 있다.

자세한 내용은 [컨디션별 노드 테인트하기](https://kubernetes.io/ko/docs/concepts/scheduling-eviction/taint-and-toleration/#컨디션별-노드-테인트하기)를 참조한다.

#### 용량과 할당가능

노드 상에 사용 가능한 리소스를 나타낸다. 리소스에는 CPU, 메모리 그리고 노드 상으로 스케줄 되어질 수 있는 최대 파드 수가 있다.

용량 블록의 필드는 노드에 있는 리소스의 총량을 나타낸다. 할당가능 블록은 일반 파드에서 사용할 수 있는 노드의 리소스 양을 나타낸다.

노드에서 [컴퓨팅 리소스 예약](https://kubernetes.io/docs/tasks/administer-cluster/reserve-compute-resources/#node-allocatable)하는 방법을 배우는 동안 용량 및 할당가능 리소스에 대해 자세히 읽어보자.

#### 정보

커널 버전, 쿠버네티스 버전 (kubelet과 kube-proxy 버전), (사용하는 경우) Docker 버전, OS 이름과 같은노드에 대한 일반적인 정보를 보여준다. 이 정보는 Kubelet에 의해 노드로부터 수집된다.

#### 하트비트

쿠버네티스 노드에서 보내는 하트비트는 노드의 가용성을 결정하는데 도움이 된다.

하트비트의 두 가지 형태는 `NodeStatus` 와 [리스(Lease) 오브젝트](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.20/#lease-v1-coordination-k8s-io)이다. 각 노드에는 `kube-node-lease` 라는 [네임스페이스](https://kubernetes.io/ko/docs/concepts/overview/working-with-objects/namespaces) 에 관련된 리스 오브젝트가 있다. 리스는 경량 리소스로, 클러스터가 확장될 때 노드의 하트비트 성능을 향상 시킨다.

kubelet은 `NodeStatus` 와 리스 오브젝트를 생성하고 업데이트 할 의무가 있다.

- kubelet은 상태가 변경되거나 구성된 상태에 대한 업데이트가 없는 경우, `NodeStatus` 를 업데이트 한다. `NodeStatus` 의 기본 업데이트 주기는 5분이다(연결할 수 없는 노드의 시간 제한인 40초 보다 훨씬 길다).
- kubelet은 10초마다 리스 오브젝트를 생성하고 업데이트 한다(기본 업데이트 주기). 리스 업데이트는 `NodeStatus` 업데이트와는 독립적으로 발생한다. 리스 업데이트가 실패하면 kubelet에 의해 재시도하며 7초로 제한된 지수 백오프를 200 밀리초에서 부터 시작한다.

#### 안정성

대부분의 경우, 노드 컨트롤러는 초당 `--node-eviction-rate`(기본값 0.1)로 축출 비율을 제한한다. 이 말은 10초당 1개의 노드를 초과하여 파드 축출을 하지 않는다는 의미가 된다.

노드 축출 행위는 주어진 가용성 영역 내 하나의 노드가 상태가 불량할 경우 변화한다. 노드 컨트롤러는 영역 내 동시에 상태가 불량한 노드의 퍼센티지가 얼마나 되는지 체크한다(NodeReady 컨디션은 ConditionUnknown 또는 ConditionFalse 다.). 상태가 불량한 노드의 일부가 최소 `--unhealthy-zone-threshold` 기본값 0.55) 가 되면 축출 비율은 감소한다. 클러스터가 작으면 (즉 `--large-cluster-size-threshold` 노드 이하면 - 기본값 50) 축출은 중지되고, 그렇지 않으면 축출 비율은 초당`--secondary-node-eviction-rate`(기본값 0.01)로 감소된다. 이 정책들이 가용성 영역 단위로 실행되어지는 이유는 나머지가 연결되어 있는 동안 하나의 가용성 영역이 마스터로부터 분할되어 질 수도 있기 때문이다. 만약 클러스터가 여러 클라우드 제공사업자의 가용성 영역에 걸쳐 있지 않으면, 오직 하나의 가용성 영역만 (전체 클러스터) 존재하게 된다.

노드가 가용성 영역들에 걸쳐 퍼져 있는 주된 이유는 하나의 전체 영역이 장애가 발생할 경우 워크로드가 상태 양호한 영역으로 이전되어질 수 있도록 하기 위해서이다. 그러므로, 하나의 영역 내 모든 노드들이 상태가 불량하면 노드 컨트롤러는 `--node-eviction-rate` 의 정상 비율로 축출한다. 코너 케이스란 모든 영역이 완전히 상태불량 (즉 클러스터 내 양호한 노드가 없는 경우) 한 경우이다. 이러한 경우, 노드 컨트롤러는 마스터 연결에 문제가 있어 일부 연결이 복원될 때까지 모든 축출을 중지하는 것으로 여긴다.

또한, 노드 컨트롤러는 파드가 테인트를 허용하지 않을 때 `NoExecute` 테인트 상태의 노드에서 동작하는 파드에 대한 축출 책임을 가지고 있다. 추가로, 노드 컨틀로러는 연결할 수 없거나, 준비되지 않은 노드와 같은 노드 문제에 상응하는 [테인트](https://kubernetes.io/docs/concepts/scheduling-eviction/taint-and-toleration/)를 추가한다. 이는 스케줄러가 비정상적인 노드에 파드를 배치하지 않게 된다.

### 노드 용량

노드 오브젝트는 노드 리소스 용량에 대한 정보(예: 사용 가능한 메모리의 양과 CPU의 수)를 추적한다. 노드의 [자체 등록](https://kubernetes.io/ko/docs/concepts/architecture/nodes/#노드에-대한-자체-등록)은 등록하는 중에 용량을 보고한다. [수동](https://kubernetes.io/ko/docs/concepts/architecture/nodes/#수동-노드-관리)으로 노드를 추가하는 경우 추가할 때 노드의 용량 정보를 설정해야 한다.

쿠버네티스 [스케줄러](https://kubernetes.io/docs/reference/generated/kube-scheduler/)는 노드 상에 모든 노드에 대해 충분한 리소스가 존재하도록 보장한다. 스케줄러는 노드 상에 컨테이너에 대한 요청의 합이 노드 용량보다 더 크지 않도록 체크한다. 요청의 합은 kubelet에서 관리하는 모든 컨테이너를 포함하지만, 컨테이너 런타임에 의해 직접적으로 시작된 컨 테이너는 제외되고 kubelet의 컨트롤 범위 밖에서 실행되는 모든 프로세스도 제외된다.

### 노드 컨트롤러

노드 [컨트롤러](https://kubernetes.io/ko/docs/concepts/architecture/controller/)는 노드의 다양한 측면을 관리하는 쿠버네티스 컨트롤 플레인 컴포넌트이다.

노드 컨트롤러는 노드가 생성되어 유지되는 동안 다양한 역할을 한다. 

첫째는 등록 시점에 (CIDR 할당이 사용토록 설정된 경우) 노드에 CIDR 블럭을 할당하는 것이다.

두 번째는 노드 컨트롤러의 내부 노드 리스트를 클라우드 제공사업자의 사용 가능한 머신 리스트 정보를 근거로 최신상태로 유지하는 것이다. 클라우드 환경에서 동작 중일 경우, 노드상태가 불량할 때마다, 노드 컨트롤러는 해당 노드용 VM이 여전히 사용 가능한지에 대해 클라우드 제공사업자에게 묻는다. 사용 가능하지 않을 경우, 노드 컨트롤러는 노드 리스트로부터 그 노드를 삭제한다.

세 번째는 노드의 동작 상태를 모니터링 하는 것이다. 노드 컨트롤러는 노드가 접근 불가할 경우 (즉 노드 컨트롤러가 어떠한 사유로 하트비트 수신을 중지하는 경우, 예를 들어 노드 다운과 같은 경우이다.) NodeStatus의 NodeReady 컨디션을 ConditionUnknown으로 업데이트 하는 책임을 지고, 노드가 계속 접근 불가할 경우 나중에 노드로부터 (정상적인 종료를 이용하여) 모든 파드를 축출시킨다. (ConditionUnknown을 알리기 시작하는 기본 타임아웃 값은 40초 이고, 파드를 축출하기 시작하는 값은 5분이다.) 노드 컨트롤러는 매 `--node-monitor-period` 초 마다 각 노드의 상태를 체크한다.