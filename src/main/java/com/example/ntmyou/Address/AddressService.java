package com.example.ntmyou.Address;

import com.example.ntmyou.Exception.UserCodeNotFoundException;
import com.example.ntmyou.User.Entity.User;
import com.example.ntmyou.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressesRepository addressesRepository;
    private final UserRepository userRepository;

    // 특저 유저의 배송지 목록 조회
    @Transactional(readOnly = true)
    public List<AddressResponseDto> getUserAddresses(Long userId) {
        List<Addresses> addresses = addressesRepository.findByUser_UserId(userId);
        return addresses.stream()
                .map(AddressMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // 배송지 추가
    @Transactional
    public AddressResponseDto addAddress(Long userId, AddressRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 회원입니다."));

        Addresses addresses = AddressMapper.toEntity(requestDto);
        addresses.setUser(user); // 유저 넣기

        Addresses saveAddress = addressesRepository.save(addresses);
        return AddressMapper.toResponseDto(saveAddress);
    }
}
