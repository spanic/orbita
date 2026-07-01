package com.bmstu_bureau_1440.orders.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@DiscriminatorValue(OrderTypes.MONITORING)

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class MonitoringOrder extends Order {

    @NonNull
    @Column(updatable = false)
    private String cadence;

    public MonitoringOrder(String aoi, String cadence) {
        super(aoi);
        this.cadence = cadence;
    }

}
