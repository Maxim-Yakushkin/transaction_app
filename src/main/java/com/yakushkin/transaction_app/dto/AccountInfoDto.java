package com.yakushkin.transaction_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountInfoDto {

    private Integer id;
    private Integer balance;
    private String currency;
}
