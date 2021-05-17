## 서비스

파드 집합에서 실행중인 애플리케이션을 네트워크 서비스로 노출하는 추상화 방법. 쿠버네티스는 파드에게 고유한 IP 주소와 파드 집합에 대해 단일 DNS 명을 부여하고, 그것들 간에 로드-밸런스를 수행할 수 있다.

### 동기

쿠버네티스의 파드는 클러스터의 상태와 일치하도록 생성되고 삭제된다. 파드는 비영구적 리소스이다. 만약 앱을 실행하기 위해 디플로이먼트를 사용한다면, 동적으로 파드를 생성하고 제거할 수있따.

각 파드는 고유한 IP 갖지만, 디플로이먼트에서는 한 시점에 실행되는 파드 집합이 잠시 후 실행되는 해당 파드 집합과 다를 수 있다.

이는 다음과 같은 문제를 발생시킨다. 일부 파드 집합(백엔드)가 다른 파드에 기능을 제공(프론트앤드)하는 경우, 프론트엔드가 워크로드의 백엔드를 사용하기 위해, 프론트엔드가 어떻게 연결할 IP 주소를 찾아서 추적할 수 있을까?

### 서비스 리소스

쿠버네티스에서 서비스는 파드의 논리적 집합과 그것들에 접근할 수 있는 정책을 정의하는 추상적인 개념이다. (이를 마이크로 서비스라고 한다) 서비스가 대상으로 하는 파드 집합은 일반적으로 셀렉터가 결정한다.

예를 들어 3개의 레플리카로 실행되는 스테이트리스 이미지-처리 백엔드를 생각해보자. 백엔드 세트를 구성하는 실제 파드는 변경될 수 있지만, 프론트엔드 클라이언트는 이를 인식할 필요가 없으며, 백엔드 세트 자체를 추적해야 할 필요도 없다.

#### 클라우드-네이티브 서비스 디스커버리

애플리케이션에서 서비스 발견을 위해 쿠버네티스 API를 사용할 수 있는 경우 서비스 내 파드 세트가 변경될 때마다 업데이트되는 엔드포인트를 API서버에 질의 할 수 있다

네이티브 애플리케이션이 아닌 경우, 쿠버네티스는 애플리케이션과 백엔드 파드 사이에 네트워크 포트 또는 로드밸런서를 배치할 수 있는 방법을 제공한다.

### 서비스 정의

쿠버네티스의 서비스는 파드와 비슷한 REST 오브젝트이다. 모든 REST 오브젝트와 마찬가지로 서비스 정의를 API 서버에 `POST` 하여 새 인스턴스를 생서할 수 있다. 서비스 오브젝트의 이름은 유효안 DNS 서브도메인 이름이여야한다.

예를 들어 각각 TCP 포트 9376에서 수신하고 `app=Myapp`  레이블을 가지고 있는 파드 세트가 있다고 가정해보자.

```yaml
apiVersion: v1
kind: Service
metadata:
  name: my-service
spec:
  selector:
    app: MyApp
  ports:
    - protocol: TCP
      port: 80
      targetPort: 9376
```

이 명세는 **my-service**라는 새로운 서비스 오브젝트를 생성하고, `app=MyApp` 레이블을 가지고 있는 파드의 TCP 9386 포트를 대상으로 한다.

쿠버네티스는 이 서비스에 프록시가 사용하는 IP주소를 할당한다.

서비스 셀렉터의 컨트롤러는 셀렉터와 일치하는 파드를 지속적으로 검색하고 **my-service**라는 엔드포인트 오브젝트에 대한 모든 업데이트를 `POST` 한다.

파드에서 포트 정의에는 이름이 있고 `targetPort` 속석에 이 이름을 참조하여 설정할 수 있다.

#### 셀렉터가 없는 서비스

서비스는 일반적으로 쿠버네티스 파드에 대한 접근을 추상화하지만, 다른 종류의 백엔드를 추상화할 수도 있다.

- 로덕션 환경에서는 외부 데이터베이스 클러스터를 사용하려고 하지만, 테스트 환경에서는 자체 데이터베이스를 사용한다.
- 한 서비스에서 다른 [네임스페이스](https://kubernetes.io/ko/docs/concepts/overview/working-with-objects/namespaces) 또는 다른 클러스터의 서비스를 지정하려고 한다.
- 워크로드를 쿠버네티스로 마이그레이션하고 있다. 해당 방식을 평가하는 동안, 쿠버네티스에서는 백엔드의 일부만 실행한다.

이러한 경우 파드에 셀렉터 없이 서비스를 정의 할 수 있다.

```yaml
apiVersion: v1
kind: Service
metadata:
  name: my-service
spec:
  ports:
    - protocol: TCP
      port: 80
      targetPort: 9376
```

엔드포인트 오브젝트를 수동으로 추가하여, 서비스를 실행 중인 네트워크 주소 및 포트에 서비스를 수동으로 매핑할 수 있다.

```yaml
apiVersion: v1
kind: Endpoints
metadata:
  name: my-service
subsets:
  - addresses:
      - ip: 192.0.2.42
    ports:
      - port: 9376
```
