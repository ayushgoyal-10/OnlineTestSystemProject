package com.example.testSystem.controller;

import com.example.testSystem.entity.Exam;
import com.example.testSystem.entity.Question;
import com.example.testSystem.repository.ExamRepository;
import com.example.testSystem.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/exams")
public class ExamController {
    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping("/create")
    public Exam createExam(@RequestBody Exam exam){
        return examRepository.save(exam);
    }

    @PostMapping("/{examId}/addQuestion")
    public Question addQuestion(@PathVariable Long examId, @RequestBody Question question){
        // first, find the exam
        Exam exam = examRepository.findById(examId).orElseThrow(()-> new RuntimeException("Exam not found"));
        // link the question to the exam
        question.setExam(exam);
        return questionRepository.save(question);
    }

    @GetMapping("/{examId}")
    public Exam getExamWithQuestions(@PathVariable Long examId){
        return examRepository.findById(examId).orElse(null);
    }
}
