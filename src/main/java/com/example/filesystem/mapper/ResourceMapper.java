package com.example.filesystem.mapper;

import com.example.filesystem.api.model.ResourceRepresentationV1;
import com.example.filesystem.model.Resource;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ResourceMapper {
    ResourceRepresentationV1 toResourceRepresentationV1(Resource resource);
    List<ResourceRepresentationV1> toResourceRepresentationListV1(List<Resource> resources);

    default OffsetDateTime toOffsetDateTime(Instant instant) {
        return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
