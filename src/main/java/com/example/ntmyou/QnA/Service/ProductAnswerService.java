package com.example.ntmyou.QnA.Service;

import com.example.ntmyou.Exception.MasterNotFoundException;
import com.example.ntmyou.Exception.ProductNotMatchesMasterException;
import com.example.ntmyou.Exception.ProductQuestionNotFoundException;
import com.example.ntmyou.Master.Entity.Master;
import com.example.ntmyou.Master.Repository.MasterRepository;
import com.example.ntmyou.QnA.DTO.ProductAnswerRequestDto;
import com.example.ntmyou.QnA.DTO.ProductAnswerResponseDto;
import com.example.ntmyou.QnA.Entity.ProductAnswer;
import com.example.ntmyou.QnA.Entity.ProductQuestion;
import com.example.ntmyou.QnA.Mapper.ProductAnswerMapper;
import com.example.ntmyou.QnA.Repository.ProductAnswerRepository;
import com.example.ntmyou.QnA.Repository.ProductQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductAnswerService {
    private final ProductQuestionRepository productQuestionRepository;
    private final ProductAnswerRepository productAnswerRepository;
    private final MasterRepository masterRepository;
    @Transactional
    public ProductAnswerResponseDto createAnswer(ProductAnswerRequestDto requestDto) {
        // ️문의가 존재하는지 확인
        ProductQuestion productQuestion = productQuestionRepository.findById(requestDto.getProductQuestionId())
                .orElseThrow(() -> new ProductQuestionNotFoundException("존재하지 않는 문의입니다."));

        // ️답변 작성자가 실제 해당 상품의 판매자인지 검증
        Master master = masterRepository.findById(requestDto.getMasterId())
                .orElseThrow(() -> new MasterNotFoundException("판매자가 존재하지 않습니다."));

        if (!productQuestion.getProduct().getMaster().equals(master)) {
            throw new ProductNotMatchesMasterException("해당 상품을 등록한 판매자만 답변을 작성할 수 있습니다.");
        }

        //DTO → Entity 변환 후 저장
        ProductAnswer productAnswer = ProductAnswerMapper.toEntity(requestDto, productQuestion, master);
        productAnswer = productAnswerRepository.save(productAnswer);

        // 문의 상태를 `isAnswered = true`로 변경
        productQuestion.setIsAnswered(true);
        productQuestionRepository.save(productQuestion);

        return ProductAnswerMapper.toResponseDto(productAnswer);
    }

    // (readOnly = true) 한 상품 문의건에 대하여 조회! 조회전용
    @Transactional(readOnly = true)
    public List<ProductAnswerResponseDto> getAnswersByQuestion(Long productQuestionId) {
        // 문의가 존재하는지 확인
        ProductQuestion productQuestion = productQuestionRepository.findById(productQuestionId)
                .orElseThrow(() -> new ProductQuestionNotFoundException("존재하지 않는 문의입니다."));

        // 해당 문의에 달린 답변들을 조회 후 DTO로 변환
        List<ProductAnswer> answers = productAnswerRepository.findByProductQuestion(productQuestion);

        return answers.stream()
                .map(ProductAnswerMapper::toResponseDto)
                .collect(Collectors.toList());
    }

}
