package com.github.estuaryoss.agent.api;

import com.github.estuaryoss.agent.model.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Api(value = "env", description = "the env API")
@RequestMapping(value = "")
public interface EnvApi {

    @ApiOperation(value = "Gets the environment variable value from the environment", nickname = "envEnvNameGet", notes = "", response = ApiResponse.class, tags = {"estuary-agent",})
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Get env var success", response = ApiResponse.class),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Get env var failure", response = ApiResponse.class)})
    @RequestMapping(value = "/env/{env_name}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    default ResponseEntity<ApiResponse> envEnvNameGet(@ApiParam(value = "The name of the env var to get value from", required = true) @PathVariable("env_name") String envName) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @ApiOperation(value = "Print all environment variables", nickname = "envGet", notes = "", response = ApiResponse.class, tags = {"estuary-agent",})
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "List of the entire environment variables", response = ApiResponse.class)})
    @RequestMapping(value = "/env",
            produces = {"application/json"},
            method = RequestMethod.GET)
    default ResponseEntity<ApiResponse> envGet() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @ApiOperation(value = "Deletes the custom defined env vars contained in the virtual environment", nickname = "envDelete", notes = "", response = ApiResponse.class, tags = {"estuary-agent",})
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Deletes the entire virtual env vars, but keeping system env vars.", response = ApiResponse.class)})
    @RequestMapping(value = "/env",
            produces = {"application/json"},
            method = RequestMethod.DELETE)
    default ResponseEntity<ApiResponse> envDelete() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @ApiOperation(value = "Set environment variables", nickname = "envPost", notes = "", response = ApiResponse.class, tags = {"estuary-agent",})
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Set environment variables success", response = ApiResponse.class),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Set environment variables failure", response = ApiResponse.class)})
    @RequestMapping(value = "/env",
            produces = {"application/json"},
            method = RequestMethod.POST)
    default ResponseEntity<ApiResponse> envPost(@ApiParam(value = "List of env vars by key-value pair in JSON format", required = true) @Valid @RequestBody String envVars) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
