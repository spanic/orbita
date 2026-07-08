package com.bmstu_bureau_1440.shared.outbox;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "outbox_events")
@EntityListeners(AuditingEntityListener.class)

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NonNull
    @Column(nullable = false, updatable = false)
    private UUID aggregateId;

    @NonNull
    @Column(nullable = false, updatable = false)
    private String eventType;

    @NonNull
    @Column(nullable = false, updatable = false)
    private String topic;

    @NonNull
    @Column(nullable = false, updatable = false, columnDefinition = "text")
    private String payload;

    @Column
    private Instant publishedAt;

    public void markPublished(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

}
