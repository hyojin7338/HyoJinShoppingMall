package com.example.ntmyou.QnA.Service;

import com.example.ntmyou.Admin.Admin;
import com.example.ntmyou.Admin.AdminRepository;
import com.example.ntmyou.Exception.AdminNotFoundException;
import com.example.ntmyou.Exception.GeneralQuestionNotFoundException;
import com.example.ntmyou.QnA.DTO.GeneralAnswerRequestDto;
import com.example.ntmyou.QnA.DTO.GeneralAnswerResponseDto;
import com.example.ntmyou.QnA.Entity.GeneralAnswer;
import com.example.ntmyou.QnA.Entity.GeneralQuestion;
import com.example.ntmyou.QnA.Mapper.GeneralAnswerMapper;
import com.example.ntmyou.QnA.Repository.GeneralAnswerRepository;
import com.example.ntmyou.QnA.Repository.GeneralQuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeneralAnswerService {
    private final GeneralQuestionRepository generalQuestionRepository;
    private final AdminRepository adminRepository;
    private final GeneralAnswerRepository generalAnswerRepository;

    public GeneralAnswerService(GeneralQuestionRepository generalQuestionRepository,
                                AdminRepository adminRepository,
                                GeneralAnswerRepository generalAnswerRepository) {
        this.generalQuestionRepository = generalQuestionRepository;
        this.adminRepository = adminRepository;
        this.generalAnswerRepository = generalAnswerRepository;
    }

    // 답변 달기
    @Transactional
    public GeneralAnswerResponseDto createAnswer(GeneralAnswerRequestDto requestDto) {
        // 문의가 존재하는지 확인
        GeneralQuestion question = generalQuestionRepository.findById(requestDto.getGeneralQuestionId())
                .orElseThrow(() -> new GeneralQuestionNotFoundException("존재하지 않는 문의내용입니다."));

        // 운영자 답변
        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() -> new AdminNotFoundException("존재하지 않는 운영자입니다."));

        // Dto -> Entity 변환
        GeneralAnswer generalAnswer = GeneralAnswerMapper.toEntity(requestDto, question, admin);
        generalAnswer = generalAnswerRepository.save(generalAnswer);

        // 문의 상태를 False -> True 로 변환
        question.setIsAnswered(true);
        generalQuestionRepository.save(question);

        return GeneralAnswerMapper.toResponseDto(generalAnswer);
    }

    // 한 상품 문의건 전체 조희
    @Transactional(readOnly = true)
    public List<GeneralAnswerResponseDto> getAnswerByQuestion(Long generalQuestionId){
        // 문의가 존재하는지 확인
        GeneralQuestion generalQuestion = generalQuestionRepository.findById(generalQuestionId)
                .orElseThrow(() -> new GeneralQuestionNotFoundException("존재하지 않는 문의힙니다."));

        // 해당 문의에 달린 답변들 조회 후 Dto 변환
        List<GeneralAnswer> answers = generalAnswerRepository.findByGeneralQuestion(generalQuestion);

        return answers.stream()
                .map(GeneralAnswerMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
