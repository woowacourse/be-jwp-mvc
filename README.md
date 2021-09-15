# MVC 프레임워크 구현하기

## 1단계 - MVC 프레임워크 구현하기
- [x] 학습테스트 통과시키기
  - [x] Junit3TestRunner
  - [x] Junit4TestRunner
  - [x] ReflectionsTest
  - [x] ReflectionTest
  - [x] AnnotationHandlerMappingTest

- [x] JspView 구현
- [x] ManualHandlerMapping.java 와 AnnotationHandlerMapping.java 모두 사용할 수 있도록 변경
- [x] `app/webapp/WEB-INF/web.xml` 에 정적 리소스 매핑 추가
- [x] Legacy MVC와 Annotation Based MVC 통합 - RegisterController

### 구구 코드리뷰
- [x] HandlerAdapter 적용, move 메서드 삭제 및 JspView 활용

### 조앤 코드리뷰 - 1차
- [x] 회원가입 시 로그인 상태가 되는 기능 추가
- [x] setAccessible(true) -> trySetAccessible() 로 변경
- [x] HandlerAdapter 외부에서 주입하도록 수정
- [x] HandlerMappings, HandlerAdapters 일급 컬렉션화
- [x] RequestMapping 애노테이션 수정
- [x] Service 레이어 및 회원가입 검증로직 추가

##  2단계 - 리팩터링
- [x] ControllerScanner 추가
  - [x] Relfections 라이브러리를 사용한다.
  - [x] Relfections 객체로 @Controller가 설정된 모든 클래스를 찾는다.
  - [x] 각 클래스의 인스턴스를 생성한다.


## 3단계 - View 구현하기
- [x] View 구현하기
  - [x] JSON View 구현

- [x] Legacy MVC 제거하기
  - [x] asis 패키지에 있는 레거시 코드를 삭제해도 서비스가 정상 동작하도록 구현


### 조앤 코드리뷰 - 1차
- [x] Controller Annotation이 달려있는 클래스 스캔 패키지 범위 더 좁게 수정
- [x] View name 분리해서 관리하도록 수정
- [ ] API Controller 패키지 분리 
- [ ] 불필요한 객체 생성을 막도록 private 생성자 추가
- [ ] 로그인한 사용자가 `GET /register` 요청 시, 루트 경로로 리다이렉트 하도록 수정
- [ ] app 모듈의 클래스 테스트 코드 추가
  - [ ] controller 패키지
    - [ ] HelloController
    - [ ] HomeController
    - [ ] LoginController
    - [ ] LogoutController
    - [ ] RegisterController
    - [ ] UserController
  - [ ] domain 패키지
   - [ ] User.class
  - [ ] repository 패키지
    - [ ] InMemoryUserRepository.class
  - [ ] service 패키지
    - [ ] LoginService.class
    - [ ] RegisterService.class
    - [ ] UserService.class
  - [ ] session 패키지
    - [ ] UserSession.class 
- [ ] mvc 모듈의 클래스 테스트 코드 추가
  - [ ] controller 패키지
    - [ ] AnnotationHandlerMapping.class
    - [ ] ControllerScanner.class
    - [ ] HandlerExecution.class
    - [ ] HandlerKey.class
  - [ ] handler 패키지
    - [ ] adapter 패키지
      - [ ] AnnotationHandlerAdapter.class
      - [ ] HandlerAdapters.class
    - [ ] mapping 패키지
      - [ ] HandlerMappings.class
  - [ ] servlet 패키지
    - [ ] DispatcherServlet.class
  - [ ] view 패키지
    - [ ] JsonView.class
    - [ ] JspView.class
    - [ ] ModelAndView.class
  - [ ] support 패키지
    - [ ] MediaType.class
    - [ ] RequestMethod.class