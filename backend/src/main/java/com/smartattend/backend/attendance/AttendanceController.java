package com.smartattend.backend.attendance;

import com.smartattend.backend.auth.UserAccount;
import com.smartattend.backend.auth.UserAccountRepository;
import com.smartattend.backend.classes.ClassEntity;
import com.smartattend.backend.classes.ClassRepository;
import com.smartattend.backend.students.Student;
import com.smartattend.backend.students.StudentRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private final UserAccountRepository userAccountRepository;

    public AttendanceController(AttendanceRepository attendanceRepository,
                                StudentRepository studentRepository,
                                ClassRepository classRepository,
                                UserAccountRepository userAccountRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.classRepository = classRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @GetMapping
    public List<AttendanceResponse> getAttendance(
        @RequestParam Long classId,
        @RequestParam String date
    ) {
        LocalDate parsedDate = LocalDate.parse(date);
        return attendanceRepository.findByClassEntityIdAndDate(classId, parsedDate).stream()
            .map(AttendanceResponse::from)
            .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public List<AttendanceResponse> saveAttendance(
        @Valid @RequestBody AttendanceRequest request,
        @RequestHeader(name = "X-User-Id", required = false) Long userId
    ) {
        LocalDate parsedDate = LocalDate.parse(request.date());
        ClassEntity classEntity = classRepository.findById(request.classId()).orElseThrow();
        UserAccount marker = userId == null ? null : userAccountRepository.findById(userId).orElse(null);

        List<AttendanceRecord> existing = attendanceRepository.findByClassEntityIdAndDate(classEntity.getId(), parsedDate);
        attendanceRepository.deleteAll(existing);

        List<AttendanceRecord> saved = request.records().stream().map(record -> {
            Student student = studentRepository.findById(record.studentId()).orElseThrow();
            return new AttendanceRecord(student, classEntity, parsedDate, record.present(), marker);
        }).map(attendanceRepository::save).toList();

        return saved.stream().map(AttendanceResponse::from).toList();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearAttendance(@RequestParam Long classId, @RequestParam String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        attendanceRepository.deleteAll(attendanceRepository.findByClassEntityIdAndDate(classId, parsedDate));
    }

    public record AttendanceRequest(
        @NotNull Long classId,
        @NotNull String date,
        @NotNull List<AttendanceItem> records
    ) {}

    public record AttendanceItem(@NotNull Long studentId, boolean present) {}

    public record AttendanceResponse(
        Long id,
        Long studentId,
        String studentName,
        Long classId,
        String className,
        String date,
        boolean present
    ) {
        public static AttendanceResponse from(AttendanceRecord record) {
            return new AttendanceResponse(
                record.getId(),
                record.getStudent().getId(),
                record.getStudent().getFullName(),
                record.getClassEntity().getId(),
                record.getClassEntity().getName(),
                record.getDate().toString(),
                record.isPresent()
            );
        }
    }
}
