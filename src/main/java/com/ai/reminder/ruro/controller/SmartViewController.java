package com.ai.reminder.ruro.controller;

import com.ai.reminder.ruro.dto.response.ReminderResponse;
import com.ai.reminder.ruro.service.ReminderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/smart")
@RequiredArgsConstructor
@Tag(name = "Smart Views", description = "전체 리마인더 대상 가상 필터 뷰 (읽기 전용)")
public class SmartViewController {

    private final ReminderService service;

    @GetMapping("/today")
    @Operation(summary = "오늘 마감 미완료 리마인더")
    public List<ReminderResponse> getToday() {
        return service.getTodayReminders();
    }

    @GetMapping("/scheduled")
    @Operation(summary = "마감일이 있는 전체 미완료 리마인더")
    public List<ReminderResponse> getScheduled() {
        return service.getScheduledReminders();
    }

    @GetMapping("/flagged")
    @Operation(summary = "플래그 된 미완료 리마인더")
    public List<ReminderResponse> getFlagged() {
        return service.getFlaggedReminders();
    }

    @GetMapping("/completed")
    @Operation(summary = "완료된 전체 리마인더")
    public List<ReminderResponse> getCompleted() {
        return service.getCompletedReminders();
    }
}
