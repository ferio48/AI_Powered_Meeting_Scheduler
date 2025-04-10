package com.HeptaTrack.model.response;

import com.HeptaTrack.model.response.meta.CompanyResponseDto;
import com.HeptaTrack.model.response.meta.InsurancePolicyTypeResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InsuranceDataResponseDto {

    private Integer id;

    private CompanyResponseDto companyResponseDto;

    private String policyNumber;

    private InsurancePolicyTypeResponseDto insurancePolicyTypeResponseDto;

    private Date expiryDate;

    private String encodedData;

    private VehicleResponseDto vehicle;

}
