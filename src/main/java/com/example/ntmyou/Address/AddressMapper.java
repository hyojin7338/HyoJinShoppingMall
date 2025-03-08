package com.example.ntmyou.Address;

public class AddressMapper {
    // requestDto -> Entity
    public static Addresses toEntity(AddressRequestDto requestDto) {
        return Addresses.builder()
                .address(requestDto.getAddress())
                .region(requestDto.getRegion())
                .receiverName(requestDto.getReceiverName())
                .receiverTel(requestDto.getReceiverTel())
                .build();
    }

    // Entity -> responseDto
    public static AddressResponseDto toResponseDto(Addresses addresses) {
        return AddressResponseDto.builder()
                .addressId(addresses.getAddressId())
                .address(addresses.getAddress())
                .region(addresses.getRegion())
                .receiverName(addresses.getReceiverName())
                .receiverTel(addresses.getReceiverTel())
                .build();
    }
}
