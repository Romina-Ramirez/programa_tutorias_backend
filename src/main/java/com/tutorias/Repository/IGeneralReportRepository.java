package com.tutorias.Repository;

import com.tutorias.Repository.Model.GeneralReport;
import java.util.Optional;

public interface IGeneralReportRepository {

    public Optional<GeneralReport> findById(Integer id);

    public Optional<GeneralReport> findByTutorId(Integer tutorId);

    public GeneralReport save(GeneralReport generalReport);

    public GeneralReport update(GeneralReport generalReport);

    public boolean softDelete(Integer id);

    // Borra físicamente los reports asociados al general report del tutor (hijos primero).
    public int hardDeleteReportsByTutorId(Integer tutorId);

    // Borra físicamente el general report del tutor.
    public int hardDeleteByTutorId(Integer tutorId);
}
