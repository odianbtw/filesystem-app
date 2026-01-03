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
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class LocalFileSystemProvider implements FileSystemProvider {

    @Override
    public List<Path> getPaths(Path directory) {
        final var list = new ArrayList<Path>();
        try (final var stream = Files.newDirectoryStream(directory)){
            for (var path : stream) {
                list.add(path);
            }
        } catch (IOException e) {
            log.error("Error during extracting files from provided path - {}.", directory, e);
            throw new InternalErrorException("Error finding resources with provided path.");
        }
        return list;
    }

    @Override
    public boolean notExists(Path path) {
        return Files.notExists(path);
    }

    @Override
    public boolean isFile(Path path) {
        return Files.isRegularFile(path);
    }

}
