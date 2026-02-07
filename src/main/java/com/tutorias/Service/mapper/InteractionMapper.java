package com.tutorias.Service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.tutorias.Repository.Model.Forum;
import com.tutorias.Repository.Model.ForumComment;
import com.tutorias.Repository.Model.GeneralReport;
import com.tutorias.Repository.Model.Grade;
import com.tutorias.Repository.Model.Report;
import com.tutorias.Repository.Model.Student;
import com.tutorias.Repository.Model.User;
import com.tutorias.Service.dto.ForumCommentDTO;
import com.tutorias.Service.dto.ForumDTO;
import com.tutorias.Service.dto.GeneralReportDTO;
import com.tutorias.Service.dto.GradeDTO;
import com.tutorias.Service.dto.ReportDTO;
import com.tutorias.Service.dto.StudentDTO;
import com.tutorias.Service.dto.StudentGradesDTO;

@Component
public class InteractionMapper {
    
    public StudentDTO convertToStudentDTO(Student student) {
        if (student == null) return null;

        User u = student.getUser();
        return StudentDTO.builder()
                .id(student.getId())
                .name(u != null ? u.getName() : null)
                .lastName(u != null ? u.getLastName() : null)
                .build();
    }

    public ForumDTO convertToForumDTO(Forum forum) {
        if (forum == null) return null;

        return ForumDTO.builder()
                .id(forum.getId())
                .title(forum.getTitle())
                .createdAt(forum.getCreatedAt())
                .build();
    }

    public ForumCommentDTO convertToForumCommentDTO(ForumComment comment) {
        if (comment == null) return null;

        User author = comment.getUser();
        String authorName = (author == null)
                ? null
                : (author.getName() + " " + author.getLastName());

        return ForumCommentDTO.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .authorName(authorName)
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public GradeDTO convertToGradeDTO(Grade grade) {
        if (grade == null) return null;

        return GradeDTO.builder()
                .id(grade.getId())
                .activity(grade.getActivity())
                .qualification(grade.getQualification())
                .observations(grade.getObservations())
                .build();
    }

    public ReportDTO convertToReportDTO(Report report) {
        if (report == null) return null;

        return ReportDTO.builder()
                .id(report.getId())
                .createdAt(report.getCreatedAt())
                .activityDescription(report.getActivityDescription())
                .minutesCompleted(report.getMinutesCompleted())
                .build();
    }

    public GeneralReportDTO convertToGeneralReportDTO(GeneralReport gr, String tutorName, List<Report> reports) {
        if (gr == null) return null;

        List<ReportDTO> reportDTOs = (reports == null) ? List.of() :
                reports.stream().map(this::convertToReportDTO).collect(Collectors.toList());

        Integer tutorId = (gr.getTutor() != null) ? gr.getTutor().getId() : null;

        return GeneralReportDTO.builder()
                .tutorId(tutorId)
                .tutorName(tutorName)
                .observation(gr.getObservation())
                .reports(reportDTOs)
                .build();
    }

    public StudentGradesDTO convertToStudentGradesDTO(Integer courseId, String courseName, List<Grade> grades) {
        List<GradeDTO> gradeDTOs = (grades == null) ? List.of()
                : grades.stream().map(this::convertToGradeDTO).toList();

        return StudentGradesDTO.builder()
                .courseId(courseId)
                .courseName(courseName)
                .grades(gradeDTOs)
                .build();
    }

}
