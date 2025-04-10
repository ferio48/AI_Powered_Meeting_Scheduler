package com.HeptaTrack.model.response;

import com.HeptaTrack.model.entity.meta.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PollutionDataResponseDto {

    private Integer id;

    private String certificateNumber;

    private Date expiryDate;

    private Company company;

    private String encodedData;

    private VehicleResponseDto vehicleResponseDto;
}
