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

    /** 저장된 모든 리마인더 리스트를 조회합니다. */
    public List<ReminderListResponse> getAllLists() {
        return reminderListRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /** 특정 ID의 리마인더 리스트와 포함된 리마인더 목록을 상세 조회합니다. */
    public ReminderListDetailResponse getListById(Long id) {
        ReminderList list = reminderListRepository.findByIdWithReminders(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReminderList", id));
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

    /** 이름과 색상을 받아 새 리마인더 리스트를 생성합니다. */
    @Transactional
    public ReminderListResponse createList(ReminderListRequest req) {
        ReminderList list = ReminderList.builder()
                .name(req.getName())
                .color(req.getColor())
                .build();
        return toResponse(reminderListRepository.save(list));
    }

    /** 기존 리마인더 리스트의 이름과 색상을 수정합니다. */
    @Transactional
    public ReminderListResponse updateList(Long id, ReminderListRequest req) {
        ReminderList list = findOrThrow(id);
        list.setName(req.getName());
        list.setColor(req.getColor());
        return toResponse(reminderListRepository.save(list));
    }

    /** 특정 ID의 리마인더 리스트를 삭제합니다. */
    @Transactional
    public void deleteList(Long id) {
        ReminderList list = findOrThrow(id);
        reminderListRepository.delete(list);
    }

    /** ID로 리마인더 리스트를 조회하고, 없으면 예외를 발생시킵니다. */
    private ReminderList findOrThrow(Long id) {
        return reminderListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReminderList", id));
    }

    /** ReminderList 엔티티를 응답 DTO로 변환합니다. (전체·미완료 항목 수 포함) */
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
