package com.mftplus.appointment.mapper;


import com.mftplus.appointment.dto.RoleDto;
import com.mftplus.appointment.entity.Role;
import com.mftplus.appointment.repository.PermissionRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(config = CentralMapperConfig.class, uses = {PermissionRepository.class})
public interface RoleMapper {

    RoleDto toDto(Role entity);

    Role toEntity(RoleDto dto);

    void updateFromDto(RoleDto roleDto,@MappingTarget Role role);
}
