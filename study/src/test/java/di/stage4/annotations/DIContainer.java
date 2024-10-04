package di.stage4.annotations;

import di.ConsumerWrapper;
import di.FunctionWrapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        final var allClassesInPackage = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return allClassesInPackage.stream()
                .filter(aClass -> aClass.isAnnotationPresent(Service.class) || aClass.isAnnotationPresent(Repository.class))
                .collect(Collectors.collectingAndThen(Collectors.toUnmodifiableSet(), DIContainer::new));
    }

    private Set<Object> createBeans(Set<Class<?>> classes) {
        return classes.stream()
                .map(FunctionWrapper.apply(Class::getDeclaredConstructor))
                .peek(constructor -> constructor.setAccessible(true))
                .map(FunctionWrapper.apply(Constructor::newInstance))
                .collect(Collectors.toSet());
    }

    private void setFields(Object bean) {
        Arrays.stream(bean.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .forEach(field -> setField(bean, field));
    }

    private void setField(Object bean, Field field) {
        Class<?> fieldType = field.getType();
        field.setAccessible(true);
        beans.stream()
                .filter(fieldType::isInstance)
                .peek(ConsumerWrapper.accept(fieldBean -> field.set(bean, fieldBean)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(fieldType + "에 해당하는 빈을 찾을 수 없습니다."));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> bean.getClass().isAssignableFrom(aClass))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(aClass + "에 해당하는 빈을 찾을 수 없습니다."));
    }
}
