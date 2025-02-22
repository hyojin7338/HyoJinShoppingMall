package com.example.ntmyou.QnA.Service;

import com.example.ntmyou.Config.Enum.Role;
import com.example.ntmyou.Exception.GeneralQuestionNotFoundException;
import com.example.ntmyou.Exception.MasterNotFoundException;
import com.example.ntmyou.Exception.RoleExistsException;
import com.example.ntmyou.Exception.UserCodeNotFoundException;
import com.example.ntmyou.Master.Entity.Master;
import com.example.ntmyou.Master.Repository.MasterRepository;
import com.example.ntmyou.QnA.DTO.GeneralQuestionRequestDto;
import com.example.ntmyou.QnA.DTO.GeneralQuestionResponseDto;
import com.example.ntmyou.QnA.Entity.GeneralQuestion;
import com.example.ntmyou.QnA.Mapper.GeneralQuestionMapper;
import com.example.ntmyou.QnA.Repository.GeneralQuestionRepository;
import com.example.ntmyou.User.Entity.User;
import com.example.ntmyou.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class GeneralQuestionService {
    private final UserRepository userRepository;
    private final MasterRepository masterRepository;
    private final GeneralQuestionRepository generalQuestionRepository;

    // 운영자에게 문의하기
    @Transactional
    public GeneralQuestionResponseDto createQuestion(GeneralQuestionRequestDto requestDto) {
        // 일반유저가 작성하면 master는 null, 판매자가 작성하면 일반유저는 null 값이 들어간다
        User user = null;
        Master master = null;

        // 일반유저 문의인지, 판매자 문의인지 구분하여 조회한다
        if (requestDto.getRole() == Role.USER) {
            user = userRepository.findById(requestDto.getUserId())
                    .orElseThrow(() -> new UserCodeNotFoundException("해당 일반 유저를 찾을 수 없습니다."));
        } else if (requestDto.getRole() == Role.MASTER) {
            master = masterRepository.findById(requestDto.getMasterId())
                    .orElseThrow(() -> new MasterNotFoundException("해당 판매자를 찾을 수 없습니다."));
        } else {
            throw new RoleExistsException("잘못된 역할(Role) 값입니다.");
        }
        // Mapper를 사용하여 Entity 변환
        GeneralQuestion generalQuestion = GeneralQuestionMapper.toEntity(requestDto, user, master);

        // 저장
        generalQuestion = generalQuestionRepository.save(generalQuestion);

        return GeneralQuestionMapper.toResponseDto(generalQuestion);
    }

    // 운영자에게 문의하기 수정
    @Transactional
    public GeneralQuestionResponseDto updateQuestion(Long generalQuestionId
            ,GeneralQuestionRequestDto requestDto) {
        // 문의 내용이 있는지 확인
        GeneralQuestion generalQuestion = generalQuestionRepository.findById(generalQuestionId)
                .orElseThrow(() -> new GeneralQuestionNotFoundException("문의 내용이 존재하지 않습니다."));

        // 변경 할 정보가 있을 경우에만 업데이트 진행한다.
        if (requestDto.getTitle() != null) {
            generalQuestion.setTitle(requestDto.getTitle());
        }
        if (requestDto.getContents() != null) {
            generalQuestion.setContents(requestDto.getContents());
        }
        if (requestDto.getQuestionType() != null) {
            generalQuestion.setQuestionType(requestDto.getQuestionType());
        }

        generalQuestionRepository.save(generalQuestion);

        return GeneralQuestionMapper.toResponseDto(generalQuestion);
    }

    // 내 문의하기 조회(특정 유저/ 특정 판매자)
    @Transactional(readOnly = true)
    public List<GeneralQuestionResponseDto> getQuestionByUser(Long userId) {
        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 회원입니다."));

        List<GeneralQuestion> generalQuestions = generalQuestionRepository.findByUser(user);

        return generalQuestions.stream()
                .map(GeneralQuestionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // 내 문의하기 조회
    @Transactional(readOnly = true)
    public List<GeneralQuestionResponseDto> getQuestionByMaster(Long masterId) {
        // 판매자 조회
        Master master = masterRepository.findById(masterId)
                .orElseThrow(() -> new MasterNotFoundException("존재하지 않는 판매자입니다."));

        List<GeneralQuestion> generalQuestions = generalQuestionRepository.findByMaster(master);

        return generalQuestions.stream()
                .map(GeneralQuestionMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
