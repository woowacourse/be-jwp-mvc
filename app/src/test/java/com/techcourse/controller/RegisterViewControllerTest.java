package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegisterViewControllerTest extends UsingTomcatTest {

    @DisplayName("요청시 200 상태코드 와 함께 회원가입 페이지를 담아 응답한다.")
    @Test
    void save() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            //given
            final HttpPost httpPost = new HttpPost(tomcatUrl + "/register/view");

            //when
            final HttpResponse response = httpClient.execute(httpPost);

            //then
            final int actualStatusCode = response.getStatusLine().getStatusCode();
            final Header contentTypeHeader = response.getFirstHeader("Content-Type");
            assertThat(actualStatusCode).isEqualTo(200);
            assertThat(contentTypeHeader.getValue()).isEqualTo("text/html;charset=UTF-8");
        } catch (Exception e) {
            Assertions.fail();
            e.printStackTrace();
        }
    }
}
