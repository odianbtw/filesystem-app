package com.example.filesystem.controller;

import com.example.filesystem.api.model.GetResourcesV1;
import com.example.filesystem.api.model.ResourceRepresentationV1;
import com.example.filesystem.api.model.SearchPathRequestV1;
import com.example.filesystem.exception.BadRequestException;
import com.example.filesystem.exception.ResourceNotFoundException;
import com.example.filesystem.service.ResourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.example.filesystem.api.model.ResourceRepresentationV1.ResourceTypeEnum.DIRECTORY;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResourceControllerV1.class)
public class ResourceControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ResourceService resourceService;

    @MockitoBean
    private Clock clock;

    private static final OffsetDateTime TIME = OffsetDateTime.MIN;

    @Test
    void getResources_returnsStatusCodeOkWithResponseBody() throws Exception {
        // given
        final var requestBody = """
                {
                    "path": "/path"
                }
                """;
        final var searchPath = new SearchPathRequestV1("/path");
        final var resources = new GetResourcesV1(
                List.of(new ResourceRepresentationV1("/path/dir", "dir", DIRECTORY, null, TIME))
        );
        when(resourceService.getResourcesV1(searchPath)).thenReturn(resources);
        // when/then
        mockMvc.perform(
            post("/v1/resources")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(requestBody)
        )
        .andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON_VALUE),
                jsonPath("$.resources[0].absolutePath", equalTo(resources.getResources().get(0).getAbsolutePath())),
                jsonPath("$.resources[0].name", equalTo(resources.getResources().get(0).getName())),
                jsonPath("$.resources[0].size", equalTo(resources.getResources().get(0).getSize())),
                jsonPath("$.resources[0].lastModified", equalTo("-999999999-01-01T00:00:00+18:00"))
        );
    }

    @Test
    void getResources_returnsStatusCodeNotFound() throws Exception {
        // given
        final var requestBody = """
                {
                    "path": "/void"
                }
                """;
        final var searchPath = new SearchPathRequestV1("/void");
        when(resourceService.getResourcesV1(searchPath)).thenThrow(new ResourceNotFoundException("Couldn't find resources in provided path"));
        Instant fixedInstant = Instant.parse("2025-12-31T12:00:00Z");
        when(clock.instant()).thenReturn(fixedInstant);
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);
        // when/then
        mockMvc.perform(
            post("/v1/resources")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(requestBody)
        )
        .andExpectAll(
                status().isNotFound(),
                content().contentType(MediaType.APPLICATION_JSON_VALUE),
                jsonPath("$.statusCode", equalTo(404)),
                jsonPath("$.message", equalTo("Couldn't find resources in provided path")),
                jsonPath("$.timestamp", equalTo("2025-12-31T12:00:00Z"))
        );
    }

    @Test
    void getResources_returnsStatusCodeBadRequest() throws Exception {
        // given
        final var requestBody = """
                {
                    "path": "/path/file.txt"
                }
                """;
        final var searchPath = new SearchPathRequestV1("/path/file.txt");
        when(resourceService.getResourcesV1(searchPath)).thenThrow(new BadRequestException("Provided path is file, but expected directory"));
        Instant fixedInstant = Instant.parse("2025-12-31T12:00:00Z");
        when(clock.instant()).thenReturn(fixedInstant);
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);
        // when/then
        mockMvc.perform(
            post("/v1/resources")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(requestBody)
        )
        .andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON_VALUE),
                jsonPath("$.statusCode", equalTo(400)),
                jsonPath("$.message", equalTo("Provided path is file, but expected directory")),
                jsonPath("$.timestamp", equalTo("2025-12-31T12:00:00Z"))
        );
    }
}
