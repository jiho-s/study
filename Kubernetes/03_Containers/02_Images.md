## 이미지

컨테이너 이미지는 애플리케이션과 애플리케이션의 모든 소프트웨어 의존성을 캡슐화한 바이너리 데이터를 나타낸다. 컨테이너 이미지는 독립적으로 실행할 수 있고 런타임 환경에 대해 잘 정의된 가정을 만드는 실행 가능한 소프트웨어 번들이다.

일반적으로 파드에서 참조하기 전에 애플리케이션의 컨테이너 이미지를 생성해서 레지스트리로 푸시한다.

### 이미지 이름

컨테이너 이미지는 일반적으로 `pause`, `example/mycontainer` 또는 `kube-apiserver` 와 같은 이름을 부여한다. 이미지는 또한 레지스트리 호스트 이름을 포함할 수 있다. 예를 들면, `fictional.registry.example/imagename` 과 같다. 그리고 포트 번호도 포함할 수 있다. 예를 들면, `fictional.registry.example:10443/imagename` 과 같다.

레지스트리 호스트 이름을 지정하지 않으면, 쿠버네티스는 도커 퍼블릭 레지스트리를 의미한다고 가정한다.

이미지 이름 부분 다음에 *tag* 를 추가할 수 있다(`docker` 와 `podman` 등의 명령과 함께 사용). 태그를 사용하면 동일한 시리즈 이미지의 다른 버전을 구분 할 수 있다.

이미지 태그는 소문자와 대문자, 숫자, 밑줄(`_`), 마침표(`.`) 및 대시(`-`)로 구성된다. 이미지 태그 안에서 구분 문자(`_`, `-` 그리고 `.`)를 배치할 수 있는 위치에 대한 추가 규칙이 있다. 태그를 지정하지 않으면, 쿠버네티스는 태그 `latest` 를 의미한다고 가정한다.

### 이미지 업데이트

디플로이먼트, 스테이트풀셋, 파드 또는 Pod template을 포함하는 다른 오브젝트를 처음 만들 때 기본적으로 해당 파드에 있는 모든 컨테이너의 풀(pull) 정책은 `IfNotPresent`로 설정된다. 이 정책은 kubelet이 이미 존재하는 이미지에 대한 풀을 생략하게 한다.

만약 항상 풀을 강제하고 싶다면, 다음 중 하나를 수행하면 된다.

