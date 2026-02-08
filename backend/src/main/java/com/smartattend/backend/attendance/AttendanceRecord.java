package com.smartattend.backend.attendance;

import com.smartattend.backend.auth.UserAccount;
import com.smartattend.backend.classes.ClassEntity;
import com.smartattend.backend.students.Student;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "attendance")
public class AttendanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity classEntity;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "is_present", nullable = false)
    private boolean present;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marked_by")
    private UserAccount markedBy;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    protected AttendanceRecord() {}

    public AttendanceRecord(Student student, ClassEntity classEntity, LocalDate date, boolean present, UserAccount markedBy) {
        this.student = student;
        this.classEntity = classEntity;
        this.date = date;
        this.present = present;
        this.markedBy = markedBy;
    }

    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public ClassEntity getClassEntity() {
        return classEntity;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isPresent() {
        return present;
    }

    public UserAccount getMarkedBy() {
        return markedBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setClassEntity(ClassEntity classEntity) {
        this.classEntity = classEntity;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public void setMarkedBy(UserAccount markedBy) {
        this.markedBy = markedBy;
    }
}
