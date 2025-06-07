package com.mcnz.spring.soaprest.Services;

import com.mcnz.spring.soaprest.Dto.CreateEventDto;
import com.mcnz.spring.soaprest.Dto.EventDetaildDto;
import com.mcnz.spring.soaprest.Dto.EventDto;
import com.mcnz.spring.soaprest.Dto.UpdateEventDto;

import java.util.List;
import java.util.Optional;

public interface EventService {
    EventDto createEvent( CreateEventDto eventDto);

    EventDto updateEvent(Long id, UpdateEventDto eventDto);

    void deleteEvent(Long id);

    EventDetaildDto getEvent(Long id);

    List<EventDto> getAllEvents();

    List<EventDto> getEventsByClubId(Long clubId);

    boolean exists(Long id);



}
