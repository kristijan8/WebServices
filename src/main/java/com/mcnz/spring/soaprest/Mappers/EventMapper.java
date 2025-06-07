package com.mcnz.spring.soaprest.Mappers;

import com.mcnz.spring.soaprest.Dto.CreateEventDto;
import com.mcnz.spring.soaprest.Dto.EventDetaildDto;
import com.mcnz.spring.soaprest.Dto.EventDto;
import com.mcnz.spring.soaprest.Models.Club;
import com.mcnz.spring.soaprest.Models.Event;

public class EventMapper {
    public static EventDto toEventDto (Event event) {
        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .type(event.getType())
                .clubId(event.getClub().getId())
                .build();
    }
    public static Event toEvent(EventDto eventDto, Club club) {
        return Event.builder()
                .id(eventDto.getId())
                .name(eventDto.getName())
                .startTime(eventDto.getStartTime())
                .endTime(eventDto.getEndTime())
                .type(eventDto.getType())
                .club(club)
                .build();
    }
    public static Event toEvent(CreateEventDto eventDto, Club club) {
        return  Event.builder()
                .name(eventDto.getName())
                .startTime(eventDto.getStartTime())
                .endTime(eventDto.getEndTime())
                .type(eventDto.getType())
                .club(club)
                .build();
    }
    public static EventDetaildDto toEventDetailsDto(Event event) {
        return EventDetaildDto.builder()
                .id(event.getId())
                .name(event.getName())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .type(event.getType())
                .club(ClubMapper.toClubDto(event.getClub()))
                .build();
    }

}
