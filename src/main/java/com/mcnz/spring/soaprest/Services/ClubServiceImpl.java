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
import com.mcnz.spring.soaprest.Repositories.UserRepository;
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
    private final UserRepository userRepository;

    public ClubServiceImpl(ClubRepository clubRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.clubRepository = clubRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ClubDto> getAllClubs() {
        return clubRepository.findAll().stream().map(c -> toClubDto(c)).toList();
    }

    @Override
    public ClubDto getClubById(long id) {
        return clubRepository.findFirstById(id).map(c -> toClubDto(c)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club Not Found"));
    }

    @Override
    public ClubDto createClub(CreateClubDto club, String username) {
        if (clubRepository.findByTitle(club.getTitle()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Club with this title already exists");
        }
        Club newClub = toClub(club);
        newClub.setCreator(userRepository.findByUsername(username).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found")));
        Club savedClub = clubRepository.save(newClub);
        return toClubDto(savedClub);
    }

    @Override
    public ClubDto updateClub(long id, CreateClubDto clubDto, String username) {
        Club clubToUpdate = clubRepository.findFirstById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club Not Found"));
        if (!checkAdmin(username) && !clubToUpdate.getCreator().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not allowed to update this club");
        }
        if (exists(clubDto.getTitle())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Club with this title already exists");
        }
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
    public void deleteClub(long id, String username) {
        Club clubToDelete = clubRepository.findFirstById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club Not Found"));
        if (!checkAdmin(username) && !clubToDelete.getCreator().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not allowed to delete this club");
        }
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


    public boolean checkAdmin(String username) {
        return userRepository.findByUsername(username)
                .map(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN")))
                .orElse(false);
    }

}
