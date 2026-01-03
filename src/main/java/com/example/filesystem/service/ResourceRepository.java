package com.example.filesystem.service;

import com.example.filesystem.model.Resource;

import java.util.List;

public interface ResourceRepository {
    List<Resource> getResources(String path);
}
