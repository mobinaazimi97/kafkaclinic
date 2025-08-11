package com.mftplus.appointment;

import com.mftplus.appointment.dto.*;
import com.mftplus.appointment.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.time.LocalDateTime;
import java.util.*;

@EnableFeignClients
@SpringBootApplication
@Slf4j
public class AppointmentApplication implements CommandLineRunner {
    private final AppointmentService appointmentService;
    private final ScheduleService scheduleService;
    private final DoctorService doctorService;
    private final SpecializationService specializationService;
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final UserService userService;

    public AppointmentApplication(AppointmentService appointmentService, ScheduleService scheduleService, DoctorService doctorService, SpecializationService specializationService, PermissionService permissionService, RoleService roleService, UserService userService) {
        this.appointmentService = appointmentService;
        this.scheduleService = scheduleService;
        this.doctorService = doctorService;
        this.specializationService = specializationService;
        this.permissionService = permissionService;
        this.roleService = roleService;
        this.userService = userService;
    }


    public static void main(String[] args) {
        SpringApplication.run(AppointmentApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //Spec :

//        System.out.println(specializationService.findAll());
        SpecializationDto specializationDto = SpecializationDto.builder()
                .skillName("heart")
                .description("heart spec")
                .deleted(false)
                .build();
        SpecializationDto saved = specializationService.save(specializationDto);
        System.out.println("saved spec :" + saved);
//        System.out.println("found skill name on spec tbl : " + specializationService.findBySkillName("heart"));
//        System.out.println("Found Doctor Name In Spec : " + specializationService.findByDoctorNameAndFamily("mobina", "azimi"));
//        specializationService.logicalRemove(1L);
//        System.out.println("saved spec :" + saved);
//        System.out.println(" SPEC found ID :" + specializationService.findById(1L));

        DoctorDto doctorDto = DoctorDto.builder()
                .doctorFirstname("mobina")
                .doctorLastname("azimi")
                .experienceYears(5)
                .specializations(List.of(saved)) // specialization قبلاً save شده و ID داره
                .build();
        DoctorDto savedDoctor = doctorService.save(doctorDto); // جدول join به‌درستی پر میشه
        System.out.println("doctor saved : " + savedDoctor);
        //----------------------------------------------------------------------------------------------
        //Doctor : ->Wrong Way!!
//        DoctorDto doctorDto = DoctorDto.builder()
//                .doctorFirstname("john")
//                .doctorLastname("alex")
//                .specializations(new ArrayList<>())
//                .deleted(false)
//                .build();
//        DoctorDto savedDoctor = doctorService.save(doctorDto);
//        SpecializationDto specialization1 = specializationService.findById(saved.getSpecializationUuid());
//        savedDoctor.getSpecializations().add(specialization1);
//        DoctorDto finalDoctor = doctorService.save(savedDoctor);
//        System.out.println("Final doctor :" + finalDoctor);
//        System.out.println("doctor name found :"+doctorService.findByDoctorName("mobina","azimi"));

//        SpecializationDto specializationDto = SpecializationDto.builder()
//                .skillName("heart")
//                .description("heart spec")
//                .deleted(false)
//                .build();
//        SpecializationDto saved = specializationService.save(specializationDto);

// doctor ro misazim
//        DoctorDto doctorDto = DoctorDto.builder()
//                .doctorFirstname("mobina")
//                .doctorLastname("azimi")
//                .specializations(new ArrayList<>())
//                .deleted(false)
//                .build();
//
//        DoctorDto savedDoctor = doctorService.save(doctorDto);
//
//// specialization ro miyarim ta be doctor ezafe konim
//        SpecializationDto specialization1 = specializationService.findById(saved.getSpecializationId());
//
//// ✅ set doctorId va esm va famil baraye specialization
//        specialization1.setDoctorId(savedDoctor.getDoctorId());
//        specialization1.setDoctorFirstName(savedDoctor.getDoctorFirstname());
//        specialization1.setDoctorLastName(savedDoctor.getDoctorLastname());
//        SpecializationDto savedSpecializationDto1 = specializationService.save(specialization1);
//        System.out.println("SAVED : "+savedSpecializationDto1);
//
//// ✅ doctor -> specialization
//        savedDoctor.getSpecializations().add(savedSpecializationDto1);
//
//// ❗️optional: agar mapstruct to ro taghir dadi ke specialization -> doctors ro ham bezare, mituni in ro ham ezafe koni:
////        specializationService.addDoctorToSpecialization(specialization1.getSpecializationId(), savedDoctor.getDoctorId());
//
//// doctor ro save mikonim
//        DoctorDto finalDoctor = doctorService.save(savedDoctor);
//        System.out.println("Final doctor :" + finalDoctor);
//        System.out.println("Found Doctor fullName on SPEC : "+specializationService.findByDoctorNameAndFamily(savedDoctor.getDoctorFirstname(), savedDoctor.getDoctorLastname()));

        //----------------------------------------------------------------------------------------------
        //Schedule :
        ScheduleDto scheduleDto = ScheduleDto.builder()
                .startDateTime(LocalDateTime.of(2025, 8, 25, 12, 30))
                .endDateTime(LocalDateTime.of(2025, 8, 26, 12, 30))
                .appointmentDurationMin(30)
                .doctorId(savedDoctor.getDoctorUuid())
                .isBooked(false)
                .build();
        ScheduleDto savedSchedule = scheduleService.createSchedulesForDoctor(savedDoctor.getDoctorUuid(), scheduleDto.getStartDateTime(), scheduleDto.getEndDateTime(), 30).get(0);
        System.out.println("Saved Schedule For Doctor :" + savedSchedule);

//        System.out.println(scheduleService.getById(savedSchedule.getScheduleUuid()));
//
//        List<ScheduleDto> availableSchedulesBySpecialization = scheduleService.findAvailableSchedulesBySpecialization(
//                saved.getSpecializationUuid()
//        );
//        System.out.println("Found free times in this spec req : " + availableSchedulesBySpecialization);
        //----------------------------------------------------------------------------------------------
        //Appointment :
        AppointmentDto appointment1 =
                AppointmentDto.builder()
                        .appointmentDateTime(LocalDateTime.of(2025, 8, 25, 12, 30))  // زمان نوبت
                        .scheduleId(savedSchedule.getScheduleUuid())
                        .patientId(UUID.fromString("ad903218-bd67-4e50-800e-43de9f842a3d"))
                        .build();
        AppointmentDto savedAppointment1 = appointmentService.create(appointment1);

        System.out.println("Saved Appointment :" + savedAppointment1);

//        AppointmentDto appointment2 =
//                AppointmentDto.builder()
//                        .appointmentDateTime(LocalDateTime.of(2025, 8, 25, 13, 30))  // زمان نوبت
//                        .scheduleId(2L)
//                        .patientId(2L)
//                        .build();
//        AppointmentDto savedAppointment2 = appointmentService.create(appointment2);
//        System.out.println("Saved Appointment 2 :" + savedAppointment2);
//
//        List<ScheduleDto> availableSchedulesBySpecialization2 = scheduleService.findAvailableSchedulesBySpecialization(
//                1L
//        );
//        System.out.println("Found free times in this spec req 2 : " + availableSchedulesBySpecialization2);
//        System.out.println("Found Schedule In Appointment :" + appointmentService.findAvailableSchedulesBySpecializationInAppointment(1L));
////        System.out.println("Found Specializations In Appointment :"+appointmentService.findSpecializations());
////
//        System.out.println("Found Specializations In Schedule :" + scheduleService.findAvailableSchedulesBySpecialization(1L));


        //------------------------------------------------------------------------------------------------------------------------------
        PermissionDto permissionDto = PermissionDto.builder().permissionName("ACCESS-ALL").build();
        PermissionDto savedPermission = permissionService.save(permissionDto);
        System.out.println("Permission saved :"+savedPermission);

        RoleDto roleDto = RoleDto.builder().roleName("ROLE_ADMIN").permissions(Set.of(savedPermission)).build();
        RoleDto savedRole = roleService.save(roleDto);
        System.out.println("Role Saved :"+savedRole);

        UserDto userDto = UserDto.builder()
                .username("mobi")
                .password("m32145")
                .roles(Set.of(savedRole))
                .build();
        UserDto userSaved = userService.save(userDto);
        System.out.println("User saved :"+userSaved);

//
//-----------------------------todo
//        log.info("Testing findAvailableSchedulesBySpecializationAndDoctor");
//        List<ScheduleDto> availableSchedulesBySpecialization = scheduleService.findAvailableSchedulesBySpecializationAndDoctor(
//                saved.getSpecializationId(),
//                null, // بدون doctorId
//                LocalDateTime.of(2025, 7, 22, 0, 0), // بازه زمانی گسترده‌تر
//                LocalDateTime.of(2025, 7, 23, 23, 59)
//        );
//        log.info("Found {} available schedules for specializationId "+
//                availableSchedulesBySpecialization,
//                saved.getSpecializationId());
//        availableSchedulesBySpecialization.forEach(schedule ->
//                log.info("Schedule id={}, startTime={}, isBooked={}, doctor={} {}",
//                        schedule.getScheduleId(),
//                        schedule.getStartDateTime(),
//                        schedule.isBooked(),
//                        schedule.getDoctor() != null ? schedule.getDoctor().getDoctorFirstname() : "null",
//                        schedule.getDoctor() != null ? schedule.getDoctor().getDoctorLastname() : "null")
//        );


//        log.info("Found {} available schedules for specializationId={}", availableSchedulesBySpecialization.size(), saved.getSpecializationId());
//        availableSchedulesBySpecialization.forEach(schedule ->
//                log.info("Schedule id={}, startTime={}, doctor={} {}",
//                        schedule.getScheduleId(),
//                        schedule.getStartDateTime(),
//                        schedule.getDoctor() != null ? schedule.getDoctor().getDoctorFirstname() : "null",
//                        schedule.getDoctor() != null ? schedule.getDoctor().getDoctorLastname() : "null")
//        );


//        AppointmentDto appointment2 =
//                AppointmentDto.builder()
//                        .appointmentDateTime(LocalDateTime.of(2025, 1, 11, 12, 12))
////                        .endDateTime(LocalDateTime.of(2025, 1, 11, 12, 12))
//                        .patientId(2L)
////                        .schedule(savedSchedule)
//                        .build();
//        System.out.println(appointment2);
//        appointmentService.create(appointment1);
//        appointmentService.create(appointment2);
//        System.out.println("Saved Appointment :" + appointment1);
//        System.out.println("Saved Appointment :" + appointment2);
//
//
////        System.out.println("doctors found for appointment :"+appointmentService.findDoctorFullName(savedDoctor.getDoctorFirstname(), savedDoctor.getDoctorLastname()));
//        System.out.println("All Appointments :"+appointmentService.getAll());
//        ------------------------------------------------------------
//                System.out.println("FIND ALL : " + appointmentService.getAll());
//        AppointmentDto appointmentDto = AppointmentDto.builder()
//                .patientId(1L)
//                .appointmentDateTime(LocalDateTime.of(2025, 5, 20, 14, 0))
//                .notes("Checkup")
//                .build();
//
//        AppointmentDto savedAppointment = appointmentService.create(appointmentDto);
//        System.out.println("Saved appointment: " + savedAppointment);


        // 1. ساخت تخصص
//        SpecializationDto specializationDto = SpecializationDto.builder()
//                .skillName("heart")
//                .description("Cardiology specialization")
//                .build();
//        SpecializationDto savedSpecialization = specializationService.save(specializationDto);
//        log.info("Saved Specialization: id={}, skillName={}", savedSpecialization.getSpecializationId(), savedSpecialization.getSkillName());
//
//        // 2. ساخت پزشک
//        DoctorDto doctorDto = DoctorDto.builder()
//                .doctorFirstname("mobina")
//                .doctorLastname("azimi")
//                .specializations(List.of(savedSpecialization))
//                .build();
//        DoctorDto savedDoctor = doctorService.save(doctorDto);
//        log.info("Saved Doctor: id={}, name={} {}", savedDoctor.getDoctorId(), savedDoctor.getDoctorFirstname(), savedDoctor.getDoctorLastname());
//
//        // 3. ساخت برنامه‌های آزاد
//        ScheduleDto scheduleDto1 = ScheduleDto.builder()
//                .startDateTime(LocalDateTime.of(2025, 7, 18, 12, 0))
//                .endDateTime(LocalDateTime.of(2025, 7, 18, 13, 0))
//                .doctor(savedDoctor)
//                .appointmentDurationMin(30)
//                .isBooked(false)
//                .build();
//        ScheduleDto savedSchedule1 = scheduleService.save(scheduleDto1);
//        log.info("Saved Schedule 1: id={}, startTime={}", savedSchedule1.getScheduleId(), savedSchedule1.getStartDateTime());
//
//        ScheduleDto scheduleDto2 = ScheduleDto.builder()
//                .startDateTime(LocalDateTime.of(2025, 7, 18, 14, 0))
//                .endDateTime(LocalDateTime.of(2025, 7, 18, 15, 0))
//                .doctor(savedDoctor)
//                .appointmentDurationMin(30)
//                .isBooked(false)
//                .build();
//        ScheduleDto savedSchedule2 = scheduleService.save(scheduleDto2);
//        log.info("Saved Schedule 2: id={}, startTime={}", savedSchedule2.getScheduleId(), savedSchedule2.getStartDateTime());
//
//        // 4. تست متد findAvailableSchedulesBySpecializationAndDoctor
//        // سناریو ۱: پیدا کردن برنامه‌ها فقط با specializationId
//        log.info("Testing findAvailableSchedulesBySpecializationAndDoctor with only specializationId");
//        List<ScheduleDto> availableSchedulesBySpecialization = scheduleService.findAvailableSchedulesBySpecializationAndDoctor(
//                savedSpecialization.getSpecializationId(),
//                null, // بدون doctorId
//                LocalDateTime.of(2025, 7, 18, 0, 0),
//                LocalDateTime.of(2025, 7, 18, 23, 59)
//        );
//        log.info("Found {} available schedules for specializationId={}", availableSchedulesBySpecialization.size(), savedSpecialization.getSpecializationId());
//        availableSchedulesBySpecialization.forEach(schedule ->
//                log.info("Schedule id={}, startTime={}, doctor={} {}",
//                        schedule.getScheduleId(),
//                        schedule.getStartDateTime(),
//                        schedule.getDoctor() != null ? schedule.getDoctor().getDoctorFirstname() : "null",
//                        schedule.getDoctor() != null ? schedule.getDoctor().getDoctorLastname() : "null")
//        );
//
//        // سناریو ۲: پیدا کردن برنامه‌ها با specializationId و doctorId
//        log.info("Testing findAvailableSchedulesBySpecializationAndDoctor with specializationId and doctorId");
//        List<ScheduleDto> availableSchedulesByDoctor = scheduleService.findAvailableSchedulesBySpecializationAndDoctor(
//                savedSpecialization.getSpecializationId(),
//                savedDoctor.getDoctorId(),
//                LocalDateTime.of(2025, 7, 18, 0, 0),
//                LocalDateTime.of(2025, 7, 18, 23, 59)
//        );
//        log.info("Found {} available schedules for specializationId={} and doctorId={}",
//                availableSchedulesByDoctor.size(),
//                savedSpecialization.getSpecializationId(),
//                savedDoctor.getDoctorId());
//        availableSchedulesByDoctor.forEach(schedule ->
//                log.info("Schedule id={}, startTime={}, doctor={} {}",
//                        schedule.getScheduleId(),
//                        schedule.getStartDateTime(),
//                        schedule.getDoctor() != null ? schedule.getDoctor().getDoctorFirstname() : "null",
//                        schedule.getDoctor() != null ? schedule.getDoctor().getDoctorLastname() : "null")
//        );
//
//        // 5. ساخت نوبت برای تست isBooked
//        AppointmentDto appointment1 = AppointmentDto.builder()
//                .appointmentDateTime(LocalDateTime.of(2025, 7, 18, 12, 30))
//                .patientId(1L)
//                .schedule(savedSchedule1)
//                .build();
//        appointmentService.create(appointment1);
//        log.info("Saved Appointment 1: id={}, patientId={}", appointment1.getId(), appointment1.getPatientId());
//
//        // 6. تست دوباره برای اطمینان از فیلتر isBooked
//        log.info("Testing findAvailableSchedulesBySpecializationAndDoctor after booking an appointment");
//        List<ScheduleDto> availableSchedulesAfterBooking = scheduleService.findAvailableSchedulesBySpecializationAndDoctor(
//                savedSpecialization.getSpecializationId(),
//                savedDoctor.getDoctorId(),
//                LocalDateTime.of(2025, 7, 18, 0, 0),
//                LocalDateTime.of(2025, 7, 18, 23, 59)
//        );
//        log.info("Found {} available schedules after booking", availableSchedulesAfterBooking.size());
//        availableSchedulesAfterBooking.forEach(schedule ->
//                log.info("Schedule id={}, startTime={}, isBooked={}",
//                        schedule.getScheduleId(),
//                        schedule.getStartDateTime(),
//                        schedule.isBooked())
//        );


    }
}
