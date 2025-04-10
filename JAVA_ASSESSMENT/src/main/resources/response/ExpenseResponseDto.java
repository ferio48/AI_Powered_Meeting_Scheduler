package com.HeptaTrack.model.response;

import com.HeptaTrack.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponseDto {

    private ExpenseTypeResponseDto expenseTypeResponseDto;

    private String price;

    private String description;

    private FarmerResponseDto farmerResponseDto;

    private PaymentType paymentType;

}
