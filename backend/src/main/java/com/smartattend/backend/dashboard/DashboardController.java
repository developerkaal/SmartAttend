package com.smartattend.backend.dashboard;

import com.smartattend.backend.attendance.AttendanceRecord;
import com.smartattend.backend.attendance.AttendanceRepository;
import com.smartattend.backend.classes.ClassEntity;
import com.smartattend.backend.classes.ClassRepository;
import com.smartattend.backend.students.StudentRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    public DashboardController(ClassRepository classRepository,
                               StudentRepository studentRepository,
                               AttendanceRepository attendanceRepository) {
        this.classRepository = classRepository;
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @GetMapping
    public DashboardResponse getDashboard() {
        long totalClasses = classRepository.count();
        long totalStudents = studentRepository.count();

        LocalDate today = LocalDate.now();
        List<AttendanceRecord> todayRecords = attendanceRepository.findByDate(today);
        long todayPresent = todayRecords.stream().filter(AttendanceRecord::isPresent).count();
        long todayTotal = todayRecords.size();
        int todayPercentage = todayTotal == 0 ? 0 : Math.toIntExact(Math.round((todayPresent * 100.0) / todayTotal));

        List<AttendanceRecord> allRecords = attendanceRepository.findAll();
        long overallPresent = allRecords.stream().filter(AttendanceRecord::isPresent).count();
        long overallTotal = allRecords.size();
        int overallPercentage = overallTotal == 0 ? 0 : Math.toIntExact(Math.round((overallPresent * 100.0) / overallTotal));

        List<ClassSummary> summaries = classRepository.findAll().stream().limit(5).map(cls -> {
            List<AttendanceRecord> classRecords = attendanceRepository.findByClassEntityId(cls.getId());
            long present = classRecords.stream().filter(AttendanceRecord::isPresent).count();
            long total = classRecords.size();
            int percentage = total == 0 ? 0 : Math.toIntExact(Math.round((present * 100.0) / total));
            return new ClassSummary(cls.getId(), cls.getName(), present, total, percentage);
        }).toList();

        return new DashboardResponse(
            totalStudents,
            totalClasses,
            todayPercentage,
            overallPercentage,
            summaries
        );
    }

    public record DashboardResponse(
        long totalStudents,
        long totalClasses,
        int todayAttendance,
        int overallAttendance,
        List<ClassSummary> classSummaries
    ) {}

    public record ClassSummary(
        Long classId,
        String className,
        long present,
        long total,
        int percentage
    ) {}
}
