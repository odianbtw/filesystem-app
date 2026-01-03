package com.example.filesystem.service;


import com.example.filesystem.api.model.GetResourcesV1;
import com.example.filesystem.api.model.SearchPathRequestV1;
import com.example.filesystem.mapper.ResourceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;

    public GetResourcesV1 getResourcesV1(SearchPathRequestV1 searchPathRequestV1) {
        final var resources = resourceRepository.getResources(searchPathRequestV1.getPath());
        return new GetResourcesV1(
                resourceMapper.toResourceRepresentationListV1(resources)
        );
    }

}
