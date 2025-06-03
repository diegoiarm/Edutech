package com.edutech.classroom.service;

import com.edutech.classroom.dto.SupportTicketDTO;
import com.edutech.classroom.dto.SupportTicketResponseDTO;
import com.edutech.classroom.entity.SupportTicket;
import com.edutech.classroom.entity.SupportTicketResponse;
import com.edutech.classroom.entity.User;
import com.edutech.classroom.exception.ResourceNotFoundException;
import com.edutech.classroom.exception.ValidationException;
import com.edutech.classroom.repository.SupportTicketRepository;
import com.edutech.classroom.repository.SupportTicketResponseRepository;
import com.edutech.classroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportService {
    private final SupportTicketRepository ticketRepository;
    private final SupportTicketResponseRepository responseRepository;
    private final UserRepository userRepository;

    // Métodos para tickets de soporte
    public List<SupportTicketDTO> findAllTickets() {
        return ticketRepository.findAll().stream()
                .map(SupportTicketDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public SupportTicketDTO findTicketById(Integer id) {
        return ticketRepository.findById(id)
                .map(SupportTicketDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado"));
    }

    public List<SupportTicketDTO> findTicketsByUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }
        return ticketRepository.findByUserId(userId).stream()
                .map(SupportTicketDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<SupportTicketDTO> findTicketsBySupportUser(Integer supportUserId) {
        if (!userRepository.existsById(supportUserId)) {
            throw new ResourceNotFoundException("Usuario de soporte no encontrado");
        }
        return ticketRepository.findBySupportUserId(supportUserId).stream()
                .map(SupportTicketDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<SupportTicketDTO> findTicketsByStatus(String status) {
        return ticketRepository.findByStatus(status).stream()
                .map(SupportTicketDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public SupportTicketDTO createTicket(SupportTicketDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!user.getIsActive()) {
            throw new ValidationException("El usuario no está activo");
        }

        SupportTicket ticket = dto.toEntity();
        ticket.setUser(user);
        ticket.setStatus("ABIERTO");
        ticket.setCreatedAt(Instant.now());

        return SupportTicketDTO.fromEntity(ticketRepository.save(ticket));
    }

    @Transactional
    public SupportTicketDTO assignSupportUser(Integer ticketId, Integer supportUserId) {
        SupportTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado"));

        User supportUser = userRepository.findById(supportUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario de soporte no encontrado"));

        if (!supportUser.getIsActive()) {
            throw new ValidationException("El usuario de soporte no está activo");
        }

        if (!supportUser.getRole().getName().equals("SUPPORT")) {
            throw new ValidationException("El usuario asignado no tiene rol de soporte");
        }

        ticket.setSupportUser(supportUser);
        ticket.setStatus("ASIGNADO");

        return SupportTicketDTO.fromEntity(ticketRepository.save(ticket));
    }

    @Transactional
    public SupportTicketDTO updateTicketStatus(Integer ticketId, String status) {
        SupportTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado"));

        if (!List.of("ABIERTO", "ASIGNADO", "EN_PROCESO", "RESUELTO", "CERRADO").contains(status)) {
            throw new ValidationException("Estado de ticket inválido");
        }

        ticket.setStatus(status);
        if (status.equals("CERRADO")) {
            ticket.setClosedAt(Instant.now());
        }

        return SupportTicketDTO.fromEntity(ticketRepository.save(ticket));
    }

    // Métodos para respuestas de tickets
    public List<SupportTicketResponseDTO> findResponsesByTicket(Integer ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new ResourceNotFoundException("Ticket no encontrado");
        }
        return responseRepository.findByTicketIdOrderByCreatedAtAsc(ticketId).stream()
                .map(SupportTicketResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<SupportTicketResponseDTO> findResponsesByUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }
        return responseRepository.findByUserId(userId).stream()
                .map(SupportTicketResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public SupportTicketResponseDTO createResponse(SupportTicketResponseDTO dto) {
        SupportTicket ticket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!user.getIsActive()) {
            throw new ValidationException("El usuario no está activo");
        }

        // Validar que el usuario sea el creador del ticket o un usuario de soporte
        if (!ticket.getUser().getId().equals(user.getId()) && 
            !user.getRole().equals("SUPPORT")) {
            throw new ValidationException("El usuario no tiene permiso para responder este ticket");
        }

        // Si el ticket está cerrado, no se pueden agregar más respuestas
        if (ticket.getStatus().equals("CERRADO")) {
            throw new ValidationException("No se pueden agregar respuestas a un ticket cerrado");
        }

        // Si es un usuario de soporte respondiendo, actualizar el estado del ticket
        if (user.getRole().equals("SUPPORT")) {
            ticket.setStatus("EN_PROCESO");
            ticketRepository.save(ticket);
        }

        SupportTicketResponse response = dto.toEntity();
        response.setTicket(ticket);
        response.setUser(user);
        response.setCreatedAt(Instant.now());

        return SupportTicketResponseDTO.fromEntity(responseRepository.save(response));
    }

    @Transactional
    public void deleteResponse(Integer responseId) {
        SupportTicketResponse response = responseRepository.findById(responseId)
                .orElseThrow(() -> new ResourceNotFoundException("Respuesta no encontrada"));

        // Solo permitir eliminar respuestas propias o si es usuario de soporte
        User currentUser = response.getUser();
        if (!currentUser.getRole().equals("SUPPORT")) {
            throw new ValidationException("No tiene permiso para eliminar esta respuesta");
        }

        responseRepository.delete(response);
    }
} 