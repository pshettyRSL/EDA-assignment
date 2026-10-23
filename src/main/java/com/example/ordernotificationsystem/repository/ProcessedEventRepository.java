package com.example.ordernotificationsystem.repository;

import com.example.ordernotificationsystem.entity.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, String> {
}
