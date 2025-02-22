package com.example.ntmyou.QnA.Service;

import com.example.ntmyou.Exception.*;
import com.example.ntmyou.Master.Entity.Master;
import com.example.ntmyou.Master.Repository.MasterRepository;
import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.Product.Repository.ProductRepository;
import com.example.ntmyou.QnA.DTO.ProductQuestionRequestDto;
import com.example.ntmyou.QnA.DTO.ProductQuestionResponseDto;
import com.example.ntmyou.QnA.Entity.ProductQuestion;
import com.example.ntmyou.QnA.Mapper.ProductQuestionMapper;
import com.example.ntmyou.QnA.Repository.ProductQuestionRepository;
import com.example.ntmyou.User.Entity.User;
import com.example.ntmyou.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductQuestionService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductQuestionRepository productQuestionRepository;
    private final MasterRepository masterRepository;


    // 문의하기 생성
    @Transactional
    public ProductQuestionResponseDto createQuestion(ProductQuestionRequestDto requestDto) {
        // 유저가 존재한지 확인
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 회원입니다."));

        // 상품이 존재하는지 확인
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("존재하지 않는 상품입니다."));

        // mapper를 활용하여 DTO -> Entity 변환 // 상품 기준으로 판매자 자동으로 설정 됨
        ProductQuestion productQuestion = ProductQuestionMapper.toEntity(requestDto, user, product);

        // JPA를 이용하여 저장
        productQuestion = productQuestionRepository.save(productQuestion);

        return ProductQuestionMapper.toResponseDto(productQuestion);
    }

    // 문의하기 수정
    @Transactional
    public ProductQuestionResponseDto updateQuestion(Long productQuestionId
            , ProductQuestionRequestDto requestDto) {
        // 문의가 존재하는지 확인
        ProductQuestion productQuestion = productQuestionRepository.findById(productQuestionId)
                .orElseThrow(() -> new QuestionNotFoundException("문의가 존재하지 않습니다."));


        // 변경할 정보가 있을 경우에만 업데이트 진행!
        if (requestDto.getTitle() != null) {
            productQuestion.setTitle(requestDto.getTitle());
        }

        if (requestDto.getContents() != null) {
            productQuestion.setContents(requestDto.getContents());
        }

        if (requestDto.getQuestionType() != null) {
            productQuestion.setQuestionType(requestDto.getQuestionType());
        }

        productQuestionRepository.save(productQuestion);

        return ProductQuestionMapper.toResponseDto(productQuestion);
    }

    // 내 문의하기 조회(특정 유저의 문의 조회)
    @Transactional(readOnly = true)
    public List<ProductQuestionResponseDto> getQuestionByUser(Long userId) {
        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 회원입니다."));

        List<ProductQuestion> userQuestion = productQuestionRepository.findByUser(user);

        return userQuestion.stream()
                .map(ProductQuestionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // 특정 판매자가 받은 모든 내용 조회
    @Transactional(readOnly = true)
    public List<ProductQuestionResponseDto> getQuestionByMaster(Long masterId) {
        // 판매자가 존재하는지 확인
        Master master = masterRepository.findById(masterId)
                .orElseThrow(() -> new MasterNotFoundException("판매자가 존재하자 않습니다."));

        // 해당 판매자가 올린 상품 중 문의 내용이 있는지 조회
        List<ProductQuestion> masterQuestion = productQuestionRepository.findByProduct_Master(master);

        return masterQuestion.stream()
                .map(ProductQuestionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

}
