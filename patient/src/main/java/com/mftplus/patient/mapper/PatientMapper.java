package com.mftplus.patient.mapper;

import com.mftplus.patient.dto.PatientDto;
import com.mftplus.patient.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface PatientMapper {

    @Mapping(source = "appointmentId", target = "appointmentId")
    @Mapping(source = "patientUuid", target = "patientUuid")
//    @Mapping(source = "patient.appointmentId", target = "appointmentId")
    PatientDto toDto(Patient patient);

    //    @Mapping(source = "patient.appointmentId", target = "appointmentId")
    @Mapping(source = "appointmentId", target = "appointmentId")
    @Mapping(source = "patientUuid", target = "patientUuid")
    List<PatientDto> toDtoList(List<Patient> patientList);

    @Mapping(target = "patientUuid", ignore = true)
    void updateFromDto(PatientDto dto, @MappingTarget Patient patient);

    Patient toEntity(PatientDto dto);

}
