package com.HeptaTrack.model.response.meta;

import com.HeptaTrack.model.response.InsuranceDataResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InsurancePolicyTypeResponseDto {

    private Integer id;

    private String name;

    private String description;

    private List<InsuranceDataResponseDto> insuranceDataResponseDtoList;

}
