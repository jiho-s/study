## Spring Framwork JDBC

### JDBC 데이터베이스 접근 방식

Spring Framework JDBC에서는 데이터베이스에 접근할 때 다음과 같은 방식중에 선택할 수 있다.

- `JdbcTemplate`

  가장 고전적인 방법으로 모든 접근 방식은 내부적으로 `JdbcTemplate`를 사용한다

- `NamedParameterJdbcTemplate`

  `JdbcTemplate` 의 `?`를 네임드 파라미터로 접근하기위해 사용한다.

- `SimpleJdbcInsert`와 `SimpleJdbcCall`

  테이블의 이름과 또는 프로시저 이름과 매개 변수 그리고 해당 컬럼의 이름을 주어서 sql 구문을 자동으로 만들어 준다.

- RDBMS objects(`MappingSqlQuery`, `SqlUpdate`, `StoredProcedure`)

### 