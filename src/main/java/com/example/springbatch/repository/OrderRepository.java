package com.example.springbatch.repository;

import com.example.springbatch.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Integer> {


}