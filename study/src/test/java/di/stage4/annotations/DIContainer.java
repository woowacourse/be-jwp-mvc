package di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import di.ConsumerWrapper;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) throws Exception {
        this.beans = createBeans(classes);
        beans.forEach(this::setFields);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) throws Exception {
        Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(rootPackageName).stream()
                .filter(aClass -> aClass.isAnnotationPresent(Service.class) || aClass.isAnnotationPresent(Repository.class))
                .collect(Collectors.toSet());
        return new DIContainer(classes);
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) throws Exception {
        Set<Object> beans = new HashSet<>();
        for (Class<?> aClass : classes) {
            Constructor<?> constructor = aClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            beans.add(constructor.newInstance());
        }
        return beans;
    }

    private void setFields(final Object bean) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Inject.class)) {
                setFields(bean, field);
            }
        }
    }

    private void setFields(final Object bean, final Field field) {
        Class<?> type = field.getType();
        field.setAccessible(true);

        beans.stream()
                .filter(type::isInstance)
                .forEach(ConsumerWrapper.accept(target -> field.set(bean, target)));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Cannot find Bean"));
    }
}
