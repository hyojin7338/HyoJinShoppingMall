package com.example.ntmyou.QnA.Repository;

import com.amazonaws.services.ec2.model.ProductCodeValues;
import com.example.ntmyou.Master.Entity.Master;
import com.example.ntmyou.QnA.Entity.ProductQuestion;
import com.example.ntmyou.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductQuestionRepository extends JpaRepository<ProductQuestion, Long> {
    // 내가 작성한 문의 확인
    List<ProductQuestion> findByUser(User user);

    // 특정 판매자의 상품들에 달린 문의 전체 조회
    List<ProductQuestion> findByProduct_Master(Master master);

    Optional<ProductQuestion> findByProductQuestionId(Long productQuestionId);

}
