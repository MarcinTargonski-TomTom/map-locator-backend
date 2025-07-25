package com.tomtom.locator.map.map_locator.mok;

import com.tomtom.locator.map.map_locator.mok.dto.NewAccountDto;
import com.tomtom.locator.map.map_locator.mok.model.Account;

interface AccountService {

    Account create(NewAccountDto dto);
}
