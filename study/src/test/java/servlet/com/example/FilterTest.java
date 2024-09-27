package servlet.com.example;

import static org.assertj.core.api.Assertions.assertThat;
import static servlet.com.example.KoreanServlet.인코딩;

import org.junit.jupiter.api.Test;
import support.HttpUtils;
import support.TestTomcatStarter;

class FilterTest {

    @Test
    void testFilter() {
        // 톰캣 서버 시작
        int port = 8081;
        final var tomcatStarter = new TestTomcatStarter("src/main/webapp/", port);
        tomcatStarter.start();

        final var response = HttpUtils.send("/korean", port);

        // 톰캣 서버 종료
        tomcatStarter.stop();

        assertThat(response.statusCode()).isEqualTo(200);

        // 테스트가 통과하도록 CharacterEncodingFilter 클래스를 수정해보자.
        assertThat(response.body()).isEqualTo(인코딩);
    }
}
