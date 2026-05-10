package com.ai.reminder.ruro.repository;

import com.ai.reminder.ruro.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    /** 특정 리스트에 속한 모든 리마인더를 조회합니다. */
    List<Reminder> findByReminderList_Id(Long listId);

    /** 플래그가 설정되고 아직 완료되지 않은 리마인더를 조회합니다. */
    List<Reminder> findByIsFlaggedTrueAndIsCompletedFalse();

    /** 완료된 모든 리마인더를 조회합니다. */
    List<Reminder> findByIsCompletedTrue();

    /** 지정된 날짜 범위 내 마감인 미완료 리마인더를 조회합니다. */
    List<Reminder> findByDueDateBetweenAndIsCompletedFalse(LocalDateTime start, LocalDateTime end);

    /** 마감일이 설정된 미완료 리마인더를 조회합니다. */
    List<Reminder> findByDueDateIsNotNullAndIsCompletedFalse();
}
