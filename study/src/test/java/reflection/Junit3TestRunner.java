package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Junit3Test junit3Test = new Junit3Test();
        Method[] methods = clazz.getMethods();

        List<Method> methodsStartWithTest = Arrays.stream(methods)
                .filter(method -> method.getName().startsWith("test"))
                .collect(Collectors.toList());

        for (Method method : methodsStartWithTest) {
            method.invoke(junit3Test);
        }
    }
}
