package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("FROM Account WHERE username = :username")
    Account accountExists(@Param("username") String name);

    @Query("FROM Account WHERE username = :username AND password = :password")
    Account accountLogin(@Param("username") String name, @Param("password") String password);

    @Query("FROM Account WHERE account_id = :id")
    Account accountIdExists(@Param("id") int id);
}
