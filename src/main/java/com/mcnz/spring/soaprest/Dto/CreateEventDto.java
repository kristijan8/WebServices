package com.mcnz.spring.soaprest.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventDto {
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String type;
    private Long clubId;
}
