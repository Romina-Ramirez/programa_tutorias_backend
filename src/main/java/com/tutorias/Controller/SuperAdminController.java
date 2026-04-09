package com.tutorias.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tutorias.Service.ISuperAdminService;
import com.tutorias.Service.dto.AdminDTO;

@RestController
@CrossOrigin
@RequestMapping("/superadmin")
public class SuperAdminController {

    @Autowired
    private ISuperAdminService superAdminService;

    @PostMapping("/admins")
    public ResponseEntity<AdminDTO> createAdmin(@RequestBody AdminDTO dto) {
        return ResponseEntity.ok(superAdminService.createAdmin(dto));
    }

    @GetMapping("/admins")
    public ResponseEntity<List<AdminDTO>> listAdmins() {
        return ResponseEntity.ok(superAdminService.listAdmins());
    }

    @PutMapping("/admins/{adminId}")
    public ResponseEntity<AdminDTO> updateAdmin(
            @PathVariable Integer adminId,
            @RequestBody AdminDTO dto
    ) {
        return ResponseEntity.ok(superAdminService.updateAdmin(adminId, dto));
    }

    @DeleteMapping("/admins/{adminId}")
    public ResponseEntity<Boolean> deleteAdmin(
            @PathVariable Integer adminId,
            @RequestParam Integer newAdminId
    ) {
        return ResponseEntity.ok(
                superAdminService.deleteAdmin(adminId, newAdminId)
        );
    }
}