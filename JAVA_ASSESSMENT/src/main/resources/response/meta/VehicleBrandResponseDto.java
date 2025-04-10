package com.HeptaTrack.model.response.meta;

import com.HeptaTrack.model.response.VehicleResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleBrandResponseDto {

    private Integer id;

    private String name;

    private String description;

    private List<VehicleResponseDto> vehicleResponseDtoList;

}
