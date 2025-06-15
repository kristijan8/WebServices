package com.mcnz.spring.soaprest.Controllers;
import com.mcnz.spring.soaprest.Dto.*;
import com.mcnz.spring.soaprest.Services.ClubService;
import com.mcnz.spring.soaprest.Services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/rest/event")
public class EventController {
    private EventService eventService;
    private ClubService clubService;

    @Autowired
    public EventController(EventService eventService, ClubService clubService) {
        this.eventService = eventService;
        this.clubService = clubService;
    }

    @GetMapping("/getAllEvents")
    public List<EventDto> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/getEvent/{eventId}")
    public EventDetaildDto getEvent(@PathVariable("eventId") Long id) {
        return eventService.getEvent(id);
    }

    @PostMapping("/createEvent")
    public EventDto createEvent(@RequestBody CreateEventDto eventDto) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return eventService.createEvent( eventDto, currentUser);
    }

    @PostMapping("/updateEvent/{eventId}")
    public EventDto updateEvent(@PathVariable("eventId") Long id, @RequestBody UpdateEventDto eventDto) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return eventService.updateEvent(id, eventDto, currentUser);
    }

    @GetMapping("/getClubEvents/{clubId}")
    public ClubDetailsDto getClubEvents(@PathVariable("clubId") Long clubId) {
        return clubService.getClubDetails(clubId);
    }

    @DeleteMapping("/deleteEvent/{eventId}")
    public void deleteEvent(@PathVariable("eventId") Long id) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        eventService.deleteEvent(id, currentUser);
    }




}
