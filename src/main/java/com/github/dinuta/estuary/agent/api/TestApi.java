package com.github.dinuta.estuary.agent.api;

import com.github.dinuta.estuary.agent.model.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "test", description = "the test API")
@RequestMapping(value = "")
public interface TestApi {

    @ApiOperation(value = "Stops all shell commands previously started", nickname = "testDelete", notes = "", response = ApiResponse.class, tags = {"estuary-agent",})
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "test stop success", response = ApiResponse.class),
            @io.swagger.annotations.ApiResponse(code = 404, message = "test stop failure", response = ApiResponse.class)})
    @RequestMapping(value = "/test",
            produces = {"application/json"},
            method = RequestMethod.DELETE)
    default ResponseEntity<ApiResponse> testDelete(@ApiParam(value = "") @RequestHeader(value = "Token", required = false) String token) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @ApiOperation(value = "Gets information about running shell commands, running processes, test status", nickname = "testGet", notes = "", response = ApiResponse.class, tags = {"estuary-agent",})
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Get test info success", response = ApiResponse.class),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Get test info failure", response = ApiResponse.class)})
    @RequestMapping(value = "/test",
            produces = {"application/json"},
            method = RequestMethod.GET)
    default ResponseEntity<ApiResponse> testGet(@ApiParam(value = "") @RequestHeader(value = "Token", required = false) String token) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @ApiOperation(value = "Starts the shell commands in detached mode and sequentially", nickname = "testIdPost", notes = "", response = ApiResponse.class, tags = {"estuary-agent",})
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "commands start success", response = ApiResponse.class),
            @io.swagger.annotations.ApiResponse(code = 404, message = "commands start failure", response = ApiResponse.class)})
    @RequestMapping(value = "/test/{id}",
            produces = {"application/json"},
            consumes = {"text/plain"},
            method = RequestMethod.POST)
    default ResponseEntity<ApiResponse> testIdPost(@ApiParam(value = "Test id set by the user", required = true) @PathVariable("id") String id, @ApiParam(value = "List of commands to run one after the other. E.g. make/mvn/sh/npm", required = true) @Valid @RequestBody String testFileContent, @ApiParam(value = "") @RequestHeader(value = "Token", required = false) String token) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
