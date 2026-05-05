package com.ai.reminder.ruro.controller;

import com.ai.reminder.ruro.dto.request.ReminderListRequest;
import com.ai.reminder.ruro.dto.response.ReminderListDetailResponse;
import com.ai.reminder.ruro.dto.response.ReminderListResponse;
import com.ai.reminder.ruro.service.ReminderListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
@RequiredArgsConstructor
@Tag(name = "Reminder Lists", description = "리마인더 리스트(그룹) 관리")
public class ReminderListController {

    private final ReminderListService service;

    @GetMapping
    @Operation(summary = "모든 리스트 조회")
    public List<ReminderListResponse> getAllLists() {
        return service.getAllLists();
    }

    @GetMapping("/{id}")
    @Operation(summary = "리스트 단건 조회 (소속 리마인더 포함)")
    public ReminderListDetailResponse getListById(@PathVariable Long id) {
        return service.getListById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "리스트 생성")
    public ReminderListResponse createList(@RequestBody ReminderListRequest req) {
        return service.createList(req);
    }

    @PutMapping("/{id}")
    @Operation(summary = "리스트 수정")
    public ReminderListResponse updateList(@PathVariable Long id, @RequestBody ReminderListRequest req) {
        return service.updateList(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "리스트 삭제 (소속 리마인더 포함)")
    public void deleteList(@PathVariable Long id) {
        service.deleteList(id);
    }
}
