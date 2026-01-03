package com.example.filesystem.service;

import com.example.filesystem.exception.BadRequestException;
import com.example.filesystem.exception.ResourceNotFoundException;
import com.example.filesystem.model.Resource;
import com.example.filesystem.model.ResourceType;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DefaultResourceRepositoryTest {

    private final FileSystemProvider fileSystemProvider = mock(FileSystemProvider.class);
    private final FileSystemResourceMapper resourceMapper = mock(FileSystemResourceMapper.class);
    private final DefaultResourceRepository subject = new DefaultResourceRepository(fileSystemProvider, resourceMapper);
    private static final Instant TIME = Instant.MIN;

    @Test
    void getResources_returnsResourcesWhenDirectoryExists(){
        // given
        final var path = Path.of("/path").toAbsolutePath().normalize();
        final var expectedPath = Path.of("/path/dir");
        final var expectedResource = new Resource("/path/dir", "dir", ResourceType.DIRECTORY, null, TIME);
        final var expected = List.of(expectedResource);
        when(fileSystemProvider.notExists(path)).thenReturn(false);
        when(fileSystemProvider.isFile(path)).thenReturn(false);
        when(fileSystemProvider.getPaths(path)).thenReturn(
                List.of(expectedPath)
        );
        when(resourceMapper.toResource(expectedPath)).thenReturn(
                expectedResource
        );
        // when
        final var actual = subject.getResources("/path");
        // then
        assertEquals(expected, actual);
        verify(fileSystemProvider).notExists(path);
        verify(fileSystemProvider).isFile(path);
        verify(fileSystemProvider).getPaths(path);
        verify(resourceMapper).toResource(expectedPath);
    }

    @Test
    void getResources_throwsExceptionWhenDirectoryNotExists(){
        // given
        final var path = Path.of("/path").toAbsolutePath().normalize();
        when(fileSystemProvider.notExists(path)).thenReturn(true);
        // when/then
        assertThrows(ResourceNotFoundException.class, () -> subject.getResources(path.toString()));
        verify(fileSystemProvider).notExists(path);
    }

    @Test
    void getResources_throwsExceptionWhenResourceIsFile(){
        // given
        final var path = Path.of("/path").toAbsolutePath().normalize();
        when(fileSystemProvider.notExists(path)).thenReturn(false);
        when(fileSystemProvider.isFile(path)).thenReturn(true);
        // when/then
        assertThrows(BadRequestException.class, () -> subject.getResources(path.toString()));
        verify(fileSystemProvider).notExists(path);
        verify(fileSystemProvider).isFile(path);
    }

}
