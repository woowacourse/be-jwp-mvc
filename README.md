# @MVC 구현하기

## 기능 구현 사항

### 1단계

- [x] AnnotationHandlerMapping을 구현한다.
- [x] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping을 처리한다.
- [x] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 ManualHandlerMapping을 처리할 수 있다.

### 2단계

- [x] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 찾을 수 있다.
- [x] HandlerMappingRegistry 클래스에서 HandlerMapping을 처리하도록 구현했다.
- [x] HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리하도록 구현했다.

### 3단계

- [x] 힌트에서 제공한 UserController가 json 형태로 응답을 반환한다.
- [x] 레거시 코드를 삭제하고 서버를 띄워도 정상 동작한다.
