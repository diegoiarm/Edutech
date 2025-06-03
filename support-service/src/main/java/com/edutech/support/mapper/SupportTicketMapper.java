package com.edutech.support.mapper;

import com.edutech.common.dto.SupportTicketDTO;
import com.edutech.support.entity.SupportTicket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SupportTicketMapper {
    @Mapping(target = "supportUserId", source = "supportUserId")
    SupportTicketDTO toDTO(SupportTicket entity);

    @Mapping(target = "supportUserId", source = "supportUserId")
    SupportTicket toEntity(SupportTicketDTO dto);
} 