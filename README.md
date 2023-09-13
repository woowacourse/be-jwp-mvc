# @MVC 구현하기

## 1단계 @MVC 프레임워크 구현하기

### 기존 코드 흐름 이해하기

- `Tomcat`에 `DispatcherServlet`를 등록한다.
    - `DispatcherServlet`은 `ManualHandlerMapping`을 가지고 있고, 이를 이용해 응답을 처리한다.
    - `ManualHandlerMapping`에는 개발자가 작성한 `Controller`를 `Path`와 매핑할 수 있다.
    - init() 메서드로 초기화해주어야 사용할 수 있다.
    - `DispatcherServletInitializer`로 DispatcherServlet 초기화헤야 한다.
- `DispatcherServletInitializer`의 `onStartUp`을 실행한다.
    - `DispatcherServletInitializer`는 `WebApplicationInitializer` 인터페이스를 구현한다.
    - `SpringServletContainerInitializer`에서 Reflection 으로 이 클래스를 찾아 해당 메서드를 실행한다.
    - `ServletContext`에 `DispatcherServlet`을 등록한다. 아마 여기서 init()을 해줄 것이다.
    - 아직 정확히 뭔지는 모르겠지만 ...`Registeration` 설정을 해준다.
        - `DispatcherServlet`의 `loadOnStartUp` 값 설정,  "/"에 매핑

- Path와 Controller는 매핑되어있지만, HttpMethod와는 매핑되어있지 않다. (미션에서 진행해야 한다.)
    - 그래서 /login, /login/view로 구분되어 있다.

### 요구사항

- [ ] @RequestMapping 어노테이션으로 URL 및 메서드를 컨트롤러에 매핑
    - [x] `AnnotationHandlerMapping`에서 어노테이션에 따른 매핑을 구현한다. (초기화)
    - [x] `HandlerExecution`의 `handle` 메서드를 구현한다.
    - [ ] `ManualHandlerMapping` 대신 `AnnotationHandlerMapping` 클래스를 사용한다.
    - [ ] `RequestMapping` 어노테이션을 사용한 핸들러 메서드를 작성한다.

