package com.ai.reminder.ruro.controller;

import com.ai.reminder.ruro.dto.request.ReminderRequest;
import com.ai.reminder.ruro.dto.response.ReminderResponse;
import com.ai.reminder.ruro.service.ReminderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
@Tag(name = "Reminders", description = "리마인더 관리")
public class ReminderController {

    private final ReminderService service;

    @GetMapping
    @Operation(summary = "리마인더 조회 (listId 쿼리 파라미터로 리스트별 필터 가능)")
    public List<ReminderResponse> getReminders(@RequestParam(required = false) Long listId) {
        if (listId != null) return service.getRemindersForList(listId);
        return service.getAllReminders();
    }

    @GetMapping("/{id}")
    @Operation(summary = "리마인더 단건 조회")
    public ReminderResponse getReminderById(@PathVariable Long id) {
        return service.getReminderById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "리마인더 생성 (listId 필수)")
    public ReminderResponse createReminder(@RequestBody ReminderRequest req) {
        return service.createReminder(req);
    }

    @PutMapping("/{id}")
    @Operation(summary = "리마인더 전체 수정")
    public ReminderResponse updateReminder(@PathVariable Long id, @RequestBody ReminderRequest req) {
        return service.updateReminder(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "리마인더 삭제")
    public void deleteReminder(@PathVariable Long id) {
        service.deleteReminder(id);
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "완료 상태 토글")
    public ReminderResponse toggleComplete(@PathVariable Long id) {
        return service.toggleComplete(id);
    }

    @PatchMapping("/{id}/flag")
    @Operation(summary = "플래그 토글")
    public ReminderResponse toggleFlag(@PathVariable Long id) {
        return service.toggleFlag(id);
    }

    @PatchMapping("/{id}/move")
    @Operation(summary = "다른 리스트로 이동")
    public ReminderResponse moveToList(@PathVariable Long id, @RequestParam Long targetListId) {
        return service.moveToList(id, targetListId);
    }
}
