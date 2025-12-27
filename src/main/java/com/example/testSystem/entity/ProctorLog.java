package com.example.testSystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "proctor_logs")
public class ProctorLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long studentId;
    private Long examId;
    private String violationType;
    private String message;
    private LocalDateTime timeStamp;
}
