package com.example.university.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "student_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    // ===== One-to-One with Student =====
    @OneToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Student student;
}
