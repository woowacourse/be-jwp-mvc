### MVC 프레임워크

- 동적으로 URL과 Controller 매핑
- 비즈니스 로직에만 집중할 수 있도록 어노테이션 기반 MVC 프레임워크

### TODO

- [ ] AnnotationHandlerMappingTest
- [ ] AnnotationHandlerMapping.initialize()
    - [ ] handlerExecutions 초기화
    - `@RequestMapping` value 가져오기
    - key = URL&HTTP 메서드
    - value = 컨트롤러
- [ ] HandlerExecution
    - [ ] `@RequestMapping`있는 메서드 리플렉션으로 메서드 호출
- [ ] `@RequestMapping`
    - [ ] URL
    - [ ] HTTP 메서드
        - [ ] 설정되어 있지 않으면 모든 메서드 지원
- [ ] Controller
    - [ ] ModelAndView 반환
- [ ] JspView
    - [ ] DispatcherServlet 클래스의 service 메서드에서 뷰에 대한 처리 파악
    - [ ] JspView 클래스로 이동