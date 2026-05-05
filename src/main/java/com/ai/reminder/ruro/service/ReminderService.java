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

    public List<ReminderResponse> getAllReminders() {
        return reminderRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ReminderResponse> getRemindersForList(Long listId) {
        if (!reminderListRepository.existsById(listId)) {
            throw new ResourceNotFoundException("ReminderList", listId);
        }
        return reminderRepository.findByReminderList_Id(listId).stream()
                .map(this::toResponse)
                .toList();
    }

    public ReminderResponse getReminderById(Long id) {
        return toResponse(findOrThrow(id));
    }

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

    @Transactional
    public ReminderResponse toggleComplete(Long id) {
        Reminder reminder = findOrThrow(id);
        reminder.setCompleted(!reminder.isCompleted());
        return toResponse(reminderRepository.save(reminder));
    }

    @Transactional
    public ReminderResponse toggleFlag(Long id) {
        Reminder reminder = findOrThrow(id);
        reminder.setFlagged(!reminder.isFlagged());
        return toResponse(reminderRepository.save(reminder));
    }

    @Transactional
    public ReminderResponse moveToList(Long reminderId, Long targetListId) {
        Reminder reminder = findOrThrow(reminderId);
        ReminderList targetList = reminderListRepository.findById(targetListId)
                .orElseThrow(() -> new ResourceNotFoundException("ReminderList", targetListId));
        reminder.setReminderList(targetList);
        return toResponse(reminderRepository.save(reminder));
    }

    @Transactional
    public void deleteReminder(Long id) {
        Reminder reminder = findOrThrow(id);
        reminderRepository.delete(reminder);
    }

    public List<ReminderResponse> getTodayReminders() {
        var start = LocalDate.now().atStartOfDay();
        var end = LocalDate.now().atTime(LocalTime.MAX);
        return reminderRepository.findByDueDateBetweenAndIsCompletedFalse(start, end).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ReminderResponse> getScheduledReminders() {
        return reminderRepository.findByDueDateIsNotNullAndIsCompletedFalse().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ReminderResponse> getFlaggedReminders() {
        return reminderRepository.findByIsFlaggedTrueAndIsCompletedFalse().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ReminderResponse> getCompletedReminders() {
        return reminderRepository.findByIsCompletedTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    private Reminder findOrThrow(Long id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder", id));
    }

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
