package com.example.testSystem.controller;

import com.example.testSystem.entity.Exam;
import com.example.testSystem.entity.Question;
import com.example.testSystem.entity.Result;
import com.example.testSystem.repository.ExamRepository;
import com.example.testSystem.repository.QuestionRepository;
import com.example.testSystem.repository.ResultRepository;
import com.example.testSystem.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/exams")
@CrossOrigin(origins = "*")
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

    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/submit")
    public Result submitExam(@RequestBody SubmissionRequest request){
        int totalScore = 0;
        // fetching the exam first
        Exam exam = examRepository.findById(request.getExamId()).orElseThrow(() -> new RuntimeException("Exam not found"));
        // looping through user's answers and checking them
        for (AnswerSheet userAns: request.getAnswers()){
            // finding the correct question in the DB
            Question dbQuestion = questionRepository.findById(userAns.getQuestionId()).orElse(null);
            if(dbQuestion!=null){
                // compare user's answer vs correct answer
                if(dbQuestion.getAnswer().equals(userAns.getSelectedOption())){
                    totalScore++; // right answer, score increased
                }
            }
        }
        // creating the result object
        Result result = new Result();
        result.setUser(userRepository.findById(request.getUserId()).get());
        result.setExam(exam);
        result.setTotalQuestions(exam.getQuestions().size());
        result.setCorrectAnswers(totalScore);
        // calculate percentage
        double percentage = ((double) totalScore/exam.getQuestions().size()) * 100;
        result.setScorePercentage(percentage);
        result.setAttemptTime(LocalDateTime.now());

        // saving the result
        return resultRepository.save(result);
    }

    @GetMapping("/results/all")
    public List<Result> getAllResults(){
        return resultRepository.findAll();
    }

}

@Data
class AnswerSheet {
    private Long questionId;
    private String selectedOption;
}

@Data
class SubmissionRequest {
    private Long userId;
    private Long examId;
    private List<AnswerSheet> answers;
}
