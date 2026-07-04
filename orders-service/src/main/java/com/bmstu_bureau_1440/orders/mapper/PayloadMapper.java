package com.bmstu_bureau_1440.orders.mapper;

import com.bmstu_bureau_1440.orders.dto.OrderPayload;
import com.bmstu_bureau_1440.orders.model.Order;

public interface PayloadMapper<T extends OrderPayload> {

    Order map(T payload);

}
