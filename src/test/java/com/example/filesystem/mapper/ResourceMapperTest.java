package com.example.filesystem.mapper;

import com.example.filesystem.api.model.ResourceRepresentationV1;
import com.example.filesystem.model.Resource;
import com.example.filesystem.model.ResourceType;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.example.filesystem.api.model.ResourceRepresentationV1.ResourceTypeEnum.DIRECTORY;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceMapperTest {


    private final ResourceMapper subject = new ResourceMapperImpl();
    private static final Instant TIME =
            Instant.parse("2025-01-01T12:00:00Z");

    @Test
    void shouldMapResourceToResourceRepresentationV1() {
        // given
        final var resource = new Resource(
                "/path/dir",
                "dir",
                ResourceType.DIRECTORY,
                null,
                TIME
        );
        final var expected = new ResourceRepresentationV1(
                "/path/dir",
                "dir",
                DIRECTORY,
                null,
                OffsetDateTime.ofInstant(TIME, ZoneOffset.UTC)
        );
        // when
        final var actual =
                subject.toResourceRepresentationV1(resource);
        // then
        assertEquals(expected, actual);
    }
}
