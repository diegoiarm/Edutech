package com.edutech.payment.mapper;

import com.edutech.common.dto.DiscountCouponDTO;
import com.edutech.payment.entity.DiscountCoupon;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-03T01:43:15-0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class DiscountCouponMapperImpl implements DiscountCouponMapper {

    @Override
    public DiscountCouponDTO toDTO(DiscountCoupon entity) {
        if ( entity == null ) {
            return null;
        }

        DiscountCouponDTO discountCouponDTO = new DiscountCouponDTO();

        return discountCouponDTO;
    }

    @Override
    public DiscountCoupon toEntity(DiscountCouponDTO dto) {
        if ( dto == null ) {
            return null;
        }

        DiscountCoupon discountCoupon = new DiscountCoupon();

        return discountCoupon;
    }
}