- 컨테이너의 `imagePullPolicy`를 `Always`로 설정.
- `imagePullPolicy`를 생략하고 `:latest`를 사용할 이미지의 태그로 사용, 쿠버네티스는 정책을 `Always`로 설정한다.
- `imagePullPolicy`와 사용할 이미지의 태그를 생략.
- [AlwaysPullImages](https://kubernetes.io/docs/reference/access-authn-authz/admission-controllers/#alwayspullimages) 어드미션 컨트롤러를 활성화.

### 이미지 인덱스가 있는 다중 아키텍처 이미지

컨테이너 레지스트리는 바이너리 이미지를 제공할 뿐만 아니라 컨테이너 이미지 인덱스를 제공할 수도 있다. 이미지 인덱스는 컨테이너의 아키텍처별 버전에 대한 여러 이미지 매니페스트를 가리킬 수 있다. 아이디어는 이미지의 이름(예를 들어, `pause`, `example/mycontainer`, `kube-apiserver`)을 가질 수 있다는 것이다. 그래서 서로 다른 시스템들이 사용하고 있는 컴퓨터 아키텍처에 따라 적합한 바이너리 이미지를 가져올 수 있다.

쿠버네티스 자체는 일반적으로 `-$(ARCH)` 접미사로 컨테이너 이미지의 이름을 지정한다. 이전 버전과의 호환성을 위해, 접미사가 있는 이전 이미지를 생성해라. 모든 아키텍처에 대한 매니페스트가 있는 이미지를 `pause` 라고 불리는 이미지로 생성하고, 이전 버전 또는 이전에 접미사로 이미지를 하드 코딩한 YAML 파일과 호환되는 이미지를 `pause-amd64` 라고 불리는 이미지를 생성한다.

### 프라이빗 레지스트리 사용

프라이빗 레지스트리는 해당 레지스트리에서 이미지를 읽을 수 있는 키를 요구할 것이다. 자격 증명(credential)은 여러 가지 방법으로 제공될 수 있다.

- 프라이빗 레지스트리에 인증하도록 노드 구성
  - 모든 파드는 구성된 프라이빗 레지스트리를 읽을 수 있음
  - 클러스터 관리자에 의한 노드 구성 필요
- 미리 내려받은(pre-pulled) 이미지
  - 모든 파드는 노드에 캐시된 모든 이미지를 사용 가능
  - 셋업을 위해서는 모든 노드에 대해서 root 접근이 필요
- 파드에 ImagePullSecrets을 명시
  - 자신의 키를 제공하는 파드만 프라이빗 레지스트리에 접근 가능
- 공급 업체별 또는 로컬 확장
  - 사용자 정의 노드 구성을 사용하는 경우, 사용자(또는 클라우드 제공자)가 컨테이너 레지스트리에 대한 노드 인증 메커니즘을 구현할 수 있다.

이들 옵션은 아래에서 더 자세히 설명한다.

#### 프라이빗 레지스트리에 인증하도록 노드 구성

노드에서 도커를 실행하는 경우, 프라이빗 컨테이너 레지스트리를 인증하도록 도커 컨테이너 런타임을 구성할 수 있다.

이 방법은 노드 구성을 제어할 수 있는 경우에 적합하다.

도커는 프라이빗 레지스트리를 위한 키를 `$HOME/.dockercfg` 또는 `$HOME/.docker/config.json` 파일에 저장한다. 만약 동일한 파일을 아래의 검색 경로 리스트에 넣으면, kubelete은 이미지를 풀 할 때 해당 파일을 자격 증명 공급자로 사용한다.

- `{--root-dir:-/var/lib/kubelet}/config.json`
- `{cwd of kubelet}/config.json`
- `${HOME}/.docker/config.json`
- `/.docker/config.json`
- `{--root-dir:-/var/lib/kubelet}/.dockercfg`
- `{cwd of kubelet}/.dockercfg`
- `${HOME}/.dockercfg`
- `/.dockercfg`

 프라이빗 레지스트리를 사용도록 사용자의 노드를 구성하기 위해서 권장되는 단계는 다음과 같다. 사용자의 데스크탑/랩탑에서 아래 내용을 실행해라.

1. 사용하고 싶은 각 자격 증명 세트에 대해서 `docker login [서버]`를 실행한다. 이것은 여러분 PC의 `$HOME/.docker/config.json`를 업데이트한다.
2. 편집기에서 `$HOME/.docker/config.json`를 보고 사용하고 싶은 자격 증명만 포함하고 있는지 확인한다.
3. 노드의 리스트를 구한다. 예를 들면 다음과 같다.
   - 이름을 원하는 경우: `nodes=$( kubectl get nodes -o jsonpath='{range.items[*].metadata}{.name} {end}' )`
   - IP를 원하는 경우: `nodes=$( kubectl get nodes -o jsonpath='{range .items[*].status.addresses[?(@.type=="ExternalIP")]}{.address} {end}' )`
4. 로컬의 `.docker/config.json`를 위의 검색 경로 리스트 중 하나에 복사한다.
   - 이를 테스트하기 위한 예: `for n in $nodes; do scp ~/.docker/config.json root@"$n":/var/lib/kubelet/config.json; done`

프라이빗 이미지를 사용하는 파드를 생성하여 검증한다. 예를 들면 다음과 같다.

```shell
kubectl apply -f - <<EOF
apiVersion: v1
kind: Pod
metadata:
  name: private-image-test-1
spec:
  containers:
    - name: uses-private-image
      image: $PRIVATE_IMAGE_NAME
      imagePullPolicy: Always
      command: [ "echo", "SUCCESS" ]
EOF
```

클러스터의 모든 노드가 반드시 동일한 `.docker/config.json`를 가져야 한다. 그렇지 않으면, 파드가 일부 노드에서만 실행되고 다른 노드에서는 실패할 것이다. 예를 들어, 노드 오토스케일링을 사용한다면, 각 인스턴스 템플릿은 `.docker/config.json`을 포함하거나 그것을 포함한 드라이브를 마운트해야 한다.

프라이빗 레지스트리 키가 `.docker/config.json`에 추가되고 나면 모든 파드는 프라이빗 레지스트리의 이미지에 읽기 접근 권한을 가지게 될 것이다

#### 미리 내려받은(pre-pulled) 이미지

>Google 쿠버네티스 엔진에서 동작 중이라면, 이미 각 노드에 Google 컨테이너 레지스트리에 대한 자격 증명과 함께 `.dockercfg`가 있을 것이다. 그렇다면 이 방법은 쓸 수 없다.

>이 방법은 노드의 구성을 제어할 수 있는 경우에만 적합하다. 이 방법은 클라우드 제공자가 노드를 관리하고 자동으로 교체한다면 안정적으로 작동하지 않을 것이다.

기본적으로, kubelet은 지정된 레지스트리에서 각 이미지를 풀 하려고 한다. 그러나, 컨테이너의 `imagePullPolicy` 속성이 `IfNotPresent` 또는 `Never`으로 설정되어 있다면, 로컬 이미지가 사용된다(우선적으로 또는 배타적으로).

레지스트리 인증의 대안으로 미리 풀 된 이미지에 의존하고 싶다면, 클러스터의 모든 노드가 동일한 미리 내려받은 이미지를 가지고 있는지 확인해야 한다.

이것은 특정 이미지를 속도를 위해 미리 로드하거나 프라이빗 레지스트리에 대한 인증의 대안으로 사용될 수 있다.

모든 파드는 미리 내려받은 이미지에 대해 읽기 접근 권한을 가질 것이다

#### 파드에 ImagePullSecrets 명시

쿠버네티스는 파드에 컨테이너 이미지 레지스트리 키를 명시하는 것을 지원한다.

##### 도커 구성으로 시크릿 생성

다음 커맨드를 실행한다.

```shell
kubectl create secret docker-registry <name> --docker-server=DOCKER_REGISTRY_SERVER --docker-username=DOCKER_USER --docker-password=DOCKER_PASSWORD --docker-email=DOCKER_EMAIL
```

만약 도커 자격 증명 파일이 이미 존재한다면, 위의 명령을 사용하지 않고, 자격 증명 파일을 쿠버네티스 시크릿으로 가져올 수 있다. [기존 도커 자격 증명으로 시크릿 생성](https://kubernetes.io/ko/docs/tasks/configure-pod-container/pull-image-private-registry/#registry-secret-existing-credentials)에서 관련 방법을 설명하고 있다.

`kubectl create secret docker-registry`는 하나의 프라이빗 레지스트리에서만 작동하는 시크릿을 생성하기 때문에, 여러 프라이빗 컨테이너 레지스트리를 사용하는 경우 특히 유용하다.

##### 파드의 imagePullSecrets 참조

이제, `imagePullSecrets` 섹션을 파드의 정의에 추가함으로써 해당 시크릿을 참조하는 파드를 생성할 수 있다.

예를 들면 다음과 같다.

```shell
cat <<EOF > pod.yaml
apiVersion: v1
kind: Pod
metadata:
  name: foo
  namespace: awesomeapps
spec:
  containers:
    - name: foo
      image: janedoe/awesomeapp:v1
  imagePullSecrets:
    - name: myregistrykey
EOF

cat <<EOF >> ./kustomization.yaml
resources:
- pod.yaml
EOF
```

이것은 프라이빗 레지스트리를 사용하는 각 파드에서 수행될 필요가 있다.

그러나, 이 필드의 셋팅은 [서비스 어카운트](https://kubernetes.io/docs/tasks/configure-pod-container/configure-service-account/) 리소스에 imagePullSecrets을 셋팅하여 자동화할 수 있다.

자세한 지침을 위해서는 [서비스 어카운트에 ImagePullSecrets 추가](https://kubernetes.io/docs/tasks/configure-pod-container/configure-service-account/#add-imagepullsecrets-to-a-service-account)를 확인한다.

이것은 노드 당 `.docker/config.json`와 함께 사용할 수 있다. 자격 증명은 병합될 것이다.