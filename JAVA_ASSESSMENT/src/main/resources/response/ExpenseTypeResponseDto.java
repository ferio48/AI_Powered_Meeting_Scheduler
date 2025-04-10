package com.HeptaTrack.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseTypeResponseDto {

    private String typeName;

    private String typeDescription;

    private List<ExpenseResponseDto> expenseResponseDtoList;

}
