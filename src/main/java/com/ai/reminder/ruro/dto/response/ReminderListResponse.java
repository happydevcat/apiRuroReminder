package com.ai.reminder.ruro.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReminderListResponse {
    private Long id;
    private String name;
    private String color;
    private int totalCount;
    private int pendingCount;
    private LocalDateTime createdAt;
}
