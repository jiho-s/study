## 오브젝트 이름과 ID

클러스터의 각 오브젝트는 해당 유형의 리소스에 대하여 고유한 [*이름*](#이름) 을 가지고 있다. 또한, 모든 쿠버네티스 오브젝트는 전체 클러스터에 걸쳐 고유한 [*UID*](#uid) 를 가지고 있다.

예를 들어, 이름이 `myapp-1234`인 파드는 동일한 네임스페이스 내에서 하나만 존재할 수 있지만, 이름이 `myapp-1234`인 파드와 디플로이먼트는 각각 존재할 수 있다.

유일하지 않은 사용자 제공 속성의 경우 쿠버네티스는 레이블과 어노테이션을 제공한다

### 이름

`/api/v1/pods/some-name`과 같이, 리소스 URL에서 오브젝트를 가리키는 클라이언트 제공 문자열.

특정 시점에 같은 종류(kind) 내에서는 하나의 이름은 하나의 오브젝트에만 지정될 수 있다. 하지만, 오브젝트를 삭제한 경우, 삭제된 오브젝트와 같은 이름을 새로운 오브젝트에 지정 가능하다.

다음은 리소스에 일반적으로 사용되는 세 가지 유형의 이름 제한 조건이다.

#### DNS 서브도메인 이름

대부분의 리소스 유형에는 [RFC 1123](https://tools.ietf.org/html/rfc1123)에 정의된 대로 DNS 서브도메인 이름으로 사용할 수 있는 이름이 필요하다. 이것은 이름이 다음을 충족해야 한다는 것을 의미한다.

- 253자를 넘지 말아야 한다.
- 소문자와 영숫자 `-` 또는 `.` 만 포함한다.
- 영숫자로 시작한다.
- 영숫자로 끝난다.

#### DNS 레이블 이름

일부 리소스 유형은 [RFC 1123](https://tools.ietf.org/html/rfc1123)에 정의된 대로 DNS 레이블 표준을 따라야 한다. 이것은 이름이 다음을 충족해야 한다는 것을 의미한다.

- 최대 63자이다.
- 소문자와 영숫자 또는 `-` 만 포함한다.
- 영숫자로 시작한다.
- 영숫자로 끝난다.

#### 경로 세그먼트 이름

일부 리소스 유형에서는 이름을 경로 세그먼트로 안전하게 인코딩 할 수 있어야 한다. 즉 이름이 "." 또는 ".."이 아니며 이름에는 "/" 또는 "%"가 포함될 수 없다.

아래는 파드의 이름이 `nginx-demo`라는 매니페스트 예시이다.

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: nginx-demo
spec:
  containers:
  - name: nginx
    image: nginx:1.14.2
    ports:
    - containerPort: 80
```

### UID

오브젝트를 중복 없이 식별하기 위해 쿠버네티스 시스템이 생성하는 문자열.

쿠버네티스 클러스터가 구동되는 전체 시간에 걸쳐 생성되는 모든 오브젝트는 서로 구분되는 UID를 갖는다. 이는 기록상 유사한 오브젝트의 출현을 서로 구분하기 위함이다.

쿠버네티스 UID는 보편적으로 고유한 식별자이다(또는 UUID라고 한다). UUID는 ISO/IEC 9834-8 과 ITU-T X.667 로 표준화 되어 있다.