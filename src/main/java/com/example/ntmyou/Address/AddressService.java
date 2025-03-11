package com.example.ntmyou.Address;

import com.example.ntmyou.Exception.AddressNotFoundException;
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

    // 특정 유저의 특정 배송지
    @Transactional(readOnly = true)
    public AddressResponseDto getDetailAddress(Long addressId) {
        Addresses addresses = addressesRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("존재하지 않는 배송지입니다."));
        return AddressMapper.toResponseDto(addresses);
    }

    // 기본 배송지 변경하는 API 생성
    @Transactional
    public void updateDefaultAddress(Long userId, Long addressId) {
        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 유저입니다."));
        // 주소 조회
        Addresses addresses = addressesRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("존재하지 않는 주소입니다."));

        // 기존의 기본 배송지 찾기
        List<Addresses> userAddresses = addressesRepository.findByUserAndIsDefault(user, true);
        for (Addresses address : userAddresses) {
            address.setDefault(false);
        }

        //  새로운 기본 배송지 설정
        addresses.setDefault(true);

        //  변경된 주소들 저장
        addressesRepository.saveAll(userAddresses);
        addressesRepository.save(addresses);

    }
}
