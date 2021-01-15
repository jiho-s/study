## equals, hashCode

두 객체의 equals가 true면 hashCode도 같아야 한다.

## UsernamePasswordAuthenticationFilter

username과 password 를 가져와 

authentication을 만든다. 이를 authenticationManager에 던진다.

##  ProviderManger

AutenticationManager의 구현체로 AutenticationProvider의 리스트를 들고 있고 적절한 Provider에게 위임한다.



## AccessDecisionManager

AccessDecisionVoter 리스트를 가지고 있다.

## AccessDecisionVoter

승인 할지 보류할지 거절할지 선택