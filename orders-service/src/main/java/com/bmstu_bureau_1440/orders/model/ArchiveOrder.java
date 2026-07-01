package com.bmstu_bureau_1440.orders.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@DiscriminatorValue(OrderTypes.ARCHIVE)

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class ArchiveOrder extends Order {

    @NonNull
    @Column(updatable = false)
    private LocalDate captureDate;

    public ArchiveOrder(String aoi, LocalDate captureDate) {
        super(aoi);
        this.captureDate = captureDate;
    }

}
