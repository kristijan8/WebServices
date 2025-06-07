package com.mcnz.spring.soaprest.Services;

import com.mcnz.jee.soap.ClubDto;
import com.mcnz.jee.soap.CreateClubDto;
import com.mcnz.jee.soap.CreateClubRequest;
import com.mcnz.spring.soaprest.Dto.ClubDetailsDto;
import com.mcnz.spring.soaprest.Mappers.EventMapper;
import com.mcnz.spring.soaprest.Models.Club;
import com.mcnz.spring.soaprest.Mappers.ClubMapper;

import com.mcnz.spring.soaprest.Repositories.ClubRepository;
import com.mcnz.spring.soaprest.Repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static com.mcnz.spring.soaprest.Mappers.ClubMapper.toClub;
import static com.mcnz.spring.soaprest.Mappers.ClubMapper.toClubDto;


@Service
public class ClubServiceImpl implements ClubService {

    private final EventRepository eventRepository;
    private ClubRepository clubRepository;

    public ClubServiceImpl(ClubRepository clubRepository, EventRepository eventRepository) {
        this.clubRepository = clubRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<ClubDto> getAllClubs() {
        return clubRepository.findAll().stream().map(c -> toClubDto(c)).toList();
    }

    @Override
    public Optional<ClubDto> getClubById(long id) {
        return clubRepository.findFirstById(id).map(c -> toClubDto(c));
    }

    @Override
    public ClubDto createClub(CreateClubDto club) {
        Club newClub = toClub(club);
        Club savedClub = clubRepository.save(newClub);
        return toClubDto(savedClub);
    }

    @Override
    public ClubDto updateClub(long id, CreateClubDto clubDto) {
        Club clubToUpdate = clubRepository.findFirstById(id).orElseThrow();
        if (!clubDto.getTitle().isEmpty()) {
            clubToUpdate.setTitle(clubDto.getTitle());
        }
        if (!clubDto.getContent().isEmpty()) {
            clubToUpdate.setContent(clubDto.getContent());
        }
        Club updatedClub = clubRepository.save(clubToUpdate);
        return toClubDto(updatedClub);
    }

    @Override
    public void deleteClub(long id) {
        clubRepository.deleteById(id);
    }

    @Override
    public boolean exists(String title) {
        return clubRepository.findByTitle(title).isPresent();
    }

    @Override
    public boolean exists(long id) {
        return  clubRepository.findFirstById(id).isPresent();
    }

    @Override
    public ClubDetailsDto getClubDetails(Long clubId) {
        ClubDetailsDto clubDetailsDto = new ClubDetailsDto();
        Club club = clubRepository.findFirstById(clubId).orElseThrow(()  -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club Not Found"));
        clubDetailsDto.setId(club.getId());
        clubDetailsDto.setTitle(club.getTitle());
        clubDetailsDto.setContent(club.getContent());
        eventRepository.findAll().stream().filter(e -> e.getClub().getId() == clubId)
                .forEach(event -> clubDetailsDto.getEvents().add(EventMapper.toEventDto(event)));
        return clubDetailsDto;
    }

}
