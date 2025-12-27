package com.example.testSystem.controller;

import com.example.testSystem.entity.ProctorLog;
import com.example.testSystem.repository.ProctorLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/proctor")
@CrossOrigin(origins = "*")
public class ProctorController {
    @Autowired
    private ProctorLogRepository proctorLogRepository;

    @PostMapping("/log")
    public String logViolation(@RequestBody ProctorLog log){
        log.setTimeStamp(LocalDateTime.now());
        proctorLogRepository.save(log);
        System.out.println("ALERT: Cheating detected ! " + log.getViolationType());
        return "Violation Logged";
    }

    @GetMapping("/logs/all")
    public List<ProctorLog> getAllLogs(){
        return proctorLogRepository.findAll();
    }
}
