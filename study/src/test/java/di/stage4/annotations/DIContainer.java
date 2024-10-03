package di.stage4.annotations;

import static java.util.stream.Collectors.toSet;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = instantiateBeans(classes);
        beans.forEach(this::injectDependencies);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(classes);
    }

    private Set<Object> instantiateBeans(Set<Class<?>> classes) {
        return classes.stream()
                .map(clazz -> {
                    try {
                        Constructor<?> constructor = clazz.getDeclaredConstructor();
                        constructor.setAccessible(true);
                        return constructor.newInstance();
                    } catch (Exception e) {
                        throw new IllegalArgumentException("빈 생성 불가");
                    }
                })
                .collect(toSet());
    }

    private void injectDependencies(Object bean) {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        Arrays.stream(declaredFields)
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .forEach(field -> {
                    Object dependency = getBean(field.getType());
                    field.setAccessible(true);
                    try {
                        field.set(bean, dependency);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("의존성 주입 실패");
                    }
                });
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 타입의 빈이 존재하지 않음"));
    }
}
