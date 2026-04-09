package com.tutorias.Service;

import java.util.List;

import com.tutorias.Service.dto.AdminDTO;

public interface ISuperAdminService {
    
    public AdminDTO createAdmin(AdminDTO dto);

    public boolean sendEmailNewAdmin(Integer adminId);

    public List<AdminDTO> listAdmins();

    public AdminDTO updateAdmin(Integer adminId, AdminDTO dto);

    public boolean deleteAdmin(Integer adminId, Integer newAdminId);
}
