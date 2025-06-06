package com.mcnz.spring.soaprest.Controllers;

import com.mcnz.jee.soap.*;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class ClubController {



    @PayloadRoot(namespace = "http://soap.jee.mcnz.com/", localPart = "getClubRequest")
    @ResponsePayload
    public GetClubResponse getScore(@RequestPayload GetClubRequest request) {
        ClubDto club = new ClubDto();
        club.setId(1);
        club.setTitle("Spring Club");
        club.setContent("This is a club for Spring enthusiasts.");
        GetClubResponse response = new GetClubResponse();
        response.setClubDto(club);
        return response;
    }


}
