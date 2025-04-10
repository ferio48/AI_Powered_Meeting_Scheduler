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
public class TaxDataResponseDto {

    private Integer id;

    private String documentNumber;

    private String chassisNumber;

    private String vehicleClass;

    private Date expiryDate;

    private String encodedData;

    private VehicleResponseDto vehicleResponseDto;

}
