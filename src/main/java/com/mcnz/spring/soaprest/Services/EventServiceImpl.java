package com.mcnz.spring.soaprest.Services;

import com.mcnz.spring.soaprest.Dto.CreateEventDto;
import com.mcnz.spring.soaprest.Dto.EventDetaildDto;
import com.mcnz.spring.soaprest.Dto.EventDto;
import com.mcnz.spring.soaprest.Dto.UpdateEventDto;
import com.mcnz.spring.soaprest.Models.Club;
import com.mcnz.spring.soaprest.Mappers.EventMapper;

import com.mcnz.spring.soaprest.Models.Event;
import com.mcnz.spring.soaprest.Repositories.ClubRepository;
import com.mcnz.spring.soaprest.Repositories.EventRepository;
import com.mcnz.spring.soaprest.Repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.mcnz.spring.soaprest.Mappers.EventMapper.toEvent;

@Service
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;
    private ClubRepository clubRepository;
    private UserRepository userRepository;
    public EventServiceImpl(EventRepository eventRepository, ClubRepository clubRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
    }



    @Override
    public EventDto createEvent(CreateEventDto eventDto, String username) {
        if (eventDto.getClubId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Club id is required");
        }
        if (clubRepository.findAll().stream().noneMatch(c -> c.getId() == eventDto.getClubId()))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Club Not Found");
        }
        Club club = clubRepository.findById(eventDto.getClubId()).get();
        if(!checkAdmin(username) && !club.getCreator().getUsername().equals(username))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not allowed to create events for this club");
        }

        Event event = toEvent(eventDto, club);
        event.setClub(club);
        eventRepository.save(event);
        return EventMapper.toEventDto(event);
    }

    @Override
    public EventDto updateEvent(Long id, UpdateEventDto eventDto, String username) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event Not Found"));
        if (!checkAdmin(username) && !event.getClub().getCreator().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not allowed to update events for this club");
        }
        if (!eventDto.getName().isEmpty()) {
            event.setName(eventDto.getName());
        }
        if (eventDto.getStartTime() != null) {
            event.setStartTime(eventDto.getStartTime());
        }
        if (eventDto.getEndTime() != null) {
            event.setEndTime(eventDto.getEndTime());
        }
        if (eventDto.getType() != null) {
            event.setType(eventDto.getType());
        }
        eventRepository.save(event);
        return EventMapper.toEventDto(event);
    }

    @Override
    public void deleteEvent(Long id, String username) {

        if (eventRepository.existsById(id)) {
            if (!checkAdmin(username) && !eventRepository.findById(id).get().getClub().getCreator().getUsername().equals(username)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not allowed to delete events for this club");
            }
            eventRepository.deleteById(id);
        }
    }

    @Override
    public EventDetaildDto getEvent(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event Not Found"));
        EventDetaildDto eventDetaildDto = EventMapper.toEventDetailsDto(event);
        return eventDetaildDto;
    }

    @Override
    public List<EventDto> getAllEvents() {
        return eventRepository.findAll().stream().map(EventMapper::toEventDto).toList();
    }

    @Override
    public List<EventDto> getEventsByClubId(Long clubId) {
        return eventRepository.findByClubId(clubId).stream().map(EventMapper::toEventDto).toList();
    }


    public boolean exists(Long id) {
        return eventRepository.existsById(id);
    }

    public boolean checkAdmin(String username) {
        return userRepository.findByUsername(username)
                .map(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN")))
                .orElse(false);
    }

}
