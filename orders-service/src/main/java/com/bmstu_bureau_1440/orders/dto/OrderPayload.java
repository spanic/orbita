package com.bmstu_bureau_1440.orders.dto;

public sealed interface OrderPayload permits ArchivePayload, TaskingPayload, MonitoringPayload {
}
