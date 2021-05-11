## 디플로이먼트

디플로이먼트는 파드와 레플리카셋에 대한 선언적 업데이트를 제공한다.

디플로이먼트에서 의도하는 상태를 설명하고, 디플로이먼트 컨트롤러는 현재 상태에서 의도하는 상태로 비율을 조정하며 변경한다. 새 레플리카셋을 생성하거나, 기존의 디플로이먼트를 제거하고 새로운 디플로이먼트에 모든 리소스를 적용하는 디플로이먼트를 정의할 수 있다.

### 사용 예시

다음은 디플로이먼트의 일반적인 사용 예시이다.

- 레플리카셋을 롤아웃 할 디플로이먼트 생성

  레플리카셋은 백그라운드에서 파드를 생성한다.

- 파드의 새로운 상태를 선언

  디플로이먼트를 PodTemplateSpec을 업데이트해서 파드의 새로운 상태를 선언한다. 새로운 레플리카셋이 생성되면, 디플로이먼트는 파드를 기존 레플리카셋에서 새로운 레플리카셋으로 이동하는 것을 관리한다. 각각의 새로운 레플리카셋은 디플로이먼트의 수정 버전에 따라 업데이트한다.

- 디플로이먼트의 이전 버전으로 롤백

  만약 디플로이먼트의 현재 상태가 안정적이지 않은 경우 디플로이먼트의 이전 버전으로 롤백한다. 각 롤백은 디플로이먼트의 수정 버전에 따라 업데이트한다.

- 더 많은 로드를 위해 디플로이먼트의 스케일 업

- 디플로이먼트 일시중지

  디플로이먼트를 일시중지하여, PodTemplateSpec에 여러 수정 사항을 적용하고, 새로운 롤아웃의 시작을 재개한다.

- 디플로이먼트의 상태를 사용

  롤아웃이 막혀있는지를 나타내는 디플로이먼트의 상태를 이용한다.

- 이전 레플리카셋 정지

  더 이상 필요 없는 이전 레플리카셋을 정리한다.

### 디플로이먼트 생성

다음은 디플로이먼트의 예시이다. 예시는 3개의 `nginx` 파드를 불러오기 위한 레플리카셋을 생성한다.

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
  labels:
    app: nginx
spec:
  replicas: 3
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:1.14.2
        ports:
        - containerPort: 80
