package com.tutorias.Repository;

import com.tutorias.Repository.Model.GeneralReport;
import java.util.Optional;

public interface IGeneralReportRepository {

    public Optional<GeneralReport> findById(Integer id);

    public Optional<GeneralReport> findByTutorId(Integer tutorId);

    public GeneralReport save(GeneralReport generalReport);

    public GeneralReport update(GeneralReport generalReport);

    public boolean softDelete(Integer id);
}
