package com.mftplus.appointment.mapper;

import com.mftplus.appointment.dto.AppointmentDto;
import com.mftplus.appointment.entity.Appointment;
import com.mftplus.appointment.entity.Schedule;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        config = CentralMapperConfig.class,
        uses = {ScheduleMapper.class}
)
public interface AppointmentMapper {


    @Mapping(source = "appointmentUuid", target = "appointmentUuid")
    @Mapping(source = "schedule.scheduleUuid", target = "scheduleId")
    AppointmentDto toDto(Appointment entity);

    Appointment toEntity(AppointmentDto dto);

    List<AppointmentDto> toDtoList(List<Appointment> entities);

    @Mapping(source = "appointmentUuid", target = "appointmentUuid")
    void updateFromDto(AppointmentDto dto, @MappingTarget Appointment entity);

    @AfterMapping
    default void setScheduleFromScheduleId(AppointmentDto dto, @MappingTarget Appointment entity) {
        if (dto.getScheduleId() != null) {
            Schedule schedule = new Schedule();
            schedule.setScheduleUuid(dto.getScheduleId());
            entity.setSchedule(schedule);
        }
    }
}
