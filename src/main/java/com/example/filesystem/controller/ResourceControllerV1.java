package com.example.filesystem.controller;


import com.example.filesystem.api.controller.ResourcesApi;
import com.example.filesystem.api.model.GetResourcesV1;
import com.example.filesystem.api.model.SearchPathRequestV1;
import com.example.filesystem.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ResourceControllerV1 implements ResourcesApi {
    private final ResourceService resourcesService;

    @Override
    public ResponseEntity<GetResourcesV1> getResources(SearchPathRequestV1 searchPathRequestV1) {
        log.info("Started finding resources with provided path - {}.", searchPathRequestV1.getPath());
        final var res = resourcesService.getResourcesV1(searchPathRequestV1);
        log.info("Finished finding resources with provided path - {}.", searchPathRequestV1.getPath());
        return ResponseEntity.ok(res);
    }


}
