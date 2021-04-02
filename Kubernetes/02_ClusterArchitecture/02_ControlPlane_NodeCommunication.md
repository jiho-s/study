## 컨트롤 플레인-노드 간 통신

이 문서는 컨트롤 플레인(실제로 컨트롤 플레인의 API 서버)과 쿠버네티스 클러스터 사이에 대한 통신 경로의 목록을 작성한다. 이는 사용자가 신뢰할 수 없는 네트워크(또는 클라우드 공급자의 완전한 퍼블릭 IP)에서 클러스터를 실행할 수 있도록 네트워크 구성을 강화하기 위한 맞춤 설치를 할 수 있도록 한다.

### 노드에서 컨트롤 플레인으로의 통신

쿠버네티스에는 "허브 앤 스포크(hub-and-spoke)" API 패턴을 가지고 있다. 노드(또는 노드에서 실행되는 파드들)의 모든 API 사용은 API 서버에서 종료된다(다른 컨트롤 플레인 컴포넌트 중 어느 것도 원격 서비스를 노출하도록 설계되지 않았다). API 서버는 하나 이상의 클라이언트 [인증](https://kubernetes.io/docs/reference/access-authn-authz/authentication/) 형식이 활성화된 보안 HTTPS 포트(일반적으로 443)에서 원격 연결을 수신하도록 구성된다. 특히 [익명의 요청](https://kubernetes.io/docs/reference/access-authn-authz/authentication/#anonymous-requests) 또는 [서비스 어카운트 토큰](https://kubernetes.io/docs/reference/access-authn-authz/authentication/#service-account-tokens)이 허용되는 경우, 하나 이상의 [권한 부여](https://kubernetes.io/ko/docs/reference/access-authn-authz/authorization/) 형식을 사용해야 한다.

API 서버에 연결하려는 파드는 쿠버네티스가 공개 루트 인증서와 유효한 베어러 토큰(bearer token)을 파드가 인스턴스화될 때 파드에 자동으로 주입하도록 서비스 어카운트를 활용하여 안전하게 연결할 수 있다. `kubernetes` 서비스(`default` 네임스페이스의)는 API 서버의 HTTPS 엔드포인트로 리디렉션되는 가상 IP 주소(kube-proxy를 통해)로 구성되어 있다.

컨트롤 플레인 컴포넌트는 보안 포트를 통해 클러스터 API 서버와도 통신한다.

결과적으로, 노드 및 노드에서 실행되는 파드에서 컨트롤 플레인으로 연결하기 위한 기본 작동 모드는 기본적으로 보호되며 신뢰할 수 없는 네트워크 및/또는 공용 네트워크에서 실행될 수 있다.

### 컨트롤 플레인에서 노드로의 통신