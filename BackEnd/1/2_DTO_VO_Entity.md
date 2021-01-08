## DTO, VO, Entity

### Entity

id가 있고 변경가능한 객체.

각 entity는 속성이 아닌 id로 식별한다. 따라서 두 entity는 속성이 다르더라도 같은 id를 가지고 있으면 같은 entity이다.

### Value Object

VO는 수정할 수 없다. VO는 id가 없으며, 모든 속성이 동일한 경우 같은 객체이다.

### DTO

데이터 전송객체이다.  여러 값을 호출해야하는경우 이를 하나로 호출하기 위해 사용