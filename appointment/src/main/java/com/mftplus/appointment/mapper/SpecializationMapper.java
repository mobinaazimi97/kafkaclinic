package com.mftplus.appointment.mapper;

import com.mftplus.appointment.dto.SpecializationDto;
import com.mftplus.appointment.model.entity.Specialization;
import org.mapstruct.*;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface SpecializationMapper {

    @Mapping(source = "specializationUuid", target = "specializationUuid")
    SpecializationDto toDto(Specialization specialization);

    @Mapping(source = "specializationUuid", target = "specializationUuid")
    Specialization toEntity(SpecializationDto dto);

    List<SpecializationDto> toDtoList(List<Specialization> specializations);

    List<Specialization> toEntityList(List<SpecializationDto> dtos);

    @Mapping(target = "specializationId", ignore = true)
    @Mapping(target = "specializationUuid", ignore = true)
    @Mapping(target = "doctors", ignore = true)
    void updateFromDto(SpecializationDto dto, @MappingTarget Specialization entity);
}
