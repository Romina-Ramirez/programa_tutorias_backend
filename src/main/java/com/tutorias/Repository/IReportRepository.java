package com.tutorias.Repository;

import com.tutorias.Repository.Model.Report;
import java.util.List;
import java.util.Optional;

public interface IReportRepository {

    public Optional<Report> findById(Integer id);

    public Report save(Report report);

    public Report update(Report report); 

    public boolean hardDelete(Integer id); 

    public List<Report> findByCourseId(Integer courseId);

    public List<Report> findByGeneralReportId(Integer generalReportId);
}
