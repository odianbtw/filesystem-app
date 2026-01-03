package com.example.filesystem.model;

import java.time.Instant;

public record Resource(
        String absolutePath,
        String name,
        ResourceType resourceType,
        Long size,
        Instant lastModified
) {
}
