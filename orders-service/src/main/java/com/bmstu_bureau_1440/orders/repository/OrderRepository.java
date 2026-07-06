package com.bmstu_bureau_1440.orders.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bmstu_bureau_1440.orders.model.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
