package com.mftplus.appointment;

import com.mftplus.appointment.dto.*;
import com.mftplus.appointment.model.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.time.LocalDateTime;
import java.util.*;

@EnableFeignClients
@SpringBootApplication
//@Slf4j
public class AppointmentApplication implements CommandLineRunner {
    private final AppointmentService appointmentService;
    private final ScheduleService scheduleService;
    private final DoctorService doctorService;
    private final SpecializationService specializationService;
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AppointmentApplication.class);


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


//        SpecializationDto specializationDto = SpecializationDto.builder()
//                .skillName("heart")
//                .description("heart spec")
//                .deleted(false)
//                .build();
//        SpecializationDto saved = specializationService.save(specializationDto);
//        log.info("saved spec : {}", saved);
        SpecializationDto specializationDto = new SpecializationDto();
        specializationDto.setSkillName("heart");
        specializationDto.setDescription("heart spec");
        specializationDto.setDeleted(false);
        SpecializationDto saved = specializationService.save(specializationDto);
        logger.info("saved spec : {}", saved);

//----------------------------------------------------------------------------------------------
        //Doctor :
//        DoctorDto doctorDto = DoctorDto.builder()
//                .doctorFirstname("alex")
//                .doctorLastname("alx")
//                .experienceYears(5)
//                .specializations(List.of(saved))
//                .build();
//        DoctorDto savedDoctor = doctorService.save(doctorDto);
//        log.info("doctor saved : {} ", savedDoctor);

        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setDoctorFirstname("alex");
        doctorDto.setDoctorLastname("alx");
        doctorDto.setExperienceYears(5);
        doctorDto.setSpecializations(List.of(saved));
        DoctorDto savedDoctor = doctorService.save(doctorDto);
        logger.info("doctor saved : {} ", savedDoctor);
//----------------------------------------------------------------------------------------------
        //Schedule :
//        ScheduleDto scheduleDto = ScheduleDto.builder()
//                .startDateTime(LocalDateTime.of(2025, 8, 25, 12, 30))
//                .endDateTime(LocalDateTime.of(2025, 8, 26, 12, 30))
//                .appointmentDurationMin(30)
//                .doctorId(savedDoctor.getDoctorUuid())
//                .isBooked(false)
//                .build();
//        ScheduleDto savedSchedule = scheduleService.createSchedulesForDoctor(savedDoctor.getDoctorUuid(), scheduleDto.getStartDateTime(), scheduleDto.getEndDateTime(), 30).get(0);
//        log.info("Saved Schedule For Doctor : {}", savedSchedule);

        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setStartDateTime(LocalDateTime.of(2025, 8, 30, 12, 30));
        scheduleDto .setEndDateTime(LocalDateTime.of(2025, 9, 12, 12, 30));
        scheduleDto.setAppointmentDurationMin(30);
        scheduleDto.setDoctorId(savedDoctor.getDoctorUuid());
        scheduleDto.setIsBooked(false);
        ScheduleDto savedSchedule = scheduleService.createSchedulesForDoctor(savedDoctor.getDoctorUuid(), scheduleDto.getStartDateTime(), scheduleDto.getEndDateTime(), 30).get(0);
        logger.info("Saved Schedule For Doctor : {}", savedSchedule);

