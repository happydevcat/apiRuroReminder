package com.ai.reminder.ruro.repository;

import com.ai.reminder.ruro.entity.ReminderList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReminderListRepository extends JpaRepository<ReminderList, Long> {
    /** 동일한 이름의 리스트가 이미 존재하는지 확인합니다. */
    boolean existsByName(String name);

    /** 이름으로 리스트를 조회합니다. */
    Optional<ReminderList> findByName(String name);
}
