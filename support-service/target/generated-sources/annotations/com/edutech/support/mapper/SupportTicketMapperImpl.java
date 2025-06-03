package com.edutech.support.mapper;

import com.edutech.common.dto.SupportTicketDTO;
import com.edutech.support.entity.SupportTicket;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-03T01:55:25-0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class SupportTicketMapperImpl implements SupportTicketMapper {

    @Override
    public SupportTicketDTO toDTO(SupportTicket entity) {
        if ( entity == null ) {
            return null;
        }

        SupportTicketDTO supportTicketDTO = new SupportTicketDTO();

        supportTicketDTO.setSupportUserId( entity.getSupportUserId() );
        supportTicketDTO.setId( entity.getId() );
        supportTicketDTO.setUserId( entity.getUserId() );
        supportTicketDTO.setSubject( entity.getSubject() );
        supportTicketDTO.setDescription( entity.getDescription() );
        supportTicketDTO.setStatus( entity.getStatus() );
        supportTicketDTO.setCreatedAt( entity.getCreatedAt() );
        supportTicketDTO.setClosedAt( entity.getClosedAt() );

        return supportTicketDTO;
    }

    @Override
    public SupportTicket toEntity(SupportTicketDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SupportTicket supportTicket = new SupportTicket();

        supportTicket.setSupportUserId( dto.getSupportUserId() );
        supportTicket.setId( dto.getId() );
        supportTicket.setUserId( dto.getUserId() );
        supportTicket.setSubject( dto.getSubject() );
        supportTicket.setDescription( dto.getDescription() );
        supportTicket.setStatus( dto.getStatus() );
        supportTicket.setCreatedAt( dto.getCreatedAt() );
        supportTicket.setClosedAt( dto.getClosedAt() );

        return supportTicket;
    }
}
