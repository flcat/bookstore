# bookstore

Springboot 와 JPA를 이용하여 간단한 서점 웹 애플리케이션을 구현했다.
h2 db를 이용했으며 회원 가입 회원 목록 상품 등록 수정 주문 주문 확인 검색등의 기능을 구현했다.

## 프로젝트 구조

/src/main/java/shop/bookstore
  
  * /item
    * 한 회원이 여러 상품을 주문할 수 있으므로 OneTomany로 Entity 관계를 설정했다.
  
  * /member
    *  회원 가입시 @Valid 통해 Http의 요청이 정상인지 검증한다.
  
  * /order 
    * Delivery 와는 OneToOne Entity 관계를 가진다
    * findAllByString method는 동적으로 쿼리를 생성해 Entity를 검색한다.
    * Given 절에서 test를 위한 member와 item을 만들고 When 절에서 item을 주문하고,
      Then 절에서 orderPrice가 올바른지, 주문 후 stockQuantity이 정확히 줄었는지 체크한다.
  
  * /utility 
    * 수량 초과로 인한 에러 메세지를 처리한다.
  
  #### XXXController :  웹 계층
  #### XXXService: business logic, Transaction 처리
  #### XXXRepository: JPA를 사용하는 계층, EntityManeger 사용
  #### Domain


## API

중복이 되는 Annotation 설명은 생략 했다.

### member
    회원의 이름, 도시, 거리, 우편번호를 가지고 있다.
    Order 와 OneToMany 관계를 가지고 있다.
  
  * MemberController
    * createForm 
      - 회원을 등록하는 Form View를 생성한다.
    * create
      - View에서 입력받은 값들을 해당 회원 Entity에 전달한 뒤 index화면으로 redirect 한다.
      - @PostMapping 통해 path 값 "/members/new" 을 URL 에 매핑
      -  @Valid 를 통해 MemberForm parameter 검증
          control statement 를 통해 Error Handling
    * list
      - 회원 목록을 model.addAttribute()를 통해 받아옴
      -  @GetMapping 통해 path 값 "members" 을 URL 에 Mapping
         model.addAttribute 를 통해 "members" 라는 이름을 갖는 Object value 추가

  * MemberService
      @transactional 통해
      join() method 실행중 다른 연산이 끼어들 수 없게 함
      오류가 생긴 경우에 연산을 취소하고 rollback
      validateDuplicateMember를 통해 중복 회원 이름 검증.
            

### item
    상품의 이름, 가격, 재고수량, 도서번호를 가지고 있고 상품을 주문하면 재고수량이 줄어든다.
  * ItemController
    * createForm 
      - 상품을 등록하는 Form View를 생성한다.
    * create
      - View에서 입력받은 값들을 해당 상품 Entity에 전달한 뒤 items로 redirect 한다.
    * list
      - 상품 목록을 model.addAttribute()를 통해 받아옴
    * updateItemForm
      - itemId를 유동적으로 얻어와 Mapping 한 뒤 해당 item 의 값들을 View에 전달한다.
    * updateItem
      - updateItemForm에서 얻어온 값들을 수정하면 해당 itemId로 수정된 값을 전달하고 items로 redirect 한다.

### order
    회원의 이름, 배송 상태, 주문 상품에 대한 정보를 가지고 있다.
    주문을 취소하면 재고수량이 다시 늘어난다.
    주문 검색이 가능하며 주문 상태로 결과를 filter 할 수 있다.

* OrderController
    * createForm 
      - 주문하는 Form View를 생성한다.
    * order
      - @RequestParam으로 해당 값을 요청해 각 parameter에 넣어주고 Entity에 전달한 뒤 orders로 redirect 한다.
    * orderList
      - 주문 목록을 model.addAttribute()를 통해 받아온다.
    * cancelOrder
      - @PathVariable 해당 orderId로 수정된 값을 전달받고 Entity 조회 후 cancelOrder()한 뒤 orders로 redirect 한다.

* Delivery
  * @Id
     - primary key 지정한다.
  * @GeneratedValue    
    - 함께 사용하므로써 primary key 자동 생성한다.
       빠른 구현을 위해 Order와 @OneToOne 단방향 상관 관계를 갖고 
       (양방향의 경우 LAZY로 설정해도 EAGER로 동작할 수 있다.)
       fetch 전략을 LAZY로 설정하므로써 연관 관계에 있는 Entity 가져오지 않고 Getter 로 접근할 때 가져온다.
       (해당 project에서는 모든 fetch전략을 LAZY로 설정했다.)
  * @Embedded
    - city street zipcode를 address로 묶어 가독성을 높였다.

  * @Enumerated 
    - Enum값을 문자열(EnumType.STRING)로 저장될 수 있도록 선언함. 기본적으로 int 값으로 저장됨.

* Order
  * @NoArgsConstructor 
    - 파라미터가 없는 기본 생성자를 생성한다.
  * @Entity @Table
    - Object와 Table Mapping
  * @Column 
    - Column명 Mapping
  * @OntoMany
     - 한가지 order에 여러 상품 주문 가능하게 설정 cascade로 상위 Entity에서 하위 Entity로 모든 작업을 전파한다.

* OrderRepository
  * @RequiredArgsConstructor 
     - 초기화 되지않은 final field나 @NonNull이 붙은 field에 대해
        생성자를 생성해 . Dependency Injection 위해서 사용했다.
 
  * @Repository
     - @Component을 사용해도 되지만  @Repository에 해당 Annotation의 기능이
        포함되어 있고 @Repository를 적음으로 명시적으로 역할을 나타냈다.

* HomeController
  * @slf4j
     - wrapper 이다. @slf4j 설정에 따라 다른 logging library를 사용할 수 있기
        때문에 log4j로 변경하는 등 migration process가 간단해졌다.

### test
    unit test 작성
    @RunWith(SpringRunner.class)해당 Annotation을 이용하면
    @Autowired에 해당하는 것들만 Application Context를 로딩하므로
    Junit4의 필요조건에 맞추어 사용 가능했다.

## TO-DO
  1. Junit4 사용해서 더 많은 unit test 작성이 필요하다.
  2. 2022/03/30 현재는 동작을 목표로 한 order에 한가지 item만 가능하도록 구현했다. 추후 수정
  3. Valid Error 검출시 Handling 과 각종 Error 검출 및 Handling
  4. category item 간 ManyToMany OneToMany ManyToOne 으로 풀어내기
  5. update() 부분에서 merge를 dirty checking 방식으로 바꾸기
