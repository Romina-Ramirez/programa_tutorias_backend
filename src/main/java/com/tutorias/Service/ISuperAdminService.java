package com.tutorias.Service;

import java.util.List;

import com.tutorias.Service.dto.AdminDTO;

public interface ISuperAdminService {
    
    public AdminDTO createAdmin(AdminDTO dto);

    public boolean sendEmailNewAdmin(Integer adminId);

    public List<AdminDTO> listAdmins();

    public AdminDTO updateAdmin(Integer adminId, AdminDTO dto);

    public int getTutorCountByAdminId(Integer adminId);

    public boolean deleteAdmin(Integer adminId, Integer newAdminId);

    // Desactiva (soft-delete) un admin: desaparece de la lista pero queda en BD.
    public boolean deactivateAdmin(Integer adminId);

    // Elimina físicamente un admin. Si tiene tutores activos, exige reasignarlos a otro admin (newAdminId).
    public boolean hardDeleteAdmin(Integer adminId, Integer newAdminId);

    // Reactiva un admin desactivado buscándolo por cédula.
    public AdminDTO activateAdminByIdCard(String idCard);
}
