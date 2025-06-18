package com.mcnz.spring.soaprest.Controllers;

import com.mcnz.jee.soap.*;
import com.mcnz.spring.soaprest.Models.UserEntity;
import com.mcnz.spring.soaprest.Repositories.UserRepository;
import com.mcnz.spring.soaprest.Security.CustomUserDetailsService;
import com.mcnz.spring.soaprest.Services.ClubService;
import com.mcnz.spring.soaprest.Services.RevokedTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
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
    private final CustomUserDetailsService customUserDetailsService;

    private ClubService clubService;
    private RevokedTokenService revokedTokenService;
    private UserRepository  userRepository;
    public ClubController(ClubService clubService, CustomUserDetailsService customUserDetailsService,
                          RevokedTokenService revokedTokenService,
                          UserRepository userRepository) {
        this.clubService = clubService;
        this.customUserDetailsService = customUserDetailsService;
        this.revokedTokenService = revokedTokenService;
        this.userRepository = userRepository;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getClubRequest")
    @ResponsePayload
    public GetClubResponse getClub(@RequestPayload GetClubRequest request) {
        Long id = request.getId();
        StatusMessage statusMessage = new StatusMessage();
        GetClubResponse response = new GetClubResponse();
        ClubDto clubDto = clubService.getClubById(id);

        statusMessage.setMessage("Club with id " + id + " found");
        statusMessage.setStatus("OK");
        response.setClubDto(clubDto);
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
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        CreateClubDto createClubDto = request.getCreateClubDto();

        if (createClubDto.getTitle().isEmpty() || createClubDto.getContent().isEmpty()) {
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title must not be empty");
        }
        CreateClubResponse response = new CreateClubResponse();
        StatusMessage statusMessage = new StatusMessage();

        ClubDto clubDto = clubService.createClub(createClubDto, currentUser);
        statusMessage.setMessage("Club created successfully with id " + clubDto.getId());
        statusMessage.setStatus("OK");
        response.setStatusMessage(statusMessage);
        response.setClubDto(clubDto);
        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateClubRequest")
    @ResponsePayload
    public UpdateClubResponse updateClub(@RequestPayload UpdateClubRequest request)  {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UpdateClubResponse response = new UpdateClubResponse();
        StatusMessage statusMessage = new StatusMessage();
        long id = request.getId();
        CreateClubDto updateClubDto = request.getCreateClubDto();

        ClubDto clubDto = clubService.updateClub(id, updateClubDto, currentUser);
        statusMessage.setMessage("Club updated successfully");
        statusMessage.setStatus("OK");
        response.setStatusMessage(statusMessage);
        response.setClubDto(clubDto);
        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteClubRequest")
    @ResponsePayload
    public DeleteClubResponse deleteClub(@RequestPayload DeleteClubRequest request)  {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        DeleteClubResponse response = new DeleteClubResponse();
        StatusMessage statusMessage = new StatusMessage();
        long id = request.getId();
        clubService.deleteClub(id, currentUser);
        statusMessage.setMessage("Club with id " + id + " deleted successfully");
        statusMessage.setStatus("OK");
        response.setStatusMessage(statusMessage);
        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteMyClubsRequest")
    @ResponsePayload
    public DeleteMyClubsResponse deleteClub(@RequestPayload DeleteMyClubsRequest request)  {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        DeleteMyClubsResponse response = new DeleteMyClubsResponse();
        StatusMessage statusMessage = new StatusMessage();
        clubService.deleteUserClubs(currentUser);
        statusMessage.setMessage("All clubs deleted successfully for user " + currentUser);
        statusMessage.setStatus("OK");
        response.setStatusMessage(statusMessage);
        return response;
    }








}
