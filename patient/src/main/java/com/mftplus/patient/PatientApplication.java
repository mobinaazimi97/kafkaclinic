package com.mftplus.patient;

import com.mftplus.patient.dto.*;
import com.mftplus.patient.model.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.Set;

@EnableFeignClients
@SpringBootApplication
//@EnableFeignClients(basePackages = "com.mftplus.patient.model.service")
//@Slf4j
public class PatientApplication implements CommandLineRunner {

    private final PermissionService permissionService;
    private final RoleService roleService;
    private final UserService userService;

    public PatientApplication(PermissionService permissionService, RoleService roleService, UserService userService) {
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
//        PermissionDto permissionDto = PermissionDto.builder().permissionName("ACCESS-ALL").build(); //For Admin
//        PermissionDto savedPermission = permissionService.save(permissionDto);
//       log.info("Permission_Access_All saved : {}" , savedPermission);
//
//        PermissionDto permissionDto1 = PermissionDto.builder().permissionName("ACCESS-ALL-MODAL").build();  //For Manager
//        PermissionDto savedPermission1 = permissionService.save(permissionDto1);
//       log.info("Permission_All_Modal Saved : {}" , savedPermission1);
//
//        PermissionDto permissionDto2 = PermissionDto.builder().permissionName("ACCESS-TO-MODAL-PATIENT").build();  //For Admin Of Patient Client
//        PermissionDto savedPermission2 = permissionService.save(permissionDto2);
//        log.info("Permission_Modal_Patient_Access Saved : {}" , savedPermission2);
//
//        PermissionDto permissionDto3 = PermissionDto.builder().permissionName("USER-MODAL-SELF").build();  //For User
//        PermissionDto savedPermission3 = permissionService.save(permissionDto3);
//        log.info("Permission_Self_User Saved : {}" , savedPermission3);
//        //-------------------------------------------------------------------------------------------------------------------------------------------
//        RoleDto roleDto = RoleDto.builder().roleName("ADMIN").permissions(Set.of(savedPermission, savedPermission1, savedPermission2)).build();
//        RoleDto savedRole = roleService.save(roleDto);
//       log.info("Role_Admin Saved : {}" , savedRole);
//
//        RoleDto roleDto1 = RoleDto.builder().roleName("MANAGER").permissions(Set.of(savedPermission1)).build();
//        RoleDto savedRole1 = roleService.save(roleDto1);
//        log.info("Role_Manager Saved : {}" , savedRole1);
//
//        RoleDto roleDto2 = RoleDto.builder().roleName("ADMIN_PATIENT_CLIENT").permissions(Set.of(savedPermission2)).build();
//        RoleDto savedRole2 = roleService.save(roleDto2);
//        log.info("Role_Admin_Clients Saved : {}" , savedRole2);
//
//        RoleDto roleDto3 = RoleDto.builder().roleName("User").permissions(Set.of(savedPermission3)).build();
//        RoleDto savedRole3 = roleService.save(roleDto3);
//       log.info("Role_User Saved : {}" , savedRole3);
////-----------------------------------------------------------------------------------------------------------------------------------------------------
//        UserDto userDto = UserDto.builder()
//                .username("user-admin")
//                .password("admin-pass")
//                .roles(Set.of(savedRole))
//                .build();
//        UserDto userSaved = userService.save(userDto);
//        log.info("User-Admin Saved : {}" , userSaved);
//
//        UserDto userDto1 = UserDto.builder()
//                .username("user-manager")
//                .password("mng-pass")
//                .roles(Set.of(savedRole1))
//                .build();
//        UserDto userSaved1 = userService.save(userDto1);
//        log.info("User-Manager Saved : {}" , userSaved1);
//
//
//        UserDto userDto2 = UserDto.builder()
//                .username("user-admin-clients")
//                .password("admin-client-pass")
//                .roles(Set.of(savedRole2))
//                .build();
//        UserDto userSaved2 = userService.save(userDto2);
//        log.info("User-Admin-Client Saved : {}" , userSaved2);
//
//
//        UserDto userDto3 = UserDto.builder()
//                .username("user-manage-server")
//                .password("user-server-pass")
//                .roles(Set.of(savedRole1, savedRole2))
//                .build();
//        UserDto userSaved3 = userService.save(userDto3);
//        log.info("User-Manage-Server Saved : {}" , userSaved3);
//
//        UserDto userDto4 = UserDto.builder()
//                .username("user")
//                .password("user-pass")
//                .roles(Set.of(savedRole3))
//                .build();
//        UserDto userSaved4 = userService.save(userDto4);
//        log.info("User Saved : {}" , userSaved4);
    }
}
