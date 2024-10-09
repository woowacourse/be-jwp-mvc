package di.stage4.annotations;

import di.ConsumerWrapper;
import di.FunctionWrapper;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::assignFields);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return classes.stream()
                .filter(DIContainer::hasServiceOrRepositoryAnnotation)
                .collect(Collectors.collectingAndThen(Collectors.toUnmodifiableSet(), DIContainer::new));
    }

    private static boolean hasServiceOrRepositoryAnnotation(Class<?> aClass) {
        return aClass.isAnnotationPresent(Service.class) || aClass.isAnnotationPresent(Repository.class);
    }

    private Set<Object> createBeans(Set<Class<?>> classes) {
        return classes.stream()
                .map(FunctionWrapper.apply(Class::getDeclaredConstructor))
                .peek(constructor -> constructor.setAccessible(true))
                .map(FunctionWrapper.apply(constructor -> {
                    Object instance = constructor.newInstance();
                    constructor.setAccessible(false);
                    return instance;
                }))
                .collect(Collectors.toUnmodifiableSet());
    }

    private void assignFields(Object bean) {
        Arrays.stream(bean.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .peek(field -> field.setAccessible(true))
                .forEach(field -> {
                    assignField(field, bean);
                    field.setAccessible(false);
                });
    }

    private void assignField(Field field, Object bean) {
        beans.stream()
                .filter(field.getType()::isInstance)
                .forEach(ConsumerWrapper.accept(fieldBean -> field.set(bean, fieldBean)));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("bean을 찾을 수 없습니다."));
    }
}
