# MVC 프레임워크 구현하기

## todo

- [ ] ViewResolver를 만들었다고?
- [ ] MvcException 이름이 적절한가? + Adapter 관련 exception은 왜 MvcException을 상속하지 않는가?
- [ ] HandlerMappings 의 필드 이름을 mappings로 수정한 이유가 무엇인가 - 답변

- [x] DispatcherServlet가 아닌 뷰에서 JSP 반환하도록 한다 - 이전 미션에서 완료
- [x] JSON 뷰를 추가한다
- [x] 레거시 MVC를 제거한다
- [x] UserController가 잘 작동되는지 확인한다

- [x] `ReflectionTest`의 테스트를 통과시킨다
- [x] `ReflectionsTest`를 완성하여 로그를 출력한다
- [x] `Junit3TestRunner`를 이용하여 `Junit3Test`에서 test로 시작하는 메서드를 실행한다
- [x] `Junit4TestRunner`를 이용하여 `Junit4Test`에서 `@MyTest`어노테이션이 있는 메서드를 실행한다

## 기능 요구 사항

- [x] AnnotationHandlerMapping 구현

- [x] 기존의 컨트롤러를 annotation 기반으로 변경하더라도 정상 작동
  - [x] DispatcherServlet이 ModelAndView를 처리할 수 있도록 기능 추가
  - [x] JspView render 메서드 구현
- [x] 새로운 TestController를 포함하면 해당 컨트롤러의 기능이 애플리케이션에서 작동
- [x] HandlerAdapter 이용하여 DispatcherServlet 전략 추가 가능하도록 구현

- [x] HandlerMappings, HandlerAdapters 일급컬렉션으로 리팩토링
  - [x] HandlerMappings
  - [x] HandlerAdapters

- [x] TODO 처리
