package com.codedifferently.tankofamerica.domain.account.repos;

import com.codedifferently.tankofamerica.domain.account.models.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepo extends CrudRepository<Account, Long> {
}
