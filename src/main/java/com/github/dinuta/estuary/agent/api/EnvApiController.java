package com.github.dinuta.estuary.agent.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dinuta.estuary.agent.constants.About;
import com.github.dinuta.estuary.agent.constants.ApiResponseConstants;
import com.github.dinuta.estuary.agent.constants.ApiResponseMessage;
import com.github.dinuta.estuary.agent.constants.DateTimeConstants;
import com.github.dinuta.estuary.agent.model.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Api(tags = {"estuary-agent"})
@Controller
public class EnvApiController implements EnvApi {

    private static final Logger log = LoggerFactory.getLogger(EnvApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    public EnvApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<ApiResponse> envEnvNameGet(@ApiParam(value = "The name of the env var to get value from", required = true) @PathVariable("env_name") String envName, @ApiParam(value = "") @RequestHeader(value = "Token", required = false) String token) {
        String accept = request.getHeader("Accept");

        return new ResponseEntity<ApiResponse>(new ApiResponse()
                .code(ApiResponseConstants.SUCCESS)
                .message(ApiResponseMessage.getMessage(ApiResponseConstants.SUCCESS))
                .description(System.getenv(envName))
                .name(About.getAppName())
                .version(About.getVersion())
                .time(LocalDateTime.now().format(DateTimeConstants.PATTERN)), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> envGet(@ApiParam(value = "") @RequestHeader(value = "Token", required = false) String token) {
        String accept = request.getHeader("Accept");

        return new ResponseEntity<ApiResponse>(new ApiResponse()
                .code(ApiResponseConstants.SUCCESS)
                .message(ApiResponseMessage.getMessage(ApiResponseConstants.SUCCESS))
                .description(System.getenv())
                .name(About.getAppName())
                .version(About.getVersion())
                .time(LocalDateTime.now().format(DateTimeConstants.PATTERN)), HttpStatus.OK);
    }
}