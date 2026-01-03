package com.example.filesystem.service;

import com.example.filesystem.model.Resource;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public interface FileSystemProvider {
    List<Path> getPaths(Path directory);
    boolean notExists(Path path);
    boolean isFile(Path path);
}
