package com.ai.reminder.ruro.service;

import com.ai.reminder.ruro.dto.request.ReminderRequest;
import com.ai.reminder.ruro.dto.response.ReminderResponse;
import com.ai.reminder.ruro.entity.Reminder;
import com.ai.reminder.ruro.entity.ReminderList;
import com.ai.reminder.ruro.enums.Priority;
import com.ai.reminder.ruro.exception.ResourceNotFoundException;
import com.ai.reminder.ruro.repository.ReminderListRepository;
import com.ai.reminder.ruro.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final ReminderListRepository reminderListRepository;

    /** 저장된 모든 리마인더를 조회합니다. */
    public List<ReminderResponse> getAllReminders() {
        return reminderRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /** 특정 리스트에 속한 리마인더 목록을 조회합니다. */
    public List<ReminderResponse> getRemindersForList(Long listId) {
        if (!reminderListRepository.existsById(listId)) {
            throw new ResourceNotFoundException("ReminderList", listId);
        }
        return reminderRepository.findByReminderList_Id(listId).stream()
                .map(this::toResponse)
                .toList();
    }

    /** 특정 ID의 리마인더를 조회합니다. */
    public ReminderResponse getReminderById(Long id) {
        return toResponse(findOrThrow(id));
    }

    /** 새 리마인더를 생성합니다. 우선순위가 없으면 NONE으로 기본 설정됩니다. */
    @Transactional
    public ReminderResponse createReminder(ReminderRequest req) {
        ReminderList list = reminderListRepository.findById(req.getListId())
                .orElseThrow(() -> new ResourceNotFoundException("ReminderList", req.getListId()));
        Reminder reminder = Reminder.builder()
                .title(req.getTitle())
                .notes(req.getNotes())
                .dueDate(req.getDueDate())
                .priority(req.getPriority() != null ? req.getPriority() : Priority.NONE)
                .isCompleted(req.getIsCompleted() != null && req.getIsCompleted())
                .isFlagged(req.getIsFlagged() != null && req.getIsFlagged())
                .reminderList(list)
                .build();
        return toResponse(reminderRepository.save(reminder));
    }

    /** 기존 리마인더의 제목·메모·마감일·우선순위·상태를 수정합니다. */
    @Transactional
    public ReminderResponse updateReminder(Long id, ReminderRequest req) {
        Reminder reminder = findOrThrow(id);
        reminder.setTitle(req.getTitle());
        reminder.setNotes(req.getNotes());
        reminder.setDueDate(req.getDueDate());
        reminder.setPriority(req.getPriority() != null ? req.getPriority() : Priority.NONE);
        if (req.getIsCompleted() != null) reminder.setCompleted(req.getIsCompleted());
        if (req.getIsFlagged() != null) reminder.setFlagged(req.getIsFlagged());
        return toResponse(reminderRepository.save(reminder));
    }

    /** 리마인더의 완료 상태를 토글합니다. (완료 ↔ 미완료) */
    @Transactional
    public ReminderResponse toggleComplete(Long id) {
        Reminder reminder = findOrThrow(id);
        reminder.setCompleted(!reminder.isCompleted());
        return toResponse(reminderRepository.save(reminder));
    }

    /** 리마인더의 플래그(중요 표시) 상태를 토글합니다. */
    @Transactional
    public ReminderResponse toggleFlag(Long id) {
        Reminder reminder = findOrThrow(id);
        reminder.setFlagged(!reminder.isFlagged());
        return toResponse(reminderRepository.save(reminder));
    }

    /** 리마인더를 다른 리스트로 이동합니다. */
    @Transactional
    public ReminderResponse moveToList(Long reminderId, Long targetListId) {
        Reminder reminder = findOrThrow(reminderId);
        ReminderList targetList = reminderListRepository.findById(targetListId)
                .orElseThrow(() -> new ResourceNotFoundException("ReminderList", targetListId));
        reminder.setReminderList(targetList);
        return toResponse(reminderRepository.save(reminder));
    }

    /** 특정 ID의 리마인더를 삭제합니다. */
    @Transactional
    public void deleteReminder(Long id) {
        Reminder reminder = findOrThrow(id);
        reminderRepository.delete(reminder);
    }

    /** 오늘 마감인 미완료 리마인더 목록을 조회합니다. */
    public List<ReminderResponse> getTodayReminders() {
        var start = LocalDate.now().atStartOfDay();
        var end = LocalDate.now().atTime(LocalTime.MAX);
        return reminderRepository.findByDueDateBetweenAndIsCompletedFalse(start, end).stream()
                .map(this::toResponse)
                .toList();
    }

    /** 마감일이 설정된 미완료 리마인더 목록을 조회합니다. */
    public List<ReminderResponse> getScheduledReminders() {
        return reminderRepository.findByDueDateIsNotNullAndIsCompletedFalse().stream()
                .map(this::toResponse)
                .toList();
    }

    /** 플래그가 설정된 미완료 리마인더 목록을 조회합니다. */
    public List<ReminderResponse> getFlaggedReminders() {
        return reminderRepository.findByIsFlaggedTrueAndIsCompletedFalse().stream()
                .map(this::toResponse)
                .toList();
    }

    /** 완료된 모든 리마인더 목록을 조회합니다. */
    public List<ReminderResponse> getCompletedReminders() {
        return reminderRepository.findByIsCompletedTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    /** ID로 리마인더를 조회하고, 없으면 예외를 발생시킵니다. */
    private Reminder findOrThrow(Long id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder", id));
    }

    /** Reminder 엔티티를 응답 DTO로 변환합니다. */
    private ReminderResponse toResponse(Reminder r) {
        return ReminderResponse.builder()
                .id(r.getId())
                .title(r.getTitle())
                .notes(r.getNotes())
                .dueDate(r.getDueDate())
                .priority(r.getPriority())
                .isCompleted(r.isCompleted())
                .isFlagged(r.isFlagged())
                .createdAt(r.getCreatedAt())
                .listId(r.getReminderList().getId())
                .listName(r.getReminderList().getName())
                .build();
    }
}
