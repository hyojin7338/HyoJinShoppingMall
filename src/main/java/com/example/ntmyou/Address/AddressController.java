package com.example.ntmyou.Address;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    // 특정 유저의 배송지 목록 조회
    @GetMapping("/address/{userId}")
    public ResponseEntity<List<AddressResponseDto>> getUserAddresses(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.getUserAddresses(userId));
    }

    //특정 유저 배송지 추가
    @PostMapping("/address/add/{userId}")
    public ResponseEntity<AddressResponseDto> addAddress(@PathVariable Long userId
            , @RequestBody AddressRequestDto requestDto)  {
        return ResponseEntity.ok(addressService.addAddress(userId, requestDto));
    }

    // 특정 유저의 특정 배송지 조회
    @GetMapping("/address/detail/{addressId}")
    public ResponseEntity<AddressResponseDto> getDetailAddress(@PathVariable Long addressId) {
        AddressResponseDto responseDto = addressService.getDetailAddress(addressId);
        return ResponseEntity.ok(responseDto);
    }

}
