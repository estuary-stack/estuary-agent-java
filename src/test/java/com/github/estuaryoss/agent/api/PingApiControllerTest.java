package com.github.estuaryoss.agent.api;

import com.github.estuaryoss.agent.component.About;
import com.github.estuaryoss.agent.component.Authentication;
import com.github.estuaryoss.agent.constants.ApiResponseCode;
import com.github.estuaryoss.agent.constants.ApiResponseMessage;
import com.github.estuaryoss.agent.model.api.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static com.github.estuaryoss.agent.constants.DateTimeConstants.PATTERN;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class PingApiControllerTest {
    private final static String SERVER_PREFIX = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private About about;

    @Autowired
    private Authentication auth;

    @Test
    public void whenCallingGetThenInformationIsRetrivedOk() {
        ResponseEntity<ApiResponse> responseEntity = this.restTemplate.withBasicAuth(auth.getUser(), auth.getPassword()).getForEntity(SERVER_PREFIX + port + "/ping",
                ApiResponse.class);

        ApiResponse body = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(body.getCode()).isEqualTo(ApiResponseCode.SUCCESS.getCode());
        assertThat(body.getMessage()).isEqualTo(ApiResponseMessage.getMessage(ApiResponseCode.SUCCESS.getCode()));
        assertThat(body.getDescription()).isEqualTo("pong");
        assertThat(body.getName()).isEqualTo(about.getAppName());
        assertThat(body.getPath()).isEqualTo("/ping?");
        assertThat(body.getVersion()).isEqualTo(about.getVersion());
        assertThat(LocalDateTime.parse(body.getTimestamp(), PATTERN)).isBefore(LocalDateTime.now());
    }
}