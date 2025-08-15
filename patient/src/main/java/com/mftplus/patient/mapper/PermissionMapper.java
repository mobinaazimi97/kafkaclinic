package com.mftplus.patient.mapper;



import com.mftplus.patient.dto.PermissionDto;
import com.mftplus.patient.model.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper(config = CentralMapperConfig.class)
public interface PermissionMapper {
    PermissionDto toDto(Permission entity);
    Permission toEntity(PermissionDto dto);
    
    Set<PermissionDto> toDtoSet(Set<Permission> permissions);
    Set<Permission> toEntitySet(Set<PermissionDto> permissionDtos);

    void updateFromDto(PermissionDto dto, @MappingTarget Permission entity);

}
