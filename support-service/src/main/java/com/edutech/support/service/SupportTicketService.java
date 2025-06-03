package com.edutech.support.service;

import com.edutech.common.dto.SupportTicketDTO;
import com.edutech.support.entity.SupportTicket;
import com.edutech.support.mapper.SupportTicketMapper;
import com.edutech.support.repository.SupportTicketRepository;
import com.edutech.support.client.UserClient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static com.edutech.common.exception.ExceptionUtils.orThrow;
import static com.edutech.common.exception.ExceptionUtils.orThrowFeign;

@Service
@RequiredArgsConstructor
public class SupportTicketService {

    private final SupportTicketRepository ticketRepo;
    private final SupportTicketMapper ticketMapper;
    private final UserClient userClient;

    public List<SupportTicketDTO> findAll() {
        return ticketRepo.findAll().stream().map(ticketMapper::toDTO).toList();
    }

    public SupportTicketDTO findById(Integer id) {
        return ticketMapper.toDTO(orThrow(ticketRepo.findById(id), "Ticket"));
    }

    public List<SupportTicketDTO> findByUserId(Integer userId) {
        return ticketRepo.findByUserId(userId).stream()
            .map(ticketMapper::toDTO)
            .toList();
    }

    public List<SupportTicketDTO> findByStatus(String status) {
        return ticketRepo.findByStatus(status).stream()
            .map(ticketMapper::toDTO)
            .toList();
    }

    public SupportTicketDTO create(SupportTicketDTO dto) {
        // Validar que el usuario existe
        orThrowFeign(dto.getUserId(), userClient::findById, "Usuario");
        return saveDTO(dto, null);
    }

    public SupportTicketDTO update(Integer id, SupportTicketDTO dto) {
        orThrow(ticketRepo.findById(id), "Ticket");
        return saveDTO(dto, id);
    }

    public void delete(Integer id) {
        ticketRepo.delete(orThrow(ticketRepo.findById(id), "Ticket"));
    }

    public SupportTicketDTO resolve(Integer id) {
        SupportTicket ticket = orThrow(ticketRepo.findById(id), "Ticket");
        ticket.setStatus("RESUELTO");
        ticket.setClosedAt(Instant.now());
        return ticketMapper.toDTO(ticketRepo.save(ticket));
    }

    private SupportTicketDTO saveDTO(SupportTicketDTO dto, Integer id) {
        SupportTicket entity = ticketMapper.toEntity(dto);
        if (id != null) {
            entity.setId(id);
        }
        return ticketMapper.toDTO(ticketRepo.save(entity));
    }
} 