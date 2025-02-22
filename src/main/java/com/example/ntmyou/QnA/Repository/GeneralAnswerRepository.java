package com.example.ntmyou.QnA.Repository;

import com.example.ntmyou.QnA.Entity.GeneralAnswer;
import com.example.ntmyou.QnA.Entity.GeneralQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeneralAnswerRepository extends JpaRepository<GeneralAnswer, Long> {
    List<GeneralAnswer> findByGeneralQuestion(GeneralQuestion generalQuestion);
}