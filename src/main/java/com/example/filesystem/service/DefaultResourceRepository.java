package com.example.filesystem.service;

import com.example.filesystem.exception.BadRequestException;
import com.example.filesystem.exception.ResourceNotFoundException;
import com.example.filesystem.model.Resource;
import com.example.filesystem.model.ResourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Component
public class DefaultResourceRepository implements ResourceRepository {

    private final FileSystemProvider fileSystemProvider;
    private final FileSystemResourceMapper resourceMapper;

    @Override
    public List<Resource> getResources(String path) {
        final var directory = Path.of(path).toAbsolutePath().normalize();
        if (fileSystemProvider.notExists(directory))
            throw new ResourceNotFoundException(String.format("Directory with path - %s not found.", path));
        if (fileSystemProvider.isFile(directory))
            throw new BadRequestException(String.format("Path %s - is file, expected directory", path));
        return fileSystemProvider.getPaths(directory)
                .stream()
                .map(resourceMapper::toResource)
                .toList();
    }

}
