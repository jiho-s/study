## 레이블과 셀렉터

*레이블* 은 파드와 같은 오브젝트에 첨부된 키와 값의 쌍이다. 레이블은 오브젝트의 특성을 식별하는 데 사용되어 사용자에게 중요하지만, 코어 시스템에 직접적인 의미는 없다. 레이블로 오브젝트의 하위 집합을 선택하고, 구성하는데 사용할 수 있다. 레이블은 오브젝트를 생성할 때에 붙이거나 생성 이후에 붙이거나 언제든지 수정이 가능하다. 오브젝트마다 키와 값으로 레이블을 정의할 수 있다. 오브젝트의 키는 고유한 값이어야 한다.

```yaml
"metadata": {
  "labels": {
    "key1" : "value1",
    "key2" : "value2"
  }
}
```

레이블은 UI와 CLI에서 효율적인 쿼리를 사용하고 검색에 사용하기에 적합하다

### 사용 동기

레이블을 이용하면 사용자가 클라이언트에 매핑 정보를 저장할 필요 없이, 느슨하게 결합한 방식으로 조직 구조와 시스템 오브젝트를 매핑할 수 있다.

서비스 배포와 배치 프로세싱 파이프라인은 흔히 다중 차원의 엔티티들이다(예: 다중 파티션 또는 배포, 다중 릴리즈 트랙, 다중 계층, 계층 속 여러 마이크로 서비스들). 관리에는 크로스-커팅 작업이 필요한 경우가 많은데 이 작업은 인프라에 의해 결정된 엄격한 계층 표현인 캡슐화를 깨트린다.

레이블 예시:

- `"release" : "stable"`, `"release" : "canary"`
- `"environment" : "dev"`, `"environment" : "qa"`, `"environment" : "production"`
- `"tier" : "frontend"`, `"tier" : "backend"`, `"tier" : "cache"`
- `"partition" : "customerA"`, `"partition" : "customerB"`
- `"track" : "daily"`, `"track" : "weekly"`

이 예시는 일반적으로 사용하는 레이블이며, 사용자는 자신만의 규칙(convention)에 따라 자유롭게 개발할 수 있다. 오브젝트에 붙여진 레이블 키는 고유해야 한다

### 구문과 캐릭터 셋

*레이블* 은 키와 값의 쌍이다. 레이블 키에는 슬래시(`/`)로 구분되는 선택한 접두사와 이름이라는 2개의 부분으로 이루어져 있다. 이름 세그먼트는 63자 미만으로 시작과 끝은 알파벳과 숫자(`[a-z0-9A-Z]`)이며, 대시(`-`), 밑줄(`_`), 점(`.`)과 함께 사용할 수 있다. 접두사는 선택이다. 만약 접두사를 지정한 경우 접두사는 DNS의 하위 도메인으로 해야 하며, 점(`.`)과 전체 253자 이하, 슬래시(`/`)로 구분되는 DNS 레이블이다.

접두사를 생략하면 키 레이블은 개인용으로 간주한다. 최종 사용자의 오브젝트에 자동화된 시스템 컴포넌트(예: `kube-scheduler`, `kube-controller-manager`, `kube-apiserver`, `kubectl` 또는 다른 타사의 자동화 구성 요소)의 접두사를 지정해야 한다.

`kubernetes.io/`와 `k8s.io/` 접두사는 쿠버네티스의 핵심 컴포넌트로 예약되어있다.

유효한 레이블 값은 다음과 같다.

- 63 자 이하 여야 하고(공백이면 안 됨),
- 시작과 끝은 알파벳과 숫자(`[a-z0-9A-Z]`)이며,
- 알파벳과 숫자, 대시(`-`), 밑줄(`_`), 점(`.`)를 중간에 포함할 수 있다.

유효한 레이블 값은 63자 미만 또는 공백이며 시작과 끝은 알파벳과 숫자(`[a-z0-9A-Z]`)이며, 대시(`-`), 밑줄(`_`), 점(`.`)과 함께 사용할 수 있다.

다음의 예시는 파드에 `environment: production` 과 `app: nginx` 2개의 레이블이 있는 구성 파일이다.

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: label-demo
  labels:
    environment: production
    app: nginx
spec:
  containers:
  - name: nginx
    image: nginx:1.14.2
    ports:
    - containerPort: 80
```
