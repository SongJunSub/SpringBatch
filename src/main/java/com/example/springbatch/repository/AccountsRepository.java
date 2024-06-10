package com.example.springbatch.repository;

import com.example.springbatch.domain.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsRepository extends JpaRepository<Accounts, Integer> {



}