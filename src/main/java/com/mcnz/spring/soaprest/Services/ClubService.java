package com.mcnz.spring.soaprest.Services;

import com.mcnz.jee.soap.ClubDto;
import com.mcnz.jee.soap.CreateClubDto;
import com.mcnz.spring.soaprest.Dto.ClubDetailsDto;

import java.util.List;
import java.util.Optional;

public interface ClubService {

    List<ClubDto> getAllClubs();
    ClubDto getClubById(long id);
    ClubDto createClub(CreateClubDto club, String username);
    ClubDto updateClub(long id, CreateClubDto clubDto, String username);
    void deleteClub(long id, String username);
    boolean exists(String title);
    boolean exists(long id);
    ClubDetailsDto getClubDetails(Long clubId);
    public void deleteUserClubs(String username);

}
