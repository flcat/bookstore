# bookstore

Springboot 와 JPA를 이용하여 간단한 서점 웹 애플리케이션을 구현 했습니다.

## 프로젝트 구조
/src/main/java/shop/bookstore
  /item - 한 회원이 여러 상품을 주문할 수 있으므로 OneTomany로 Entity 관계를 설정
  
  /member - 회원 가입시 @Valid Annotation을 통해 Http의 요청이 정상인지 검증
  
  /order - Delivery 와는 OneToOne Entity 관계를 가짐
         - findAllByString method는 동적으로 쿼리를 생성해 Entity를 검색한다.
         - 
  
  /utility -

## API
각 api endpoint마다 method와 파라미터 등등 request/response 구조 설명

## TO-DO
1. xxx 사용해서 unit test 작성
2. Valid error 검출시 Handling
3. setName setPrice set 방식은 보안상 문제가 될 수 있으니 추후 개선
4. category item 간 ManyToMany OneToMany ManyToOne 으로 풀어내기
5. setter 최대한 제거
6. 유지보수를 위해 Entity 다듬기
7. update() 부분에서 merge를 dirty checking 방식으로 바꾸기
