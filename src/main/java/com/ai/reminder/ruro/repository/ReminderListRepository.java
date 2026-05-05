package com.ai.reminder.ruro.repository;

import com.ai.reminder.ruro.entity.ReminderList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReminderListRepository extends JpaRepository<ReminderList, Long> {
    boolean existsByName(String name);
    Optional<ReminderList> findByName(String name);
}
