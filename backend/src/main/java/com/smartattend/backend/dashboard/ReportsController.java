package com.smartattend.backend.dashboard;

import com.smartattend.backend.attendance.AttendanceRecord;
import com.smartattend.backend.attendance.AttendanceRepository;
import com.smartattend.backend.classes.ClassEntity;
import com.smartattend.backend.classes.ClassRepository;
import com.smartattend.backend.students.Student;
import com.smartattend.backend.students.StudentRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private final AttendanceRepository attendanceRepository;

    public ReportsController(StudentRepository studentRepository,
                             ClassRepository classRepository,
                             AttendanceRepository attendanceRepository) {
        this.studentRepository = studentRepository;
        this.classRepository = classRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @GetMapping("/student")
    public List<StudentReport> studentReports(
        @RequestParam(required = false) Long classId,
        @RequestParam(required = false) Long studentId
    ) {
        List<Student> students;
        if (studentId != null) {
            students = studentRepository.findById(studentId).map(List::of).orElse(List.of());
        } else if (classId != null) {
            students = studentRepository.findByClassEntityId(classId);
        } else {
            students = studentRepository.findAll();
        }

        return students.stream().map(student -> {
            List<AttendanceRecord> attendance = attendanceRepository.findByClassEntityId(student.getClassEntity().getId())
                .stream()
                .filter(record -> record.getStudent().getId().equals(student.getId()))
                .toList();
            int total = attendance.size();
            int present = (int) attendance.stream().filter(AttendanceRecord::isPresent).count();
            int absent = total - present;
            int percentage = total == 0 ? 0 : Math.toIntExact(Math.round((present * 100.0) / total));
            return new StudentReport(
                student.getId(),
                student.getRollNo(),
                student.getFullName(),
                student.getClassEntity().getName(),
                total,
                present,
                absent,
                percentage
            );
        }).toList();
    }

    @GetMapping("/date")
    public List<DateReport> dateReports(@RequestParam Long classId, @RequestParam String date) {
        ClassEntity classEntity = classRepository.findById(classId).orElseThrow();
        LocalDate parsedDate = LocalDate.parse(date);
        return attendanceRepository.findByClassEntityIdAndDate(classId, parsedDate).stream()
            .map(record -> new DateReport(
                record.getStudent().getId(),
                record.getStudent().getRollNo(),
                record.getStudent().getFullName(),
                record.isPresent(),
                classEntity.getName()
            ))
            .toList();
    }

    public record StudentReport(
        Long studentId,
        String rollNo,
        String fullName,
        String className,
        int totalDays,
        int presentDays,
        int absentDays,
        int percentage
    ) {}

    public record DateReport(
        Long studentId,
        String rollNo,
        String fullName,
        boolean present,
        String className
    ) {}
}
