package com.estuary.api;

import com.estuary.api.models.ApiResponseString;
import com.estuary.api.utils.HttpRequestUtils;
import com.estuary.constants.About;
import com.estuary.constants.ApiResponseConstants;
import com.estuary.constants.ApiResponseMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.estuary.api.constants.HeaderConstants.X_REQUEST_ID;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class XRequestIdTest {
    private final static String SERVER_PREFIX = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private HttpRequestUtils httpRequestUtils;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void whenCallingWithXIdSetFromClientThenTheServersReturnsItBack() {
        String xReqId = "my-provided-value";
        Map<String, String> headers = new HashMap<>();
        headers.put(X_REQUEST_ID, xReqId);

        ResponseEntity<ApiResponseString> responseEntity =
                this.restTemplate
                        .exchange(SERVER_PREFIX + port + "/about",
                                HttpMethod.GET,
                                httpRequestUtils.getRequestEntityContentTypeAppJson(null, headers),
                                ApiResponseString.class);

        ApiResponseString body = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseEntity.getHeaders().get(X_REQUEST_ID).get(0)).isEqualTo(xReqId); // <--
        assertThat(body.getCode()).isEqualTo(ApiResponseConstants.SUCCESS);
        assertThat(body.getMessage()).isEqualTo(ApiResponseMessage.getMessage(ApiResponseConstants.SUCCESS));
        assertThat(body.getDescription()).isEqualTo(About.getAppName());
        assertThat(body.getName()).isEqualTo(About.getAppName());
        assertThat(body.getVersion()).isEqualTo(About.getVersion());
        assertThat(body.getTime()).isBefore(LocalDateTime.now());
    }

    @Test
    public void whenCallingWithXIdNotSetFromClientThenTheServersReturnsANewUUIDOne() {
        Map<String, String> headers = new HashMap<>();

        ResponseEntity<ApiResponseString> responseEntity =
                this.restTemplate
                        .exchange(SERVER_PREFIX + port + "/about",
                                HttpMethod.GET,
                                httpRequestUtils.getRequestEntityContentTypeAppJson(null, headers),
                                ApiResponseString.class);

        ApiResponseString body = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseEntity.getHeaders().get(X_REQUEST_ID).get(0)).isInstanceOf(String.class); // <--
        assertThat(responseEntity.getHeaders().get(X_REQUEST_ID).get(0).length()).isEqualTo(36); // <--
        assertThat(body.getCode()).isEqualTo(ApiResponseConstants.SUCCESS);
        assertThat(body.getMessage()).isEqualTo(ApiResponseMessage.getMessage(ApiResponseConstants.SUCCESS));
        assertThat(body.getDescription()).isEqualTo(About.getAppName());
        assertThat(body.getName()).isEqualTo(About.getAppName());
        assertThat(body.getVersion()).isEqualTo(About.getVersion());
        assertThat(body.getTime()).isBefore(LocalDateTime.now());
    }

}
