package com.smartattend.backend.attendance;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByClassEntityIdAndDate(Long classId, LocalDate date);
    List<AttendanceRecord> findByDate(LocalDate date);
    List<AttendanceRecord> findByClassEntityId(Long classId);
}
