package com.mcnz.spring.soaprest.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubDetailsDto {
    private Long id;
    private String title;
    private String content;
    List<EventDto> events = new ArrayList<>();
}
