package com.mftplus.patient.mapper;



import com.mftplus.patient.dto.UserDto;
import com.mftplus.patient.model.User;
import com.mftplus.patient.repository.RoleRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = CentralMapperConfig.class, uses = {RoleRepository.class})
public interface UserMapper {

    @Mapping(target = "roles", source = "roles")
    UserDto toDto(User entity);

    User toEntity(UserDto dto);

    @Mapping(target = "roles", source = "roles")
    List<UserDto> toDtoList(List<User> users);

//    @Mapping(target = "roleSet", source = "roleSet")
    List<User> toEntityList(List<UserDto> userDtos);

//    @Mapping(target = "id", ignore = true)
    void updateFromDto(UserDto dto,@MappingTarget User entity);
}
