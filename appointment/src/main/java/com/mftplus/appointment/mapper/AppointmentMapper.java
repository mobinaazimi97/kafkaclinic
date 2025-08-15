package com.mftplus.appointment.mapper;

import com.mftplus.appointment.dto.AppointmentDto;
import com.mftplus.appointment.model.entity.Appointment;
import com.mftplus.appointment.model.entity.Schedule;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Mapper(
        config = CentralMapperConfig.class,
        uses = {ScheduleMapper.class}
)
public interface AppointmentMapper {

    DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Mapping(source = "appointmentUuid", target = "appointmentUuid")
    @Mapping(source = "schedule.scheduleUuid", target = "scheduleId")
    @Mapping(source = "user.userUuid", target = "userUuid")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "appointmentDateTime", target = "appointmentDateTime")
    AppointmentDto toDto(Appointment entity);

    @Mapping(source = "appointmentDateTime", target = "appointmentDateTime")
    Appointment toEntity(AppointmentDto dto);

    @Mapping(source = "appointmentDateTime", target = "appointmentDateTime")
    List<AppointmentDto> toDtoList(List<Appointment> entities);

    @Mapping(source = "appointmentUuid", target = "appointmentUuid")
    @Mapping(source = "appointmentDateTime", target = "appointmentDateTime")
    void updateFromDto(AppointmentDto dto, @MappingTarget Appointment entity);

    @AfterMapping
    default void setScheduleFromScheduleId(AppointmentDto dto, @MappingTarget Appointment entity) {
        if (dto.getScheduleId() != null) {
            Schedule schedule = new Schedule();
            schedule.setScheduleUuid(dto.getScheduleId());
            entity.setSchedule(schedule);
        }
    }


    default String map(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(FORMATTER) : null;
    }

    default LocalDateTime map(String dateTime) {
        if (dateTime == null) return null;
        try {
            return LocalDateTime.parse(dateTime, FORMATTER);
        } catch (DateTimeParseException e) {

            return null;
        }
    }

}
