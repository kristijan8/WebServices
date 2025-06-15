package com.mcnz.spring.soaprest.Mappers;

import com.mcnz.jee.soap.ClubDto;
import com.mcnz.jee.soap.CreateClubDto;
import com.mcnz.spring.soaprest.Models.Club;

public class ClubMapper {
    public static ClubDto toClubDto (Club club) {
        ClubDto clubDto = new ClubDto();
        clubDto.setId(club.getId());
        clubDto.setTitle(club.getTitle());
        clubDto.setContent(club.getContent());
        clubDto.setCreator(club.getCreator().getUsername());
        return clubDto;
    }
    public static Club toClub (CreateClubDto clubDto) {
        Club club = new Club();
        club.setTitle(clubDto.getTitle());
        club.setContent(clubDto.getContent());
        return club;
    }
}