//------------------------------------------------------------------------------------------------------------------------------
//        PermissionDto permissionDto = PermissionDto.builder().permissionName("ACCESS-ALL").build(); //For Admin
//        PermissionDto savedPermission = permissionService.save(permissionDto);
//        log.info("Permission_Access_All saved : {}", savedPermission);
//
//        PermissionDto permissionDto1 = PermissionDto.builder().permissionName("ACCESS-ALL-MODAL").build();  //For Manager
//        PermissionDto savedPermission1 = permissionService.save(permissionDto1);
//        log.info("Permission_All_Modal Saved : {}", savedPermission1);
//
//        PermissionDto permissionDto2 = PermissionDto.builder().permissionName("ACCESS-TO-MODAL-PATIENT").build();  //For Admin Of Patient Client
//        PermissionDto savedPermission2 = permissionService.save(permissionDto2);
//        log.info("Permission_Modal_Patient_Access Saved : {}", savedPermission2);
//
//        PermissionDto permissionDto3 = PermissionDto.builder().permissionName("USER-MODAL-SELF").build();  //For User
//        PermissionDto savedPermission3 = permissionService.save(permissionDto3);
//        log.info("Permission_Self_User Saved : {}", savedPermission3);
//        //-------------------------------------------------------------------------------------------------------------------------------------------
//        RoleDto roleDto = RoleDto.builder().roleName("ADMIN").permissions(Set.of(savedPermission, savedPermission1, savedPermission2)).build();
//        RoleDto savedRole = roleService.save(roleDto);
//        log.info("Role_Admin Saved : {}", savedRole);
//
//        RoleDto roleDto1 = RoleDto.builder().roleName("MANAGER").permissions(Set.of(savedPermission1)).build();
//        RoleDto savedRole1 = roleService.save(roleDto1);
//        log.info("Role_Manager Saved : {}", savedRole1);
//
//        RoleDto roleDto2 = RoleDto.builder().roleName("ADMIN_PATIENT_CLIENT").permissions(Set.of(savedPermission2)).build();
//        RoleDto savedRole2 = roleService.save(roleDto2);
//        log.info("Role_Admin_Clients Saved : {}", savedRole2);
//
//        RoleDto roleDto3 = RoleDto.builder().roleName("User").permissions(Set.of(savedPermission3)).build();
//        RoleDto savedRole3 = roleService.save(roleDto3);
//        log.info("Role_User Saved : {}", savedRole3);
////-----------------------------------------------------------------------------------------------------------------------------------------------------
//        UserDto userDto = UserDto.builder()
//                .username("user-admin")
//                .password("admin-pass")
//                .roles(Set.of(savedRole))
//                .build();
//        UserDto userSaved = userService.save(userDto);
//        log.info("User-Admin Saved : {}", userSaved);
//
//        UserDto userDto1 = UserDto.builder()
//                .username("user-manager")
//                .password("mng-pass")
//                .roles(Set.of(savedRole1))
//                .build();
//        UserDto userSaved1 = userService.save(userDto1);
//        log.info("User-Manager Saved : {}", userSaved1);
//
//
//        UserDto userDto2 = UserDto.builder()
//                .username("user-admin-clients")
//                .password("admin-client-pass")
//                .roles(Set.of(savedRole2))
//                .build();
//        UserDto userSaved2 = userService.save(userDto2);
//        log.info("User-Admin-Client Saved : {}", userSaved2);
//
//
//        UserDto userDto3 = UserDto.builder()
//                .username("user-manage-server")
//                .password("user-server-pass")
//                .roles(Set.of(savedRole1, savedRole2))
//                .build();
//        UserDto userSaved3 = userService.save(userDto3);
//        log.info("User-Manage-Server Saved : {} ", userSaved3);
//
//        UserDto userDto4 = UserDto.builder()
//                .username("user")
//                .password("user-pass")
//                .roles(Set.of(savedRole3))
//                .build();
//        UserDto userSaved4 = userService.save(userDto4);
//        log.info("User Saved :{}", userSaved4);

//-----------------------------------------------------------------------------------------------------------------------------------------------------

//        AppointmentDto appointment1 = AppointmentDto.builder()
//                        .appointmentDateTime(LocalDateTime.of(2025, 8, 25, 16, 30))
//                        .scheduleId(savedSchedule.getScheduleUuid())
//                        .patientUuid(UUID.fromString("ad903218-bd67-4e50-800e-43de9f842a3d"))
//                        .build();
//        AppointmentDto savedAppointment1 = appointmentService.create(appointment1);
//
//        log.info("Saved Appointment : {}", savedAppointment1);

        AppointmentDto appointment1 = new AppointmentDto();
        appointment1.setAppointmentDateTime(LocalDateTime.of(2025, 8, 28, 12, 30));
        appointment1.setScheduleId(savedSchedule.getScheduleUuid());
        appointment1.setPatientUuid(UUID.fromString("ad903218-bd67-4e50-800e-43de9f842a34"));
        AppointmentDto savedAppointment1 = appointmentService.create(appointment1);

        logger.info("Saved Appointment : {}", savedAppointment1);


    }
}
