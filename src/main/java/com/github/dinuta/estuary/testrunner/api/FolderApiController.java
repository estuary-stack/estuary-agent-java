package com.github.dinuta.estuary.testrunner.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dinuta.estuary.testrunner.constants.About;
import com.github.dinuta.estuary.testrunner.constants.ApiResponseConstants;
import com.github.dinuta.estuary.testrunner.constants.ApiResponseMessage;
import com.github.dinuta.estuary.testrunner.constants.DateTimeConstants;
import com.github.dinuta.estuary.testrunner.model.api.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.zeroturnaround.zip.NameMapper;
import org.zeroturnaround.zip.ZipUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.List;

@Api(tags = {"estuary-testrunner"})
@Controller
public class FolderApiController implements FolderApi {

    private static final Logger log = LoggerFactory.getLogger(FolderApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    public FolderApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<? extends Object> folderGet(@ApiParam(value = "Target folder path to get as zip", required = false) @RequestHeader(value = "Folder-Path", required = false) String folderPath, @ApiParam(value = "") @RequestHeader(value = "Token", required = false) String token) {
        String accept = request.getHeader("Accept");
        String archiveNamePath = "results.zip";
        String headerName = "Folder-Path";
        List<String> fileContent;

        if (folderPath == null) {
            return new ResponseEntity<ApiResponse>(new ApiResponse()
                    .code(ApiResponseConstants.HTTP_HEADER_NOT_PROVIDED)
                    .message(String.format(ApiResponseMessage.getMessage(ApiResponseConstants.HTTP_HEADER_NOT_PROVIDED), headerName))
                    .description(String.format(ApiResponseMessage.getMessage(ApiResponseConstants.HTTP_HEADER_NOT_PROVIDED), headerName))
                    .name(About.getAppName())
                    .version(About.getVersion())
                    .time(LocalDateTime.now().format(DateTimeConstants.PATTERN)), HttpStatus.NOT_FOUND);
        }

        File file;
        ByteArrayResource resource;
        try {
            file = new File(archiveNamePath);
            ZipUtil.pack(new File(folderPath), file, new NameMapper() {
                public String map(String name) {
                    return name;
                }
            });

            resource = new ByteArrayResource(IOUtils.toByteArray(new FileInputStream(archiveNamePath)));
        } catch (Exception e) {
            return new ResponseEntity<ApiResponse>(new ApiResponse()
                    .code(ApiResponseConstants.FOLDER_ZIP_FAILURE)
                    .message(String.format(ApiResponseMessage.getMessage(ApiResponseConstants.FOLDER_ZIP_FAILURE), folderPath))
                    .description(ExceptionUtils.getStackTrace(e))
                    .name(About.getAppName())
                    .version(About.getVersion())
                    .time(LocalDateTime.now().format(DateTimeConstants.PATTERN)), HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(MediaType.valueOf("application/zip"))
                .contentLength(file.length())
                .body(resource);
    }

}
