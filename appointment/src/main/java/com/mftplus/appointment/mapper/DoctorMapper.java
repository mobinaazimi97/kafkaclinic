package com.mftplus.appointment.mapper;

import com.mftplus.appointment.dto.DoctorDto;
import com.mftplus.appointment.model.entity.Doctor;

import org.mapstruct.*;

import java.util.List;

@Mapper(
        config = CentralMapperConfig.class,
        uses = {SpecializationMapper.class, ScheduleMapper.class}
)
public interface DoctorMapper {

    DoctorDto toDto(Doctor doctor);

    Doctor toEntity(DoctorDto dto);

    List<DoctorDto> toDtoList(List<Doctor> doctors);


    @Mapping(target = "doctorId", ignore = true)
    @Mapping(target = "doctorUuid", ignore = true)
    @Mapping(target = "specializations", ignore = true) // بهتر مدیریت شود جداگانه اگر لازم است
    @Mapping(target = "schedules", ignore = true)
    void updateFromDto(DoctorDto dto, @MappingTarget Doctor entity);
}
