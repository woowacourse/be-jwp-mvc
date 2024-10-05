package di.stage4.annotations;

import di.ConsumerWrapper;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import org.reflections.Reflections;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    private DIContainer(final Set<Class<?>> classes) {
        this.beans = new HashSet<>();
        try {
            createBeans(classes);
            initBeans(beans);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Reflections reflections = new Reflections(rootPackageName);
        HashSet<Class<?>> annotatedClasses = new HashSet<>();
        List<Class<? extends Annotation>> classes = List.of(Repository.class, Service.class);
        for (Class<? extends Annotation> aClass : classes) {
            Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(aClass);
            annotatedClasses.addAll(typesAnnotatedWith);
        }
        return new DIContainer(annotatedClasses);
    }

    private void createBeans(Set<Class<?>> classes)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Class<?> aClass : classes) {
            Constructor<?> constructor = aClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object bean = constructor.newInstance();
            beans.add(bean);
        }
    }

    private void initBeans(Set<Object> beans) throws IllegalAccessException {
        for (Object bean : beans) {
            setFields(bean);
        }
    }

    private void setFields(Object bean) {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Inject.class)) {
                setFiled(bean, field);
            }
        }
    }

    private void setFiled(Object bean, Field field) {
        Class<?> typeOfFiled = field.getType();
        beans.stream()
                .filter(typeOfFiled::isInstance)
                .findFirst()
                .ifPresent(ConsumerWrapper.accept(target -> field.set(bean, target)));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(object -> object.getClass().equals(aClass))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}
