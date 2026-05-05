package com.ai.reminder.ruro.dto.request;

import com.ai.reminder.ruro.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReminderRequest {
    private String title;
    private String notes;
    private LocalDateTime dueDate;
    private Priority priority;
    private Boolean isCompleted;
    private Boolean isFlagged;
    private Long listId;
}
