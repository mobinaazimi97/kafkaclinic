package com.mftplus.appointment.mapper;


import com.mftplus.appointment.dto.RoleDto;
import com.mftplus.appointment.model.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Set;


@Mapper(config = CentralMapperConfig.class, uses = {PermissionMapper.class})
public interface RoleMapper {

    RoleDto toDto(Role entity);

    Set<RoleDto> toDtoSet(Set<Role> roles);

    Role toEntity(RoleDto dto);

    void updateFromDto(RoleDto roleDto, @MappingTarget Role role);
}
