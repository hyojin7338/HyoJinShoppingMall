package com.example.ntmyou.QnA.Repository;

import com.example.ntmyou.QnA.Entity.ProductAnswer;
import com.example.ntmyou.QnA.Entity.ProductQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductAnswerRepository extends JpaRepository<ProductAnswer, Long> {
    List<ProductAnswer> findByProductQuestion(ProductQuestion productQuestion);
}
