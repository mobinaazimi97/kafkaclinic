package com.mftplus.patient;

import com.mftplus.patient.dto.*;
import com.mftplus.patient.service.*;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@EnableFeignClients
@SpringBootApplication
public class PatientApplication implements CommandLineRunner {
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final UserService userService;

    public PatientApplication(PatientService patientService, AppointmentService appointmentService, PermissionService permissionService, RoleService roleService, UserService userService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.permissionService = permissionService;
        this.roleService = roleService;
        this.userService = userService;
    }


    public static void main(String[] args) {
        SpringApplication.run(PatientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        //------------------------------------------------------------------------------------------------------------------------------
        PermissionDto permissionDto = PermissionDto.builder().permissionName("ACCESS-ALL").build();
        PermissionDto savedPermission = permissionService.save(permissionDto);
        System.out.println("Permission saved :" + savedPermission);
//-----------------------------------------------------------------------------------------------------------------------------------------
        RoleDto roleDto = RoleDto.builder().roleName("ROLE_ADMIN").permissions(Set.of(savedPermission)).build();
        RoleDto savedRole = roleService.save(roleDto);
        System.out.println("Role Saved :" + savedRole);
//-----------------------------------------------------------------------------------------------------------------------------------------

        UserDto userDto = UserDto.builder()
                .username("mobi")
                .password("m32145")
                .roles(Set.of(savedRole))
                .build();
        UserDto userSaved = userService.save(userDto);
        System.out.println("User saved :" + userSaved);
//        System.out.println("Starting simple manual tests...");
        //-----------------------------------------------------------------------------------------------------------------------------------------

        try {
            // ایجاد یک قرار ملاقات
            AppointmentDto appointmentDto = new AppointmentDto();
            appointmentDto.setAppointmentDateTime(LocalDateTime.now().plusDays(1));
            appointmentDto.setNotes("Initial appointment");
            appointmentDto.setId(UUID.fromString("b71ef350-c143-429a-8693-3ea866314fb7"));
            appointmentDto.setScheduleId(appointmentDto.getScheduleId());

            // فراخوانی سرویس Appointment
            ResponseEntity<AppointmentDto> appointmentResponse = appointmentService.createAppointment(appointmentDto);
            if (appointmentResponse.getStatusCode().is2xxSuccessful() && appointmentResponse.getBody() != null) {
                UUID appointmentId = appointmentResponse.getBody().getId();
                System.out.println("Created appointment with ID: " + appointmentId);

//                 ایجاد بیمار با appointmentId
                PatientDto patientDto = new PatientDto();
                patientDto.setFirstName("Ali");
                patientDto.setLastName("Rezaei");
                patientDto.setAge(30);
                patientDto.setPhone("09123456789");
                patientDto.setAppointmentId(appointmentId);

                // ذخیره بیمار
                PatientDto savedPatient = patientService.savePatient(patientDto);
                System.out.println("Saved patient: " + savedPatient);

                // بررسی بیمار ذخیره‌شده
                if (savedPatient != null && savedPatient.getPatientUuid() != null) {
                    PatientDto foundPatient = patientService.findById(savedPatient.getPatientUuid());
                    System.out.println("Found patient: " + foundPatient);

                    // به‌روزرسانی بیمار (اختیاری)
                    foundPatient.setPhone("09998887766");
                    PatientDto updatedPatient = patientService.updatePatient(foundPatient.getPatientUuid(), foundPatient);
                    System.out.println("Updated patient: " + updatedPatient);

                    // بررسی بیماران مرتبط با appointmentId
                    List<PatientDto> patientsByAppointment = patientService.getByAppointmentId(appointmentId);
                    System.out.println("Patients with appointmentId " + appointmentId + ": " + patientsByAppointment);
                } else {
                    System.out.println("Failed to save patient or missing patientId.");
                }
            } else {
                System.out.println("Failed to create appointment.");
            }
        } catch (FeignException e) {
            System.err.println("Feign error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Manual tests finished.");

        System.out.println("Starting simple manual tests...");
        try {
            PatientDto patientDto = new PatientDto();
            patientDto.setFirstName("Ali");
            patientDto.setLastName("Rezaei");
            patientDto.setAge(30);
            patientDto.setPhone("09123456789");
            patientDto.setAppointmentId(patientDto.getAppointmentId());

//            //------------------------------------------------------------------------------------------
//
            AppointmentDto appointmentDetails = new AppointmentDto();
            appointmentDetails.setAppointmentDateTime(LocalDateTime.now().plusDays(1));
            appointmentDetails.setNotes("Initial appointment");


            PatientDto savedPatient = patientService.savePatient(patientDto);
            System.out.println("Saved patient: " + savedPatient);

            if (savedPatient != null && savedPatient.getPatientUuid() != null) {
                PatientDto foundPatient = patientService.findById(savedPatient.getPatientUuid());
                System.out.println("Found patient: " + foundPatient);
//
                foundPatient.setPhone("09998887766");
                patientService.updatePatient(foundPatient.getPatientUuid(), foundPatient);
                System.out.println("Updated patient phone");
            } else {
                System.out.println("Failed to save patient or missing patientId.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during manual tests: " + e.getMessage());
        }
        System.out.println("Manual tests finished.");

            //----------------------------------------------------------------------------------------------

//        SpecializationDto specializationDto = SpecializationDto.builder()
//                .skillName("heart")
//                .description("adsfdg")
//                .build();
//        SpecializationDto saved = specializationService.saveSpecialization(specializationDto);
//
//        DoctorDto doctorDto = DoctorDto.builder().doctorName("mobina").specialization(saved).build();
//        doctorService.saveDoctor(doctorDto);


//        System.out.println("Starting simple manual tests...");
//        try {
//            PatientDto patientDto = new PatientDto();
//            patientDto.setFirstName("Ali");
//            patientDto.setLastName("Rezaei");
//            patientDto.setAge(30);
//            patientDto.setPhone("09123456789");
//
//            //------------------------------------------------------------------------------------------
//
//            AppointmentDto appointmentDetails = new AppointmentDto();
//            appointmentDetails.setAppointmentDateTime(LocalDateTime.now().plusDays(1));
//            appointmentDetails.setNotes("Initial appointment");
////            patientDto.setAppointmentDetails(appointmentDetails);
//
////            PatientDto savedPatient = patientService.savePatientAndRequestAppointment(patientDto);
//            patientDto.setAppointmentId(appointmentDetails.getId());
//            PatientDto savedPatient = patientService.savePatient(patientDto);
//            System.out.println("Saved patient: " + savedPatient);
//
//            if (savedPatient != null && savedPatient.getPatientId() != null) {
//                PatientDto foundPatient = patientService.findById(savedPatient.getPatientId());
//                System.out.println("Found patient: " + foundPatient);
////
////                foundPatient.setPhone("09998887766");
////                patientService.updatePatient(foundPatient.getPatientId(), foundPatient);
////                System.out.println("Updated patient phone");
//            } else {
//                System.out.println("Failed to save patient or missing patientId.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("Error during manual tests: " + e.getMessage());
//        }
//        System.out.println("Manual tests finished.");

            //----------------------------------------------------------------------------------------------

//        SpecializationDto specializationDto = SpecializationDto.builder()
//                .skillName("heart")
//                .description("adsfdg")
//                .build();
//        SpecializationDto saved = specializationService.saveSpecialization(specializationDto);
//
//        DoctorDto doctorDto = DoctorDto.builder().doctorName("mobina").specialization(saved).build();
//        doctorService.saveDoctor(doctorDto);

    }
}
