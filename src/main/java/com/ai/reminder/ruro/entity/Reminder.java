package com.ai.reminder.ruro.entity;

import com.ai.reminder.ruro.enums.Priority;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reminder")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted;

    @Column(name = "is_flagged", nullable = false)
    private boolean isFlagged;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", nullable = false)
    private ReminderList reminderList;

    @PrePersist
    private void onPrePersist() {
        createdAt = LocalDateTime.now();
        if (priority == null) priority = Priority.NONE;
    }
}
