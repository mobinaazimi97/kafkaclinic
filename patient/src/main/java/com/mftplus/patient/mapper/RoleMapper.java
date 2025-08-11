package com.mftplus.patient.mapper;



import com.mftplus.patient.dto.RoleDto;
import com.mftplus.patient.model.Role;
import com.mftplus.patient.repository.PermissionRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper(config = CentralMapperConfig.class, uses = {PermissionRepository.class})
public interface RoleMapper {

    RoleDto toDto(Role entity);

    Role toEntity(RoleDto dto);

    Set<RoleDto> toDtoSet(Set<Role> roles);
    Set<Role> toEntitySet(Set<RoleDto> roleDtos);

    void updateFromDto(RoleDto roleDto,@MappingTarget Role role);
}
