package com.edutech.payment.service;

import com.edutech.common.dto.DiscountCouponDTO;
import com.edutech.payment.entity.DiscountCoupon;
import com.edutech.payment.mapper.DiscountCouponMapper;
import com.edutech.payment.repository.DiscountCouponRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.edutech.common.exception.ExceptionUtils.orThrow;

@Service
@RequiredArgsConstructor
public class DiscountCouponService {

    private final DiscountCouponRepository couponRepo;
    private final DiscountCouponMapper couponMapper;

    public List<DiscountCouponDTO> findAll() {
        return couponRepo.findAll().stream().map(couponMapper::toDTO).toList();
    }

    public DiscountCouponDTO findById(Integer id) {
        return couponMapper.toDTO(orThrow(couponRepo.findById(id), "Cupón"));
    }

    public DiscountCouponDTO findByCode(String code) {
        return couponMapper.toDTO(orThrow(couponRepo.findByCode(code), "Cupón"));
    }

    public List<DiscountCouponDTO> findActive() {
        return couponRepo.findByIsActiveTrue().stream()
            .map(couponMapper::toDTO)
            .collect(Collectors.toList());
    }

    public List<DiscountCouponDTO> findValid() {
        Instant now = Instant.now();
        return couponRepo.findByIsActiveTrueAndValidFromBeforeAndValidUntilAfter(now, now).stream()
            .map(couponMapper::toDTO)
            .collect(Collectors.toList());
    }

    public DiscountCouponDTO create(DiscountCouponDTO dto) {
        if (couponRepo.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("Ya existe un cupón con el código: " + dto.getCode());
        }
        return saveDTO(dto, null);
    }

    public DiscountCouponDTO update(Integer id, DiscountCouponDTO dto) {
        orThrow(couponRepo.findById(id), "Cupón");
        return saveDTO(dto, id);
    }

    public void delete(Integer id) {
        couponRepo.delete(orThrow(couponRepo.findById(id), "Cupón"));
    }

    public DiscountCouponDTO activate(Integer id) {
        DiscountCoupon coupon = orThrow(couponRepo.findById(id), "Cupón");
        coupon.setIsActive(true);
        return couponMapper.toDTO(couponRepo.save(coupon));
    }

    public DiscountCouponDTO deactivate(Integer id) {
        DiscountCoupon coupon = orThrow(couponRepo.findById(id), "Cupón");
        coupon.setIsActive(false);
        return couponMapper.toDTO(couponRepo.save(coupon));
    }

    private DiscountCouponDTO saveDTO(DiscountCouponDTO dto, Integer id) {
        DiscountCoupon entity = couponMapper.toEntity(dto);
        if (id != null) {
            entity.setId(id);
        }
        return couponMapper.toDTO(couponRepo.save(entity));
    }
} 