package com.edutech.payment.mapper;

import com.edutech.common.dto.PaymentDTO;
import com.edutech.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target = "paymentDate", ignore = true)
    @Mapping(target = "paymentInstitution", ignore = true)
    @Mapping(target = "transactionId", ignore = true)
    PaymentDTO toDTO(Payment entity);

    Payment toEntity(PaymentDTO dto);
} 