```

- `.metadata.name` 필드에 따라 `nginx-deployment` 이름으로 디플로이먼트가 생성된다.
- `.spec.replicas` 필드에 따라 디플로이먼트는 3개의 레플리카 파드를 생성한다.
- `.spec.selector` 필드는 디플로이먼트가 관리할 파드를 찾는 방법을 정의한다. 이 사례에서는 파드 템플릿에 정의된 레이블(`app: nginx`)을 선택한다. 그러나 파드 템플릿 자체의 규칙이 만족되는 한, 보다 정교한 선택 규칙의 적용이 가능하다. 
- `template` 필드에는 다음 하위 필드가 포함되어있다.
  - 파드는 `.metadata.labels` 필드를 사용해서 `app: nginx` 라는 레이블을 붙인다.
  - 파드 템플릿의 사양 또는 `.template.spec` 필드는 파드가 [도커 허브](https://hub.docker.com/)의 `nginx`1.14.2 버전 이미지를 실행하는 `nginx` 컨테이너 1개를 실행하는 것을 나타낸다.
  - 컨테이너 1개를 생성하고, `.spec.template.spec.containers[0].name`필드를 사용해서 `nginx` 이름을 붙인다.

시작하기 전에, 쿠버네티스 클러스터가 시작되고 실행 중인지 확인한다. 위의 디플로이먼트를 생성하려면 다음 단계를 따른다.

1. 다음 명령어를 실행해서 디플로이먼트를 생성한다.

   ```shell
   kubectl apply -f nginx-deployment.yaml
   ```

2. `kubectl get deployments` 을 실행해서 디플로이먼트가 생성되었는지 확인한다

   ```bash
   NAME               READY   UP-TO-DATE   AVAILABLE   AGE
   nginx-deployment   0/3     0            0           1s
   ```

   클러스터에서 디플로이먼트를 점검할 때, 다음 필드가 표시된다.

   - `NAME` 은 네임스페이스에 있는 디플로이먼트 이름의 목록이다.
   - `READY` 는 사용자가 사용할 수 있는 애플리케이션의 레플리카의 수를 표시한다. ready/desired 패턴을 따른다.
   - `UP-TO-DATE` 는 의도한 상태를 얻기 위해 업데이트된 레플리카의 수를 표시한다.
   - `AVAILABLE` 은 사용자가 사용할 수 있는 애플리케이션 레플리카의 수를 표시한다.
   - `AGE` 는 애플리케이션의 실행된 시간을 표시한다.

   `.spec.replicas` 필드에 따라 의도한 레플리카의 수가 3개인지 알 수 있다.

3. 디플로이먼트의 롤아웃 상태를 보려면, `kubectl rollout status deployment/nginx-deployment` 를 실행한다.

   다음과 유사하게 출력된다.

   ```shell
   Waiting for rollout to finish: 2 out of 3 new replicas have been updated...
   deployment "nginx-deployment" successfully rolled out
   ```

4. 몇 초 후 `kubectl get deployments` 를 다시 실행한다. 다음과 유사하게 출력된다.

   ```
   NAME               READY   UP-TO-DATE   AVAILABLE   AGE
   nginx-deployment   3/3     3            3           18s
   ```

   디플로이먼트에서 3개의 레플리카가 생성되었고, 모든 레플리카는 최신 상태(최신 파드 템플릿을 포함)이며 사용 가능한 것을 알 수 있다.

5. 디플로이먼트로 생성된 레플리카셋(`rs`)을 보려면, `kubectl get rs` 를 실행한다. 다음과 유사하게 출력된다.

   ```
   NAME                          DESIRED   CURRENT   READY   AGE
   nginx-deployment-75675f5897   3         3         3       18s
   ```

   레플리카셋의 출력에는 다음 필드가 표시된다.

   - `NAME` 은 네임스페이스에 있는 레플리카셋 이름의 목록이다.
   - `DESIRED` 는 디플로이먼트의 생성 시 정의된 의도한 애플리케이션 *레플리카* 의 수를 표시한다. 이것이 *의도한 상태* 이다.
   - `CURRENT` 는 현재 실행 중인 레플리카의 수를 표시한다.
   - `READY` 는 사용자가 사용할 수 있는 애플리케이션의 레플리카의 수를 표시한다.
   - `AGE` 는 애플리케이션의 실행된 시간을 표시한다.

   레플리카셋의 이름은 항상 `[DEPLOYMENT-NAME]-[RANDOM-STRING]` 형식으로 된 것을 알 수 있다. 무작위 문자열은 무작위로 생성되며, `pod-template-hash` 를 시드(seed)로 사용한다.

6. 각 파드에 자동으로 생성된 레이블을 보려면, `kubectl get pods --show-labels`를 실행한다. 다음과 유사하게 출력된다.

   ```
   NAME                                READY     STATUS    RESTARTS   AGE       LABELS
   nginx-deployment-75675f5897-7ci7o   1/1       Running   0          18s       app=nginx,pod-template-hash=3123191453
   nginx-deployment-75675f5897-kzszj   1/1       Running   0          18s       app=nginx,pod-template-hash=3123191453
   nginx-deployment-75675f5897-qqcnn   1/1       Running   0          18s       app=nginx,pod-template-hash=3123191453
   ```

   만들어진 레플리카셋은 실행 중인 3개의 `nginx` 파드를 보장한다.

#### Pod-template-hash 레이블

`pod-template-hash` 레이블은 디플로이먼트 컨트롤러에 의해서 디플로이먼트가 생성 또는 채택한 모든 레플리카셋에 추가된다.

이 레이블은 디플로이먼트의 자식 레플리카셋이 겹치지 않도록 보장한다. 레플리카셋의 `PodTemplate` 을 해싱하고, 해시 결과를 레플리카셋 셀렉터, 파드 템플릿 레이블 및 레플리카셋 이 가질 수 있는 기존의 모든 파드에 레이블 값으로 추가해서 사용하도록 생성한다.

### 디플로이먼트 업데이트

> 디플로이먼트의 파드 템플릿(즉, `.spec.template`)이 변경된 경우에만 디플로이먼트의 롤아웃이 트리거(trigger) 된다. 예를 들면 템플릿의 레이블이나 컨테이너 이미지가 업데이트된 경우이다. 디플로이먼트의 스케일링과 같은 다른 업데이트는 롤아웃을 트리거하지 말아야 한다.

다음 단계에 따라 디플로이먼트를 업데이트한다.

1. `nginx:1.14.2` 이미지 대신 `nginx:1.16.1` 이미지를 사용하도록 nginx 파드를 업데이트 한다.

   ```shell
   kubectl --record deployment.apps/nginx-deployment set image deployment.v1.apps/nginx-deployment nginx=nginx:1.16.1
   ```

   또는 다음의 명령어를 사용한다.

   ```shell
   kubectl set image deployment/nginx-deployment nginx=nginx:1.16.1 --record
   ```

   다음과 유사하게 출력된다.

   ```
   deployment.apps/nginx-deployment image updated
   ```

   대안으로 디플로이먼트를 `edit` 해서 `.spec.template.spec.containers[0].image` 를 `nginx:1.14.2` 에서 `nginx:1.16.1` 로 변경한다.

   ```shell
   kubectl edit deployment.v1.apps/nginx-deployment
   ```

   다음과 유사하게 출력된다.

   ```
   deployment.apps/nginx-deployment edited
   ```

2. 롤아웃 상태를 보려면 다음을 실행한다.

   ```shell
   kubectl rollout status deployment/nginx-deployment
   ```

   이와 유사하게 출력된다.

   ```
   Waiting for rollout to finish: 2 out of 3 new replicas have been updated...
   ```

   또는

   ```
   deployment "nginx-deployment" successfully rolled out
   ```

업데이트된 디플로이먼트에 대해 자세한 정보 보기

- 롤아웃이 성공하면 `kubectl get deployments` 를 실행해서 디플로이먼트를 볼 수 있다. 이와 유사하게 출력된다.

  ```ini
  NAME               READY   UP-TO-DATE   AVAILABLE   AGE
  nginx-deployment   3/3     3            3           36s
  ```

- `kubectl get rs` 를 실행해서 디플로이먼트가 새 레플리카셋을 생성해서 파드를 업데이트 했는지 볼 수 있고, 새 레플리카셋을 최대 3개의 레플리카로 스케일 업, 이전 레플리카셋을 0개의 레플리카로 스케일 다운한다.

  ```shell
  kubectl get rs
  ```

  이와 유사하게 출력된다.

  ```
  NAME                          DESIRED   CURRENT   READY   AGE
  nginx-deployment-1564180365   3         3         3       6s
  nginx-deployment-2035384211   0         0         0       36s
  ```

- `get pods` 를 실행하면 새 파드만 표시된다.

  ```shell
  kubectl get pods
  ```

  이와 유사하게 출력된다.

  ```
  NAME                                READY     STATUS    RESTARTS   AGE
  nginx-deployment-1564180365-khku8   1/1       Running   0          14s
  nginx-deployment-1564180365-nacti   1/1       Running   0          14s
  nginx-deployment-1564180365-z9gth   1/1       Running   0          14s
  ```

  다음에 이러한 파드를 업데이트 하려면 디플로이먼트의 파드 템플릿만 다시 업데이트 하면 된다.

  디플로이먼트는 업데이트되는 동안 일정한 수의 파드만 중단되도록 보장한다. 기본적으로 적어도 의도한 파드 수의 75% 이상이 동작하도록 보장한다(최대 25% 불가).

  또한 디플로이먼트는 의도한 파드 수 보다 더 많이 생성되는 파드의 수를 제한한다. 기본적으로, 의도한 파드의 수 기준 최대 125%까지만 추가 파드가 동작할 수 있도록 제한한다(최대 25% 까지).

  예를 들어, 위 디플로이먼트를 자세히 살펴보면 먼저 새로운 파드를 생성한 다음 이전 파드를 삭제하고, 새로운 파드를 만든 것을 볼 수 있다. 충분한 수의 새로운 파드가 나올 때까지 이전 파드를 죽이지 않으며, 충분한 수의 이전 파드들이 죽기 전까지 새로운 파드를 만들지 않는다. 이것은 최소 2개의 파드를 사용할 수 있게 하고, 최대 4개의 파드를 사용할 수 있게 한다.

- 디플로이먼트의 세부 정보 가져오기

  ```shell
  kubectl describe deployments
  ```

  이와 유사하게 출력된다.

  ```
  Name:                   nginx-deployment
  Namespace:              default
  CreationTimestamp:      Thu, 30 Nov 2017 10:56:25 +0000
  Labels:                 app=nginx
  Annotations:            deployment.kubernetes.io/revision=2
  Selector:               app=nginx
  Replicas:               3 desired | 3 updated | 3 total | 3 available | 0 unavailable
  StrategyType:           RollingUpdate
  MinReadySeconds:        0
  RollingUpdateStrategy:  25% max unavailable, 25% max surge
  Pod Template:
    Labels:  app=nginx
     Containers:
      nginx:
        Image:        nginx:1.16.1
        Port:         80/TCP
        Environment:  <none>
        Mounts:       <none>
      Volumes:        <none>
    Conditions:
      Type           Status  Reason
      ----           ------  ------
      Available      True    MinimumReplicasAvailable
      Progressing    True    NewReplicaSetAvailable
    OldReplicaSets:  <none>
    NewReplicaSet:   nginx-deployment-1564180365 (3/3 replicas created)
    Events:
      Type    Reason             Age   From                   Message
      ----    ------             ----  ----                   -------
      Normal  ScalingReplicaSet  2m    deployment-controller  Scaled up replica set nginx-deployment-2035384211 to 3
      Normal  ScalingReplicaSet  24s   deployment-controller  Scaled up replica set nginx-deployment-1564180365 to 1
      Normal  ScalingReplicaSet  22s   deployment-controller  Scaled down replica set nginx-deployment-2035384211 to 2
      Normal  ScalingReplicaSet  22s   deployment-controller  Scaled up replica set nginx-deployment-1564180365 to 2
      Normal  ScalingReplicaSet  19s   deployment-controller  Scaled down replica set nginx-deployment-2035384211 to 1
      Normal  ScalingReplicaSet  19s   deployment-controller  Scaled up replica set nginx-deployment-1564180365 to 3
      Normal  ScalingReplicaSet  14s   deployment-controller  Scaled down replica set nginx-deployment-2035384211 to 0
  ```

  처음 디플로이먼트를 생성했을 때, 디플로이먼트가 레플리카셋(nginx-deployment-2035384211)을 생성해서 3개의 레플리카로 직접 스케일 업한 것을 볼 수 있다. 디플로이먼트를 업데이트할 때 새 레플리카셋(nginx-deployment-1564180365)을 생성하고, 1개로 스케일 업한 다음 이전 레플리카셋을 2개로 스케일 다운해서, 최소 2개의 파드를 사용할 수 있고 최대 4개의 파드가 항상 생성되어 있도록 하였다. 이후 지속해서 같은 롤링 업데이트 정책으로 새 레플리카셋은 스케일 업하고 이전 레플리카셋은 스케일 다운한다. 마지막으로 새로운 레플리카셋에 3개의 사용 가능한 레플리카가 구성되며, 이전 레플리카셋은 0개로 스케일 다운된다.

#### 롤오버(인-플라이트 다중 업데이트)

디플로이먼트 컨트롤러는 각 시간마다 새로운 디플로이먼트에서 레플리카셋이 의도한 파드를 생성하고 띄우는 것을 주시한다. 만약 디플로이먼트가 업데이트되면, 기존 레플리카셋에서`.spec.selector` 레이블과 일치하는 파드를 컨트롤 하지만, 템플릿과 `.spec.template`이 불일치하면 스케일 다운이 된다. 결국 새로운 레플리카셋은 `.spec.replicas` 로 스케일되고, 모든 기존 레플리카셋은 0개로 스케일된다.

만약 기존 롤아웃이 진행되는 중에 디플로이먼트를 업데이트하는 경우 디플로이먼트가 업데이트에 따라 새 레플리카셋을 생성하고, 스케일 업하기 시작한다. 그리고 이전에 스케일 업 하던 레플리카셋에 롤오버 한다. --이것은 기존 레플리카셋 목록에 추가하고 스케일 다운을 할 것이다.

예를 들어 디플로이먼트로 `nginx:1.14.2` 레플리카를 5개 생성을 한다. 하지만 `nginx:1.14.2` 레플리카 3개가 생성되었을 때 디플로이먼트를 업데이트해서 `nginx:1.16.1` 레플리카 5개를 생성성하도록 업데이트를 한다고 가정한다. 이 경우 디플로이먼트는 즉시 생성된 3개의 `nginx:1.14.2` 파드 3개를 죽이기 시작하고 `nginx:1.16.1`파드를 생성하기 시작한다. 이것은 과정이 변경되기 전 `nginx:1.14.2` 레플리카 5개가 생성되는 것을 기다리지 않는다.

#### 레이블 셀렉터 업데이트

일반적으로 레이블 셀렉터를 업데이트 하는 것을 권장하지 않으며 셀렉터를 미리 계획하는 것을 권장한다. 어떤 경우든 레이블 셀렉터의 업데이트를 해야하는 경우 매우 주의하고, 모든 영향을 파악했는지 확인해야 한다.

- 셀렉터 추가 시 디플로이먼트의 사양에 있는 파드 템플릿 레이블도 새 레이블로 업데이트해야 한다. 그렇지 않으면 유효성 검사 오류가 반환된다. 이 변경은 겹치지 않는 변경으로 새 셀렉터가 이전 셀렉터로 만든 레플리카셋과 파드를 선택하지 않게 되고, 그 결과로 모든 기존 레플리카셋은 고아가 되며, 새로운 레플리카셋을 생성하게 된다.
- 셀렉터 업데이트는 기존 셀렉터 키 값을 변경하며, 결과적으로 추가와 동일한 동작을 한다.
- 셀렉터 삭제는 디플로이먼트 셀렉터의 기존 키를 삭제하며 파드 템플릿 레이블의 변경을 필요로 하지 않는다. 기존 레플리카셋은 고아가 아니고, 새 레플리카셋은 생성되지 않는다. 그러나 제거된 레이블은 기존 파드와 레플리카셋에 여전히 존재한다는 점을 참고해야 한다.

### 디플로이먼트 롤백

기본적으로 모든 디플로이먼트의 롤아웃 기록은 시스템에 남아있어 언제든지 원할 때 롤백이 가능하다 

- 디플로이먼트를 업데이트하는 동안 이미지 이름을 `nginx:1.16.1` 이 아닌 `nginx:1.161` 로 입력해서 오타를 냈다고 가정한다.

  ```shell
  kubectl set image deployment.v1.apps/nginx-deployment nginx=nginx:1.161 --record=true
  ```

  이와 유사하게 출력된다.

  ```
  deployment.apps/nginx-deployment image updated
  ```

- 롤아웃이 고착 된다. 고착된 롤아웃 상태를 확인할 수 있다.

  ```shell
  kubectl rollout status deployment/nginx-deployment
  ```

  이와 유사하게 출력된다.

  ```
  Waiting for rollout to finish: 1 out of 3 new replicas have been updated...
  ```

- Ctrl-C 를 눌러 위의 롤아웃 상태 보기를 중지한다.

- 이전 레플리카는 2개(`nginx-deployment-1564180365` 과 `nginx-deployment-2035384211`), 새 레플리카는 1개(nginx-deployment-3066724191)임을 알 수 있다.

  ```shell
  kubectl get rs
  ```

  이와 유사하게 출력된다.

  ```
  NAME                          DESIRED   CURRENT   READY   AGE
  nginx-deployment-1564180365   3         3         3       25s
  nginx-deployment-2035384211   0         0         0       36s
  nginx-deployment-3066724191   1         1         0       6s
  ```

- 생성된 파드를 보면, 새로운 레플리카셋에 생성된 1개의 파드가 이미지 풀 루프(pull loop)에서 고착된 것을 볼 수 있다.

  ```shell
  kubectl get pods
  ```

  이와 유사하게 출력된다.

  ```
  NAME                                READY     STATUS             RESTARTS   AGE
  nginx-deployment-1564180365-70iae   1/1       Running            0          25s
  nginx-deployment-1564180365-jbqqo   1/1       Running            0          25s
  nginx-deployment-1564180365-hysrc   1/1       Running            0          25s
  nginx-deployment-3066724191-08mng   0/1       ImagePullBackOff   0          6s
  ```

#### 디플로이먼트의 롤아웃 기록 확인

1. 먼저 이 디플로이먼트의 수정 사항을 확인한다.

   ```shell
   kubectl rollout history deployment.v1.apps/nginx-deployment
   ```

   이와 유사하게 출력된다.

   ```
   deployments "nginx-deployment"
   REVISION    CHANGE-CAUSE
   1           kubectl apply --filename=https://k8s.io/examples/controllers/nginx-deployment.yaml --record=true
   2           kubectl set image deployment.v1.apps/nginx-deployment nginx=nginx:1.16.1 --record=true
   3           kubectl set image deployment.v1.apps/nginx-deployment nginx=nginx:1.161 --record=true
   ```

   `CHANGE-CAUSE` 는 수정 생성시 디플로이먼트 주석인 `kubernetes.io/change-cause` 에서 복사한다. 다음에 대해 `CHANGE-CAUSE` 메시지를 지정할 수 있다.

   - 디플로이먼트에 `kubectl annotate deployment.v1.apps/nginx-deployment kubernetes.io/change-cause="image updated to 1.16.1"` 로 주석을 단다.
   - `kubectl` 명령어 이용시 `--record` 플래그를 추가해서 리소스 변경을 저장한다.
   - 수동으로 리소스 매니페스트 편집.

2. 각 수정 버전의 세부 정보를 보려면 다음을 실행한다.

   ```shell
   kubectl rollout history deployment.v1.apps/nginx-deployment --revision=2
   ```

   이와 유사하게 출력된다.

   ```
   deployments "nginx-deployment" revision 2
     Labels:       app=nginx
             pod-template-hash=1159050644
     Annotations:  kubernetes.io/change-cause=kubectl set image deployment.v1.apps/nginx-deployment nginx=nginx:1.16.1 --record=true
     Containers:
      nginx:
       Image:      nginx:1.16.1
       Port:       80/TCP
        QoS Tier:
           cpu:      BestEffort
           memory:   BestEffort
       Environment Variables:      <none>
     No volumes.
   ```

#### 이전 수정 버전으로 롤백

1. 이제 현재 롤아웃의 실행 취소 및 이전 수정 버전으로 롤백 하기로 결정했다.

   ```shell
   kubectl rollout undo deployment.v1.apps/nginx-deployment
   ```

   이와 유사하게 출력된다.

   ```
   deployment.apps/nginx-deployment rolled back
   ```

   Alternatively, you can rollback to a specific revision by specifying it with `--to-revision`:

   ```shell
   kubectl rollout undo deployment.v1.apps/nginx-deployment --to-revision=2
   ```

   이와 유사하게 출력된다.

   ```
   deployment.apps/nginx-deployment rolled back
   ```

2. 만약 롤백에 성공하고, 디플로이먼트가 예상대로 실행되는지 확인하려면 다음을 실행한다.

   ```shell
   kubectl get deployment nginx-deployment
   ```

   이와 유사하게 출력된다.

   ```
   NAME               READY   UP-TO-DATE   AVAILABLE   AGE
   nginx-deployment   3/3     3            3           30m
   ```

3. 디플로이먼트의 설명 가져오기.

   ```shell
   kubectl describe deployment nginx-deployment
   ```

   이와 유사하게 출력된다.

   ```
   Name:                   nginx-deployment
   Namespace:              default
   CreationTimestamp:      Sun, 02 Sep 2018 18:17:55 -0500
   Labels:                 app=nginx
   Annotations:            deployment.kubernetes.io/revision=4
                           kubernetes.io/change-cause=kubectl set image deployment.v1.apps/nginx-deployment nginx=nginx:1.16.1 --record=true
   Selector:               app=nginx
   Replicas:               3 desired | 3 updated | 3 total | 3 available | 0 unavailable
   StrategyType:           RollingUpdate
   MinReadySeconds:        0
   RollingUpdateStrategy:  25% max unavailable, 25% max surge
   Pod Template:
     Labels:  app=nginx
     Containers:
      nginx:
       Image:        nginx:1.16.1
       Port:         80/TCP
       Host Port:    0/TCP
       Environment:  <none>
       Mounts:       <none>
     Volumes:        <none>
   Conditions:
     Type           Status  Reason
     ----           ------  ------
     Available      True    MinimumReplicasAvailable
     Progressing    True    NewReplicaSetAvailable
   OldReplicaSets:  <none>
   NewReplicaSet:   nginx-deployment-c4747d96c (3/3 replicas created)
   Events:
     Type    Reason              Age   From                   Message
     ----    ------              ----  ----                   -------
     Normal  ScalingReplicaSet   12m   deployment-controller  Scaled up replica set nginx-deployment-75675f5897 to 3
     Normal  ScalingReplicaSet   11m   deployment-controller  Scaled up replica set nginx-deployment-c4747d96c to 1
     Normal  ScalingReplicaSet   11m   deployment-controller  Scaled down replica set nginx-deployment-75675f5897 to 2
     Normal  ScalingReplicaSet   11m   deployment-controller  Scaled up replica set nginx-deployment-c4747d96c to 2
     Normal  ScalingReplicaSet   11m   deployment-controller  Scaled down replica set nginx-deployment-75675f5897 to 1
     Normal  ScalingReplicaSet   11m   deployment-controller  Scaled up replica set nginx-deployment-c4747d96c to 3
     Normal  ScalingReplicaSet   11m   deployment-controller  Scaled down replica set nginx-deployment-75675f5897 to 0
     Normal  ScalingReplicaSet   11m   deployment-controller  Scaled up replica set nginx-deployment-595696685f to 1
     Normal  DeploymentRollback  15s   deployment-controller  Rolled back deployment "nginx-deployment" to revision 2
     Normal  ScalingReplicaSet   15s   deployment-controller  Scaled down replica set nginx-deployment-595696685f to 0
   ```

### 디플로이먼트 스케일링

다음 명령어를 사용해서 디플로이먼트의 스케일을 할 수 있다.

```shell
kubectl scale deployment.v1.apps/nginx-deployment --replicas=10
```

이와 유사하게 출력된다.

```
deployment.apps/nginx-deployment scaled
```

령 클러스터에서 [horizontal Pod autoscaling](https://kubernetes.io/ko/docs/tasks/run-application/horizontal-pod-autoscale-walkthrough/)를 설정 한 경우 디플로이먼트에 대한 오토스케일러를 설정할 수 있다. 그리고 기존 파드의 CPU 사용률을 기준으로 실행할 최소 파드 및 최대 파드의 수를 선택할 수 있다.

```shell
kubectl autoscale deployment.v1.apps/nginx-deployment --min=10 --max=15 --cpu-percent=80
```

이와 유사하게 출력된다.

```
deployment.apps/nginx-deployment scaled
```

#### 비례적 스케일링(Proportional Scaling)

디플로이먼트 롤링업데이트는 여러 버전의 애플리케이션을 동시에 실행할 수 있도록 지원한다. 사용자 또는 오토스케일러가 롤아웃 중에 있는 디플로이먼트 롤링 업데이트를 스케일링 하는 경우(진행중 또는 일시 중지 중), 디플로이먼트 컨트롤러는 위험을 줄이기 위해 기존 활성화된 레플리카셋(파드와 레플리카셋)의 추가 레플리카의 균형을 조절 한다. 이것을 *proportional scaling*라 부른다.

예를 들어, 10개의 레플리카를 디플로이먼트로 maxSurge=3, 그리고 maxUnavailable=2 로 실행 한다.

- 디플로이먼트에 있는 10개의 레플리카가 실행되는지 확인한다.

  ```shell
  kubectl get deploy
  ```

  이와 유사하게 출력된다.

  ```
  NAME                 DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
  nginx-deployment     10        10        10           10          50s
  ```

- 클러스터 내부에서 확인할 수 없는 새 이미지로 업데이트 된다.

  ```shell
  kubectl set image deployment.v1.apps/nginx-deployment nginx=nginx:sometag
  ```

  이와 유사하게 출력된다.

  ```
  deployment.apps/nginx-deployment image updated
  ```

- 이미지 업데이트는 레플리카셋 nginx-deployment-1989198191 으로 새로운 롤 아웃이 시작하지만, 위에서 언급한 `maxUnavailable` 의 요구 사항으로 인해 차단된다. 롤아웃 상태를 확인한다.

  ```shell
  kubectl get rs
  ```

  ```
  이와 유사하게 출력된다.
  ```

  ```
  NAME                          DESIRED   CURRENT   READY     AGE
  nginx-deployment-1989198191   5         5         0         9s
  nginx-deployment-618515232    8         8         8         1m
  ```

- 그 다음 디플로이먼트에 대한 새로운 스케일링 요청이 함께 따라온다. 오토스케일러는 디플로이먼트 레플리카를 15로 증가시킨다. 디플로이먼트 컨트롤러는 새로운 5개의 레플리카의 추가를 위한 위치를 결정해야 한다. 만약 비례적 스케일링을 사용하지 않으면 5개 모두 새 레플리카셋에 추가된다. 비례적 스케일링으로 추가 레플리카를 모든 레플리카셋에 걸쳐 분산할 수 있다. 비율이 높을수록 가장 많은 레플리카가 있는 레플리카셋으로 이동하고, 비율이 낮을 수록 적은 레플리카가 있는 레플리카셋으로 이동한다. 남은 것들은 대부분의 레플리카가 있는 레플리카셋에 추가된다. 0개의 레플리카가 있는 레플리카셋은 스케일 업 되지 않는다.

위의 예시에서 기존 레플리카셋에 3개의 레플리카가 추가되고, 2개의 레플리카는 새 레플리카에 추가된다. 결국 롤아웃 프로세스는 새 레플리카가 정상이라고 가정하면 모든 레플리카를 새 레플리카셋으로 이동시킨다. 이를 확인하려면 다음을 실행한다.

```shell
kubectl get deploy
```

이와 유사하게 출력된다.

```
NAME                 DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
nginx-deployment     15        18        7            8           7m
```

롤아웃 상태는 레플리카가 각 레플리카셋에 어떻게 추가되었는지 확인한다.

```shell
kubectl get rs
```

이와 유사하게 출력된다.

```
NAME                          DESIRED   CURRENT   READY     AGE
nginx-deployment-1989198191   7         7         0         7m
nginx-deployment-618515232    11        11        11        7m
```

### 디플로이먼트의 일시 중지와 재개

하나 이상의 업데이트를 트리거하기 전에 디플로이먼트를 일시 중지한 다음 다시 시작할 수 있다. 이렇게 하면 불필요한 롤아웃을 트리거하지 않고 일시 중지와 재개 사이에 여러 수정 사항을 적용할 수 있다.

- 예를 들어, 생성된 디플로이먼트의 경우 디플로이먼트 상세 정보를 가져온다.

  ```shell
  kubectl get deploy
  ```

  이와 유사하게 출력된다.

  ```
  NAME      DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
  nginx     3         3         3            3           1m
  ```

  롤아웃 상태를 가져온다.

  ```shell
  kubectl get rs
  ```

  이와 유사하게 출력된다.

  ```
  NAME               DESIRED   CURRENT   READY     AGE
  nginx-2142116321   3         3         3         1m
  ```

- 다음 명령을 사용해서 일시 중지한다.

  ```shell
  kubectl rollout pause deployment.v1.apps/nginx-deployment
  ```

  이와 유사하게 출력된다.

  ```
  deployment.apps/nginx-deployment paused
  ```

- 그런 다음 디플로이먼트의 이미지를 업데이트 한다.

  ```shell
  kubectl set image deployment.v1.apps/nginx-deployment nginx=nginx:1.16.1
  ```

  이와 유사하게 출력된다.

  ```
  deployment.apps/nginx-deployment image updated
  ```

- 새로운 롤아웃이 시작되지 않는다.

  ```shell
  kubectl rollout history deployment.v1.apps/nginx-deployment
  ```

  이와 유사하게 출력된다.

  ```
  deployments "nginx"
  REVISION  CHANGE-CAUSE
  1   <none>
  ```

- 롤아웃 상태를 가져와서 디플로이먼트 업데이트가 성공적인지 확인한다.

  ```shell
  kubectl get rs
  ```

  이와 유사하게 출력된다.

  ```
  NAME               DESIRED   CURRENT   READY     AGE
  nginx-2142116321   3         3         3         2m
  ```

- 예를 들어 사용할 리소스를 업데이트하는 것처럼 원하는 만큼 업데이트할 수 있다.

  ```shell
  kubectl set resources deployment.v1.apps/nginx-deployment -c=nginx --limits=cpu=200m,memory=512Mi
  ```

  이와 유사하게 출력된다.

  ```
  deployment.apps/nginx-deployment resource requirements updated
  ```

  디플로이먼트를 일시 중지하기 전의 초기 상태는 해당 기능을 지속한다. 그러나 디플로이먼트가 일시 중지한 상태에서는 디플로이먼트의 새 업데이트에 영향을 주지 않는다.

- 결국, 디플로이먼트를 재개하고 새로운 레플리카셋이 새로운 업데이트를 제공하는 것을 관찰한다.

  ```shell
  kubectl rollout resume deployment.v1.apps/nginx-deployment
  ```

  이와 유사하게 출력된다.

  ```
  deployment.apps/nginx-deployment resumed
  ```

- 롤아웃이 완료될 때까지 상태를 관찰한다.

  ```shell
  kubectl get rs -w
  ```

  이와 유사하게 출력된다.

  ```
  NAME               DESIRED   CURRENT   READY     AGE
  nginx-2142116321   2         2         2         2m
  nginx-3926361531   2         2         0         6s
  nginx-3926361531   2         2         1         18s
  nginx-2142116321   1         2         2         2m
  nginx-2142116321   1         2         2         2m
  nginx-3926361531   3         2         1         18s
  nginx-3926361531   3         2         1         18s
  nginx-2142116321   1         1         1         2m
  nginx-3926361531   3         3         1         18s
  nginx-3926361531   3         3         2         19s
  nginx-2142116321   0         1         1         2m
  nginx-2142116321   0         1         1         2m
  nginx-2142116321   0         0         0         2m
  nginx-3926361531   3         3         3         20s
  ```

- 롤아웃 최신 상태를 가져온다.

  ```shell
  kubectl get rs
  ```

  이와 유사하게 출력된다.

  ```
  NAME               DESIRED   CURRENT   READY     AGE
  nginx-2142116321   0         0         0         2m
  nginx-3926361531   3         3         3         28s
  ```

### 디플로이먼트 사양 작성

다른 모든 쿠버네티스 설정과 마찬가지로 디플로이먼트에는 `.apiVersion`, `.kind` 그리고 `.metadata` 필드가 필요하다.

#### 파드 템플릿

`.spec.template` 과 `.spec.selector` 은 `.spec` 에서 유일한 필수 필드이다.

`.spec.template` 는 파드 템플릿이다. 이것은 파드와 정확하게 동일한 스키마를 가지고 있고, 중첩된 것을 제외하면 `apiVersion` 과 `kind` 를 가지고 있지 않는다.

파드에 필요한 필드 외에 디플로이먼트 파드 템플릿은 적절한 레이블과 적절한 재시작 정책을 명시해야 한다. 레이블의 경우 다른 컨트롤러와 겹치지 않도록 해야 한다. 자세한 것은 셀렉터를 참조한다.

`.spec.template.spec.restartPolicy` 에는 오직 `Always` 만 허용되고, 명시되지 않으면 기본값이 된다.

#### 레플리카

`.spec.replicas` 은 필요한 파드의 수를 지정하는 선택적 필드이다. 이것의 기본값은 1이다.

#### 셀렉터

`.spec.selector` 는 디플로이먼트의 대상이 되는 파드에 대해 레이블 셀렉터를 지정하는 필수 필드이다.

`.spec.selector` 는 `.spec.template.metadata.labels` 과 일치해야 하며, 그렇지 않으면 API에 의해 거부된다.

API 버전 `apps/v1` 에서는 `.spec.selector` 와 `.metadata.labels` 이 설정되지 않으면 `.spec.template.metadata.labels` 은 기본 설정되지 않는다. 그래서 이것들은 명시적으로 설정되어야 한다. 또한 `apps/v1` 에서는 디플로이먼트를 생성한 후에는 `.spec.selector` 이 변경되지 않는 점을 참고한다.

디플로이먼트는 템플릿의 `.spec.template` 와 다르거나 파드의 수가 `.spec.replicas`를 초과할 경우 셀렉터와 일치하는 레이블을 가진 파드를 종료할 수 있다. 파드의 수가 의도한 수량보다 적을 경우 `.spec.template` 에 맞는 새 파드를 띄운다.

#### 전략

`.spec.strategy` 는 이전 파드를 새로운 파드로 대체하는 전략을 명시한다.`.spec.strategy.type` 은 "재생성" 또는 "롤링업데이트"가 될 수 있다. "롤링업데이트"가 기본값이다.

##### 디플로이먼트 재생성

기존의 모든 파드는 `.spec.strategy.type==Recreate` 이면 새 파드가 생성되기 전에 죽는다.

##### 디플로이먼트 롤링 업데이트

디플로이먼트는 `.spec.strategy.type==RollingUpdate` 이면 파드를 롤링 업데이트 방식으로 업데이트 한다. `maxUnavailable` 와 `maxSurge` 를 명시해서 롤링 업데이트 프로세스를 제어할 수 있다.

###### 최대 불가(Max Unavailable)

`.spec.strategy.rollingUpdate.maxUnavailable` 은 업데이트 프로세스 중에 사용할 수 없는 최대 파드의 수를 지정하는 선택적 필드이다. 이 값은 절대 숫자(예: 5) 또는 의도한 파드 비율(예: 10%)이 될 수 있다. 절대 값은 반올림해서 백분율로 계산한다. 만약 `.spec.strategy.rollingUpdate.maxSurge` 가 0이면 값이 0이 될 수 없다. 기본 값은 25% 이다.

예를 들어 이 값을 30%로 설정하면 롤링업데이트 시작시 즉각 이전 레플리카셋의 크기를 의도한 파드 중 70%를 스케일 다운할 수 있다. 새 파드가 준비되면 기존 레플리카셋을 스케일 다운할 수 있으며, 업데이트 중에 항상 사용 가능한 전체 파드의 수는 의도한 파드의 수의 70% 이상이 되도록 새 레플리카셋을 스케일 업할 수 있다.

###### 최대 서지(Max Surge)

`.spec.strategy.rollingUpdate.maxSurge` 는 의도한 파드의 수에 대해 생성할 수 있는 최대 파드의 수를 지정하는 선택적 필드이다. 이 값은 절대 숫자(예: 5) 또는 의도한 파드 비율(예: 10%)이 될 수 있다. `MaxUnavailable` 값이 0이면 이 값은 0이 될 수 없다. 절대 값은 반올림해서 백분율로 계산한다. 기본 값은 25% 이다.

예를 들어 이 값을 30%로 설정하면 롤링업데이트 시작시 새 레플리카셋의 크기를 즉시 조정해서 기존 및 새 파드의 전체 갯수를 의도한 파드의 130%를 넘지 않도록 한다. 기존 파드가 죽으면 새로운 래플리카셋은 스케일 업할 수 있으며, 업데이트하는 동안 항상 실행하는 총 파드의 수는 최대 의도한 파드의 수의 130%가 되도록 보장한다.

#### 최소 대기 시간(초)

`.spec.minReadySeconds` 는 새롭게 생성된 파드의 컨테이너가 어떤 것과도 충돌하지 않고 사 용할 수 있도록 준비되어야 하는 최소 시간(초)을 지정하는 선택적 필드이다. 이 기본 값은 0이다(파드는 준비되는 즉시 사용할 수 있는 것으로 간주됨). 

#### 수정 버전 기록 제한

디플로이먼트의 수정 버전 기록은 자신이 컨트롤하는 레플리카셋에 저장된다.

`.spec.revisionHistoryLimit` 은 롤백을 허용하기 위해 보존할 이전 레플리카셋의 수를 지정하는 선택적 필드이다. 이 이전 레플리카셋은 `etcd` 의 리소스를 소비하고, `kubectl get rs` 의 결과를 가득차게 만든다. 각 디플로이먼트의 구성은 디플로이먼트의 레플리카셋에 저장된다. 이전 레플리카셋이 삭제되면 해당 디플로이먼트 수정 버전으로 롤백할 수 있는 기능이 사라진다. 기본적으로 10개의 기존 레플리카셋이 유지되지만 이상적인 값은 새로운 디플로이먼트의 빈도와 안정성에 따라 달라진다.

더욱 구체적으로 이 필드를 0으로 설정하면 레플리카가 0이 되며 이전 레플리카셋이 정리된다. 이 경우, 새로운 디플로이먼트 롤아웃을 취소할 수 없다. 새로운 디플로이먼트 롤아웃은 수정 버전 이력이 정리되기 때문이다.

#### 일시 정지

`.spec.paused` 는 디플로이먼트를 일시 중지나 재개하기 위한 선택적 부울 필드이다. 일시 중지 된 디플로이먼트와 일시 중지 되지 않은 디플로이먼트 사이의 유일한 차이점은 일시 중지된 디플로이먼트는 PodTemplateSpec에 대한 변경 사항이 일시중지 된 경우 새 롤아웃을 트리거 하지 않는다. 디플로이먼트는 생성시 기본적으로 일시 중지되지 않는다.