package com.HeptaTrack.model.response;

import com.HeptaTrack.model.entity.Expense;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FarmerResponseDto implements Serializable {

    private static final long serialVersionUID = 32768732568274L;

    private String name;

    private String phoneNumber;

    private List<Expense> expenseList;

}
