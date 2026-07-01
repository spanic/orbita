package com.bmstu_bureau_1440.orders;

import static org.instancio.Select.all;
import static org.instancio.Select.field;

import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Selector;

import com.bmstu_bureau_1440.orders.model.ArchiveOrder;
import com.bmstu_bureau_1440.orders.model.MonitoringOrder;
import com.bmstu_bureau_1440.orders.model.Order;
import com.bmstu_bureau_1440.orders.model.TaskingOrder;

public final class OrderTestsFixtures {

    private static final Selector[] AUTO_GENERATED_FIELDS = {
            field(Order::getId), field(Order::getCreatedAt)
    };

    public static final Model<TaskingOrder> TASKING_ORDER_MODEL = Instancio.of(TaskingOrder.class)
            .ignore(all(AUTO_GENERATED_FIELDS)).toModel();

    public static final Model<MonitoringOrder> MONITORING_ORDER_MODEL = Instancio.of(MonitoringOrder.class)
            .ignore(all(AUTO_GENERATED_FIELDS)).toModel();

    public static final Model<ArchiveOrder> ARCHIVE_ORDER_MODEL = Instancio.of(ArchiveOrder.class)
            .ignore(all(AUTO_GENERATED_FIELDS)).toModel();

}
