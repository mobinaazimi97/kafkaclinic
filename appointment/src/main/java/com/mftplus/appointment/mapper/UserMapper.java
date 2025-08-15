package com.mftplus.appointment.mapper;


import com.mftplus.appointment.dto.UserDto;
import com.mftplus.appointment.model.entity.User;
import com.mftplus.appointment.model.repository.RoleRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;

@Mapper(config = CentralMapperConfig.class, uses = {RoleRepository.class})
public interface UserMapper {

    @Mapping(target = "roles", source = "roles")
    UserDto toDto(User entity);

    User toEntity(UserDto dto);

    @Mapping(target = "roles", source = "roles")
    List<UserDto> toDtoList(List<User> users);

    @Mapping(target = "roles", source = "roles")
    Set<UserDto> toDtoSet(Set<User> users);

    void updateFromDto(UserDto dto, @MappingTarget User entity);
}
