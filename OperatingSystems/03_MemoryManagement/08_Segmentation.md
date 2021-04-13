## Segmentation

각각의 오브젝트(Symboltable, Sourcetext, Constant table, Parse tree, Call stack) 마다 vitual Address를 할당

##### Segment

각각의 서로 독립된 주소 공간들로 0부터 시스템에서 허용된 최대 크기까지 선형구조

- 증가하거나 감소하는 자료 구조의 관리를 간단하게한다.
- 세그먼트로 된 함수가 재컴파일시 다른파일은 수정할 필요가 없다
- 함수나 데이터를 여러 프로세스가 공유하기 쉽다.
- 서로 다른 세그먼트에 서로 다른 보호를 적용할 수 있다.

### Implemnetation of Pure Segmentation

페이지는 크기가 고정되어 있지만, 세그멘테이션은 가변이다. 세그멘테이션만 사용하면 외부 단편화가 발생한다.

#### Segmentation with Paging: MULTICS

세그먼트마다 하나의 디스크립터를 갖는다. 디스크립터마다 페이지 테이블을 갖는다.