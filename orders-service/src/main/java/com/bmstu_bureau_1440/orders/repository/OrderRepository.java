package com.bmstu_bureau_1440.orders.repository;

import com.bmstu_bureau_1440.orders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
