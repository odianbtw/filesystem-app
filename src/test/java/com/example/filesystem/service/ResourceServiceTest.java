package com.example.filesystem.service;


import com.example.filesystem.api.model.GetResourcesV1;
import com.example.filesystem.api.model.ResourceRepresentationV1;
import com.example.filesystem.api.model.SearchPathRequestV1;
import com.example.filesystem.mapper.ResourceMapper;
import com.example.filesystem.model.Resource;
import com.example.filesystem.model.ResourceType;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.example.filesystem.api.model.ResourceRepresentationV1.ResourceTypeEnum.DIRECTORY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ResourceServiceTest {

    private final ResourceRepository resourceRepository = mock(ResourceRepository.class);
    private final ResourceMapper resourceMapper = mock(ResourceMapper.class);
    private final ResourceService subject = new ResourceService(resourceRepository, resourceMapper);
    private static final OffsetDateTime TIME = OffsetDateTime.of(
        2025, 12, 31, 12, 0, 0, 0, ZoneOffset.UTC
    );


    @Test
    void testGetResourcesV1_returnsMappedResources() {
        // given
        final var path = new SearchPathRequestV1("/path");
        final var expectedList = List.of(
                new ResourceRepresentationV1("/path/dir", "dir", DIRECTORY, null, TIME)
        );
        final var expected = new GetResourcesV1(expectedList);
        final var list =  List.of(new Resource("/path/dir", "dir", ResourceType.DIRECTORY, null, Instant.from(TIME)));
        when(resourceRepository.getResources(path.getPath())).thenReturn(list);
        when(resourceMapper.toResourceRepresentationListV1(list)).thenReturn(expectedList);
        // when
        final var actual = subject.getResourcesV1(path);
        // then
        assertEquals(expected, actual);
        verify(resourceRepository).getResources(path.getPath());
        verify(resourceMapper).toResourceRepresentationListV1(list);
    }

}
