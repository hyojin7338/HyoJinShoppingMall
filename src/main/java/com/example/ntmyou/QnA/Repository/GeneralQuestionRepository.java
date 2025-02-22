package com.example.ntmyou.QnA.Repository;

import com.example.ntmyou.Master.Entity.Master;
import com.example.ntmyou.QnA.Entity.GeneralQuestion;
import com.example.ntmyou.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeneralQuestionRepository extends JpaRepository<GeneralQuestion, Long> {
    List<GeneralQuestion> findByUser(User user);

    List<GeneralQuestion> findByMaster(Master master);
}