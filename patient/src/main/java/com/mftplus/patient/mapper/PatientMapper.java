package com.mftplus.patient.mapper;

import com.mftplus.patient.dto.PatientDto;
import com.mftplus.patient.model.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface PatientMapper {

    @Mapping(source = "appointmentUuid", target = "appointmentUuid")
    @Mapping(source = "patientUuid", target = "patientUuid")
    @Mapping(source = "patient.user.userUuid", target = "userUuid")
    @Mapping(source = "patient.user.username", target = "username")
    PatientDto toDto(Patient patient);

    @Mapping(source = "appointmentUuid", target = "appointmentUuid")
    @Mapping(source = "patientUuid", target = "patientUuid")
    List<PatientDto> toDtoList(List<Patient> patientList);

    @Mapping(target = "patientUuid", ignore = true)
    void updateFromDto(PatientDto dto, @MappingTarget Patient patient);

    Patient toEntity(PatientDto dto);

}
