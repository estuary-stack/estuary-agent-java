package com.estuary.api;

import com.estuary.api.models.ApiResponseCommandDescription;
import com.estuary.api.utils.HttpRequestUtils;
import com.estuary.constants.About;
import com.estuary.constants.ApiResponseConstants;
import com.estuary.constants.ApiResponseMessage;
import com.estuary.model.CommandDescription;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class CommandApiControllerTest {
    private final static String SERVER_PREFIX = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private HttpRequestUtils httpRequestUtils;

    @Autowired
    private TestRestTemplate restTemplate;

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "ls -lrt;README.md",
                    "ls -lrt | grep README.md;README.md",
                    "echo 1 && echo 2;1\n2",
            }
    )
    public void whenSendingCorrectCommandsThenApiReturnsZeroExitCode(String commandInfo) {
        ResponseEntity<ApiResponseCommandDescription> responseEntity = getApiResponseCommandDescriptionResponseEntity(commandInfo);

        ApiResponseCommandDescription body = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(body.getCode()).isEqualTo(ApiResponseConstants.SUCCESS);
        assertThat(body.getMessage()).isEqualTo(
                String.format(ApiResponseMessage.getMessage(ApiResponseConstants.SUCCESS)));

        this.assertSuccessCommandDescriptionFields(commandInfo, body.getDescription());

        assertThat(body.getName()).isEqualTo(About.getAppName());
        assertThat(body.getVersion()).isEqualTo(About.getVersion());
        assertThat(body.getTime()).isBefore(LocalDateTime.now());
    }

    @Test
    public void whenSendingTwoCommandsThenApiReturnsSumOfTimeExecutionInSeconds() {
        int sleep1 = 1;
        int sleep2 = 2;
        String command1 = "sleep " + sleep1;
        String command2 = "sleep " + sleep2;
        String command = command1 + "\n" + command2;
        ResponseEntity<ApiResponseCommandDescription> responseEntity =
                getApiResponseCommandDescriptionResponseEntity(command);

        ApiResponseCommandDescription body = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(body.getCode()).isEqualTo(ApiResponseConstants.SUCCESS);
        assertThat(body.getMessage()).isEqualTo(
                String.format(ApiResponseMessage.getMessage(ApiResponseConstants.SUCCESS)));
        assertThat(body.getDescription().getDuration()).isEqualTo(sleep1 + sleep2);

        assertThat(body.getDescription().getCommands().get(command1).getDuration()).isEqualTo(sleep1);
        assertThat(body.getDescription().getCommands().get(command2).getDuration()).isEqualTo(sleep2);
        assertThat(body.getName()).isEqualTo(About.getAppName());
        assertThat(body.getVersion()).isEqualTo(About.getVersion());
        assertThat(body.getTime()).isBefore(LocalDateTime.now());
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "whatever;not found",
                    "ls whateverivalid;No such file or directory",
                    "cat whenever;No such file or directory",
            }
    )
    public void whenSendingIncorrectCommandsThenApiReturnsNonZeroExitCode(String commandInfo) {
        ResponseEntity<ApiResponseCommandDescription> responseEntity = getApiResponseCommandDescriptionResponseEntity(commandInfo);

        ApiResponseCommandDescription body = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(body.getCode()).isEqualTo(ApiResponseConstants.SUCCESS);
        assertThat(body.getMessage()).isEqualTo(
                String.format(ApiResponseMessage.getMessage(ApiResponseConstants.SUCCESS)));

        this.assertFailureCommandDescriptionFields(commandInfo, body.getDescription());

        assertThat(body.getName()).isEqualTo(About.getAppName());
        assertThat(body.getVersion()).isEqualTo(About.getVersion());
        assertThat(body.getTime()).isBefore(LocalDateTime.now());
    }

    private ResponseEntity<ApiResponseCommandDescription> getApiResponseCommandDescriptionResponseEntity(String commandInfo) {
        Map<String, String> headers = new HashMap<>();

        return this.restTemplate
                .exchange(SERVER_PREFIX + port + "/command",
                        HttpMethod.POST,
                        httpRequestUtils.getRequestEntityContentTypeAppJson(commandInfo.split(";")[0], headers),
                        ApiResponseCommandDescription.class);
    }

    private void assertSuccessCommandDescriptionFields(String commandInfo, CommandDescription body) {
        String command = commandInfo.split(";")[0];
        String expected = commandInfo.split(";")[1];

        assertCommonCommonDescriptionFields(command, body);

        assertThat(body.getCommands().get(commandInfo.split(";")[0]).getDetails().getCode()).isEqualTo(0);
        assertThat(body.getCommands().get(commandInfo.split(";")[0]).getDetails().getErr()).isEqualTo("");
        assertThat(body.getCommands().get(commandInfo.split(";")[0]).getDetails().getOut()).contains(expected);
    }

    private void assertFailureCommandDescriptionFields(String commandInfo, CommandDescription body) {
        String command = commandInfo.split(";")[0];
        String expected = commandInfo.split(";")[1];

        assertCommonCommonDescriptionFields(command, body);

        assertThat(body.getCommands().get(command).getDetails().getCode()).isNotEqualTo(0);
        assertThat(body.getCommands().get(command).getDetails().getErr()).contains(expected);
        assertThat(body.getCommands().get(command).getDetails().getOut()).isEqualTo("");
    }

    private void assertCommonCommonDescriptionFields(String command, CommandDescription body) {
        assertThat(body.getFinishedat()).isBefore(LocalDateTime.now());
        assertThat(body.getStartedat()).isBefore(LocalDateTime.now());
        assertThat(body.getFinished()).isEqualTo(true);
        assertThat(body.getStarted()).isEqualTo(false);
        assertThat(body.getDuration()).isGreaterThanOrEqualTo(0);
        assertThat(body.getPid()).isGreaterThan(0);
        assertThat(body.getId()).isEqualTo("none");

        assertThat(body.getCommands().get(command).getFinishedat()).isBefore(LocalDateTime.now());
        assertThat(body.getCommands().get(command).getStartedat()).isBefore(LocalDateTime.now());
        assertThat(body.getCommands().get(command).getDuration()).isGreaterThanOrEqualTo(0);
        assertThat(body.getCommands().get(command).getStatus()).isEqualTo("finished");

        assertThat(body.getCommands().get(command).getDetails().getPid()).isGreaterThanOrEqualTo(0);
        assertThat(body.getCommands().get(command).getDetails().getArgs()[2]).isEqualTo(command);
    }
}
