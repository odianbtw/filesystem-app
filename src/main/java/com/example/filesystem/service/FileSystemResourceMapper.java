package com.example.filesystem.service;

import com.example.filesystem.exception.InternalErrorException;
import com.example.filesystem.model.Resource;
import com.example.filesystem.model.ResourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

@Slf4j
@Component
public class FileSystemResourceMapper {
    public Resource toResource(Path path) {
        try {
            BasicFileAttributes attrs =  Files.readAttributes(path, BasicFileAttributes.class);
            return new Resource(
                    path.toAbsolutePath().toString(),
                    path.getFileName().toString(),
                    attrs.isDirectory() ? ResourceType.DIRECTORY : ResourceType.FILE,
                    attrs.isDirectory() ? null : attrs.size(),
                    attrs.lastModifiedTime().toInstant()
            );
        } catch (IOException e) {
            log.error("Error during extracting basic file attributes for path - {}", path, e);
            throw new InternalErrorException("Error finding resources with provided path.");
        }
    }
}
