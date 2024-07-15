package com.example.happyfarmer.Repositories;

import com.example.happyfarmer.Models.Account;
import org.springframework.data.repository.CrudRepository;

public interface DepotRepository extends CrudRepository<Account, Integer> {
    Account findDepotByUserId(long id);
    boolean existsByUserId(long id);
}
