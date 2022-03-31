# bookstore

Springboot 와 JPA를 이용하여 간단한 서점 웹 애플리케이션을 구현 했습니다.

## 프로젝트 구조

/src/main/java/shop/bookstore
  
  /item - 한 회원이 여러 상품을 주문할 수 있으므로 OneTomany로 Entity 관계를 설정
  
  /member - 회원 가입시 @Valid Annotation을 통해 Http의 요청이 정상인지 검증
  
  /order - Delivery 와는 OneToOne Entity 관계를 가짐
         - findAllByString method는 동적으로 쿼리를 생성해 Entity를 검색한다.
         - Given 절에서 test를 위한 member와 item을 만들고 When 절에서 item을 주문하고 Then 절에서 orderPrice가 올바른지, 주문 후 stockQuantity이 정확히 줄었는지 체크.
  
  /utility - 수량 초과로 인한 에러 메세지 처리
  
  XXXController :  웹 계층
  XXXService: business logic, Transaction 처리
  XXXRepository: JPA를 사용하는 계층, EntityManeger 사용
  Domain


## API
MemberController

create
@PostMapping Annotation을 통해 path 값 "/members/new" 을 URL 에 매핑
@Valid 를 통해 MemberForm parameter 검증
control statement 를 통해 Error Handling

list
@GetMapping Annotation을 통해 path 값 "members" 을 URL 에 매핑
model.addAttribute 를 통해 "members" 라는 이름을 갖는 Object value 추가

## TO-DO
1. xxx 사용해서 unit test 작성
2. 
3. 2022/03/30 현재는 동작을 목표로 한 order에 한가지 item만 가능하도록 구현했다. 추후 수정
4. Valid Error 검출시 Handling 과 각종 Error 검출 및 Handling
5. setName setPrice set 방식은 보안상 문제가 될 수 있으니 추후 개선
6. category item 간 ManyToMany OneToMany ManyToOne 으로 풀어내기
7. setter 최대한 제거
8. 유지보수를 위해 Entity 다듬기
9. update() 부분에서 merge를 dirty checking 방식으로 바꾸기
