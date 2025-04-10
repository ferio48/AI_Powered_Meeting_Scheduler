package com.HeptaTrack.model.response;

import com.HeptaTrack.model.response.meta.VehicleBrandResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponseDto {

    private Integer id;

    private Date makingDate;

    private VehicleBrandResponseDto vehicleBrandResponseDto;

    private String vehicleModelNumber;

    private Year vehicleMakingYear;

    private RegistrationDataResponseDto registrationDataResponseDto;

    private InsuranceDataResponseDto insuranceDataResponseDto;

    private PollutionDataResponseDto pollutionDataResponseDto;

    private TaxDataResponseDto taxDataResponseDto;

    private UserResponseDto userResponseDto;

}
