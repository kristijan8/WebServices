package com.mcnz.spring.soaprest.Controllers;

import com.mcnz.jee.soap.*;
import com.mcnz.spring.soaprest.Services.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;


//http://localhost:8080/ws/club.wsdl

@Endpoint
public class ClubController {
    private static final String NAMESPACE_URI = "http://soap.jee.mcnz.com/";

    private ClubService clubService;
    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getClubRequest")
    @ResponsePayload
    public GetClubResponse getClub(@RequestPayload GetClubRequest request) {
        Long id = request.getId();
        StatusMessage statusMessage = new StatusMessage();
        GetClubResponse response = new GetClubResponse();
        Optional<ClubDto> clubDto = clubService.getClubById(id);
        if (clubDto.isEmpty()) {
            statusMessage.setMessage("Club with id " + id + " not found");
            response.setClubDto(null);
            statusMessage.setStatus("ERROR");
        }
        else
        {
            statusMessage.setMessage("Club with id " + id + " found");
            statusMessage.setStatus("OK");
            response.setClubDto(clubDto.get());
        }
        response.setStatusMessage(statusMessage);
        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllClubsRequest")
    @ResponsePayload
    public GetAllClubsResponse getAllClubs(@RequestPayload GetAllClubsRequest request)  {
        GetAllClubsResponse response = new GetAllClubsResponse();
        clubService.getAllClubs().stream().forEach(c -> response.getClubDtos().add(c));
        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setMessage("All clubs retrieved successfully");
        statusMessage.setStatus("OK");
        response.setStatusMessage(statusMessage);
        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createClubRequest")
    @ResponsePayload
    public CreateClubResponse createClub(@RequestPayload CreateClubRequest request)  {
        CreateClubResponse response = new CreateClubResponse();
        StatusMessage statusMessage = new StatusMessage();

        CreateClubDto createClubDto = request.getCreateClubDto();
        if (createClubDto.getTitle().isEmpty() || createClubDto.getContent().isEmpty()) {

            statusMessage.setMessage("Title and content must not be empty");
            statusMessage.setStatus("ERROR");
            response.setClubDto(null);
            response.setStatusMessage(statusMessage);
            return response;
        }

        if (clubService.exists(createClubDto.getTitle()) ) {
            statusMessage.setMessage("Club with title " + createClubDto.getTitle() + " already exists");
            statusMessage.setStatus("ERROR");
            response.setClubDto(null);
            response.setStatusMessage(statusMessage);
            return response;
        }

        ClubDto clubDto = clubService.createClub(createClubDto);
        statusMessage.setMessage("Club craeted successfully with id " + clubDto.getId());
        statusMessage.setStatus("OK");
        response.setStatusMessage(statusMessage);
        response.setClubDto(clubDto);
        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateClubRequest")
    @ResponsePayload
    public UpdateClubResponse updateClub(@RequestPayload UpdateClubRequest request)  {
        UpdateClubResponse response = new UpdateClubResponse();
        StatusMessage statusMessage = new StatusMessage();

        long id = request.getId();
        CreateClubDto updateClubDto = request.getCreateClubDto();
        if(!clubService.exists(id)) {
            statusMessage.setMessage("Club with id "+id + " does not exists");
            statusMessage.setStatus("ERROR");
            response.setClubDto(null);
            response.setStatusMessage(statusMessage);
            return response;
        }

        if (clubService.exists(updateClubDto.getTitle()) ) {
            statusMessage.setMessage("Club with title " + updateClubDto.getTitle() + " already exists");
            statusMessage.setStatus("ERROR");
            response.setClubDto(null);
            response.setStatusMessage(statusMessage);
            return response;
        }

        ClubDto clubDto = clubService.updateClub(id, updateClubDto);
        statusMessage.setMessage("Club updated successfully");
        statusMessage.setStatus("OK");
        response.setStatusMessage(statusMessage);
        response.setClubDto(clubDto);
        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteClubRequest")
    @ResponsePayload
    public DeleteClubResponse deleteClub(@RequestPayload DeleteClubRequest request)  {
        DeleteClubResponse response = new DeleteClubResponse();
        StatusMessage statusMessage = new StatusMessage();

        long id = request.getId();
        if(!clubService.exists(id)) {
            statusMessage.setMessage("Club with id "+id + " does not exists");
            statusMessage.setStatus("ERROR");
            response.setStatusMessage(statusMessage);
            return response;
        }
        clubService.deleteClub(id);
        statusMessage.setMessage("Club with id " + id + " deleted successfully");
        statusMessage.setStatus("OK");
        response.setStatusMessage(statusMessage);
        return response;
    }


}
