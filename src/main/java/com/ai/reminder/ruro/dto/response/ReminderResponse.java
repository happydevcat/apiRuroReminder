package com.ai.reminder.ruro.dto.response;

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
public class ReminderResponse {
    private Long id;
    private String title;
    private String notes;
    private LocalDateTime dueDate;
    private Priority priority;
    private boolean isCompleted;
    private boolean isFlagged;
    private LocalDateTime createdAt;
    private Long listId;
    private String listName;
}
