package com.mftplus.appointment.mapper;


import com.mftplus.appointment.dto.ScheduleDto;
import com.mftplus.appointment.entity.Doctor;
import com.mftplus.appointment.entity.Schedule;
import org.mapstruct.*;

import java.util.List;
import java.util.UUID;

@Mapper(
        config = CentralMapperConfig.class,
        uses = {DoctorMapper.class, AppointmentMapper.class}
)
public interface ScheduleMapper {
    @Mapping(source = "scheduleUuid", target = "scheduleUuid")
    @Mapping(source = "doctor.doctorUuid", target = "doctorId")
    @Mapping(source = "doctor.doctorFirstname", target = "doctorFirstName")
    @Mapping(source = "doctor.doctorLastname", target = "doctorLastName")
    ScheduleDto toDto(Schedule schedule);

    @InheritInverseConfiguration
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    Schedule toEntity(ScheduleDto dto);

    List<ScheduleDto> toDtoList(List<Schedule> schedules);

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    void updateFromDto(ScheduleDto scheduleDto, @MappingTarget Schedule schedule);

    default Doctor mapDoctor(UUID doctorUuid) {
        if (doctorUuid == null) {
            return null;
        }
        Doctor doctor = new Doctor();
        doctor.setDoctorUuid(doctorUuid);
        return doctor;
    }
}
