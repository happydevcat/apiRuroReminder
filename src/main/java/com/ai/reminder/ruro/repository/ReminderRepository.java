package com.ai.reminder.ruro.repository;

import com.ai.reminder.ruro.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByReminderList_Id(Long listId);
    List<Reminder> findByIsFlaggedTrueAndIsCompletedFalse();
    List<Reminder> findByIsCompletedTrue();
    List<Reminder> findByDueDateBetweenAndIsCompletedFalse(LocalDateTime start, LocalDateTime end);
    List<Reminder> findByDueDateIsNotNullAndIsCompletedFalse();
}
