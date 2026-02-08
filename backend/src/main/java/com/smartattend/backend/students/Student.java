package com.smartattend.backend.students;

import com.smartattend.backend.auth.UserAccount;
import com.smartattend.backend.classes.ClassEntity;
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

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "roll_no", nullable = false)
    private String rollNo;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private String email;

    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity classEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private UserAccount createdBy;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    protected Student() {}

    public Student(String rollNo, String fullName, String email, String phone, ClassEntity classEntity, UserAccount createdBy) {
        this.rollNo = rollNo;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.classEntity = classEntity;
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public String getRollNo() {
        return rollNo;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public ClassEntity getClassEntity() {
        return classEntity;
    }

    public UserAccount getCreatedBy() {
        return createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setClassEntity(ClassEntity classEntity) {
        this.classEntity = classEntity;
    }

    public void setCreatedBy(UserAccount createdBy) {
        this.createdBy = createdBy;
    }
}
