package com.mcnz.spring.soaprest.Repositories;

import com.mcnz.spring.soaprest.Models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    public Event findByName(String name);
    public Event findByType(String type);
    public List<Event> findAll();
    public List<Event> findByClubId(Long clubId);
    public Event findFirstById(long id);

}
