package com.mftplus.appointment.mapper;


import com.mftplus.appointment.dto.PermissionDto;
import com.mftplus.appointment.model.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = CentralMapperConfig.class)
public interface PermissionMapper {
    PermissionDto toDto(Permission entity);
    Permission toEntity(PermissionDto dto);

    void updateFromDto(PermissionDto dto, @MappingTarget Permission entity);

}
