package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<Question> clazz = Question.class;

        assertThat(clazz.getSimpleName()).isEqualTo("Question");  // 클래스명
        assertThat(clazz.getName()).isEqualTo("reflection.Question");  // 패키지 포함 클래스명
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");  // Inner Class의 경우 $를 구분자로 사용하여 표기
    }

    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        final Class<?> clazz = Class.forName("reflection.Question");

        assertThat(clazz).isEqualTo(Question.class);
        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        final Object student = new Student();

        // getDeclaredFields()는 해당 클래스에 선언된 모든 필드를 가져온다.
        // getFields()는 해당 클래스와 상속받은 클래스의 public 필드만 가져온다.
        final Field[] fields = student.getClass().getDeclaredFields();
        final List<String> actualFieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .toList();

        assertThat(actualFieldNames).contains("name", "age");
    }

    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        final Class<?> studentClass = Student.class;
        final Method[] methods = studentClass.getDeclaredMethods();
        final List<String> actualMethods = Arrays.stream(methods)
                .map(Method::getName)
                .toList();

        assertThat(actualMethods)
                .hasSize(3)
                .contains("getAge", "toString", "getName");
    }

    @Test
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();

        assertThat(constructors).hasSize(2);
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        final Constructor<?> firstConstructor = questionClass.getDeclaredConstructor(
                String.class, String.class, String.class
        );
        final Constructor<?> secondConstructor = questionClass.getDeclaredConstructor(
                long.class, String.class, String.class, String.class, Date.class, int.class
        );

        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = (Question) secondConstructor.newInstance(
                1L, "gugu", "제목2", "내용2", new Date(), 0
        );

        assertThat(firstQuestion.getWriter()).isEqualTo("gugu");
        assertThat(firstQuestion.getTitle()).isEqualTo("제목1");
        assertThat(firstQuestion.getContents()).isEqualTo("내용1");
        assertThat(secondQuestion.getWriter()).isEqualTo("gugu");
        assertThat(secondQuestion.getTitle()).isEqualTo("제목2");
        assertThat(secondQuestion.getContents()).isEqualTo("내용2");
    }

    @Test
    void givenClass_whenGetsPublicFields_thenCorrect() {
        final Class<?> questionClass = Question.class;

        // 두 방법 모두 가능
        final Field[] fields = questionClass.getFields();  // 상속받은 필드도 포함한다.
        final Field[] fields2 = Arrays.stream(questionClass.getDeclaredFields())
                .filter(field -> Modifier.isPublic(field.getModifiers()))
                .toArray(Field[]::new);  // 상속받은 필드는 포함하지 않는다.

        assertThat(fields).isEmpty();
        assertThat(fields2).isEmpty();
    }

    @Test
    void givenClass_whenGetsDeclaredFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getDeclaredFields();

        assertThat(fields).hasSize(6);
        assertThat(fields[0].getName()).isEqualTo("questionId");
    }

    @Test
    void givenClass_whenGetsFieldsByName_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;
        final Field field = questionClass.getDeclaredField("questionId");

        assertThat(field.getName()).isEqualTo("questionId");
    }

    @Test
    void givenClassField_whenGetsType_thenCorrect() throws Exception {
        final Field field = Question.class.getDeclaredField("questionId");
        final Class<?> fieldClass = field.getType();

        assertThat(fieldClass.getSimpleName()).isEqualTo("long");
    }

    @Test
    void givenClassField_whenSetsAndGetsValue_thenCorrect() throws Exception {
        final Class<?> studentClass = Student.class;
        final Student student = (Student) studentClass.getDeclaredConstructor().newInstance();
        final Field field = studentClass.getDeclaredField("age");

        // field에 접근 할 수 있도록 만든다.
        field.setAccessible(true);  // 설정하지 않을 시 IllegalAccessException 발생

        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        field.set(student, 99);

        assertThat(field.getInt(student)).isEqualTo(99);
        field.setAccessible(false);  // 한 번 제어를 변경한 경우 계속 상태가 유지되므로, 사용 후 복구시켜 줌
        assertThat(student.getAge()).isEqualTo(99);
    }
}
