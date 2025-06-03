package com.edutech.payment.service;

import com.edutech.common.dto.PaymentDTO;
import com.edutech.payment.entity.Payment;
import com.edutech.payment.mapper.PaymentMapper;
import com.edutech.payment.repository.PaymentRepository;
import com.edutech.payment.client.UserClient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.edutech.common.exception.ExceptionUtils.orThrow;
import static com.edutech.common.exception.ExceptionUtils.orThrowFeign;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepo;
    private final PaymentMapper paymentMapper;
    private final UserClient userClient;

    public List<PaymentDTO> findAll() {
        return paymentRepo.findAll().stream().map(paymentMapper::toDTO).toList();
    }

    public PaymentDTO findById(Integer id) {
        return paymentMapper.toDTO(orThrow(paymentRepo.findById(id), "Pago"));
    }

    public List<PaymentDTO> findByUserId(Integer userId) {
        return paymentRepo.findByUserId(userId).stream()
            .map(paymentMapper::toDTO)
            .collect(Collectors.toList());
    }

    public List<PaymentDTO> findByStatus(String status) {
        return paymentRepo.findByStatus(status).stream()
            .map(paymentMapper::toDTO)
            .collect(Collectors.toList());
    }

    public PaymentDTO create(PaymentDTO dto) {
        // Validar que el usuario existe
        orThrowFeign(dto.getUserId(), userClient::findById, "Usuario");

        return saveDTO(dto, null);
    }

    public PaymentDTO update(Integer id, PaymentDTO dto) {
        orThrow(paymentRepo.findById(id), "Pago");
        return saveDTO(dto, id);
    }

    public void delete(Integer id) {
        paymentRepo.delete(orThrow(paymentRepo.findById(id), "Pago"));
    }

    private PaymentDTO saveDTO(PaymentDTO dto, Integer id) {
        Payment entity = paymentMapper.toEntity(dto);
        if (id != null) {
            entity.setId(id);
        }
        return paymentMapper.toDTO(paymentRepo.save(entity));
    }
} 