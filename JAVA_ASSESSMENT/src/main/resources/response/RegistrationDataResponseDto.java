package com.HeptaTrack.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDataResponseDto {

    private Integer id;

    private String registrationNumber;

    private Date registrationDate;

    private Date registrationExpiryDate;

    private String encodedData;

    private VehicleResponseDto vehicleResponseDto;

}
