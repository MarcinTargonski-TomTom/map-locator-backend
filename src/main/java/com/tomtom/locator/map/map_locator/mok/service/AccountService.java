package com.tomtom.locator.map.map_locator.mok.service;

import com.tomtom.locator.map.map_locator.mok.dto.NewAccountDto;
import com.tomtom.locator.map.map_locator.model.Account;
import lombok.NonNull;

public interface AccountService {

    Account create(@NonNull NewAccountDto dto);
}
