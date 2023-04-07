package com.yakushkin.transaction_app.repository;

import com.yakushkin.transaction_app.dto.AccountInfoDto;
import com.yakushkin.transaction_app.entity.Account;

import java.util.List;

public interface FilterAccountRepository {

    List<AccountInfoDto> findAllAccountsByUserId(Integer userId);
}
