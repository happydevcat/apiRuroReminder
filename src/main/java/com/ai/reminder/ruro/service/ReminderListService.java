package com.ai.reminder.ruro.service;

import com.ai.reminder.ruro.dto.request.ReminderListRequest;
import com.ai.reminder.ruro.dto.response.ReminderListDetailResponse;
import com.ai.reminder.ruro.dto.response.ReminderListResponse;
import com.ai.reminder.ruro.dto.response.ReminderResponse;
import com.ai.reminder.ruro.entity.ReminderList;
import com.ai.reminder.ruro.exception.ResourceNotFoundException;
import com.ai.reminder.ruro.repository.ReminderListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReminderListService {

    private final ReminderListRepository reminderListRepository;

    public List<ReminderListResponse> getAllLists() {
        return reminderListRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ReminderListDetailResponse getListById(Long id) {
        ReminderList list = findOrThrow(id);
        List<ReminderResponse> reminders = list.getReminders().stream()
                .map(r -> ReminderResponse.builder()
                        .id(r.getId())
                        .title(r.getTitle())
                        .notes(r.getNotes())
                        .dueDate(r.getDueDate())
                        .priority(r.getPriority())
                        .isCompleted(r.isCompleted())
                        .isFlagged(r.isFlagged())
                        .createdAt(r.getCreatedAt())
                        .listId(list.getId())
                        .listName(list.getName())
                        .build())
                .toList();
        return ReminderListDetailResponse.builder()
                .id(list.getId())
                .name(list.getName())
                .color(list.getColor())
                .createdAt(list.getCreatedAt())
                .reminders(reminders)
                .build();
    }

    @Transactional
    public ReminderListResponse createList(ReminderListRequest req) {
        ReminderList list = ReminderList.builder()
                .name(req.getName())
                .color(req.getColor())
                .build();
        return toResponse(reminderListRepository.save(list));
    }

    @Transactional
    public ReminderListResponse updateList(Long id, ReminderListRequest req) {
        ReminderList list = findOrThrow(id);
        list.setName(req.getName());
        list.setColor(req.getColor());
        return toResponse(reminderListRepository.save(list));
    }

    @Transactional
    public void deleteList(Long id) {
        ReminderList list = findOrThrow(id);
        reminderListRepository.delete(list);
    }

    private ReminderList findOrThrow(Long id) {
        return reminderListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReminderList", id));
    }

    private ReminderListResponse toResponse(ReminderList list) {
        int total = list.getReminders().size();
        int pending = (int) list.getReminders().stream().filter(r -> !r.isCompleted()).count();
        return ReminderListResponse.builder()
                .id(list.getId())
                .name(list.getName())
                .color(list.getColor())
                .totalCount(total)
                .pendingCount(pending)
                .createdAt(list.getCreatedAt())
                .build();
    }
}
