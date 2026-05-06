package com.ai.reminder.ruro.entity;

import com.ai.reminder.ruro.enums.Priority;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReminderListTest {

    @Test
    void builder_setsNameAndColor() {
        ReminderList list = ReminderList.builder()
                .name("Work")
                .color("#FF0000")
                .build();

        assertThat(list.getName()).isEqualTo("Work");
        assertThat(list.getColor()).isEqualTo("#FF0000");
    }

    @Test
    void builder_remindersDefaultToEmptyList() {
        ReminderList list = ReminderList.builder()
                .name("Work")
                .build();

        assertThat(list.getReminders()).isNotNull().isEmpty();
    }

    @Test
    void noArgsConstructor_fieldsAreNull() {
        ReminderList list = new ReminderList();

        assertThat(list.getId()).isNull();
        assertThat(list.getName()).isNull();
        assertThat(list.getColor()).isNull();
        assertThat(list.getCreatedAt()).isNull();
    }

    @Test
    void setter_updatesName() {
        ReminderList list = ReminderList.builder().name("Work").build();
        list.setName("Personal");

        assertThat(list.getName()).isEqualTo("Personal");
    }

    @Test
    void setter_updatesColor() {
        ReminderList list = ReminderList.builder().name("Work").color("#FF0000").build();
        list.setColor("#00FF00");

        assertThat(list.getColor()).isEqualTo("#00FF00");
    }

    @Test
    void allArgsConstructor_setsAllFields() {
        LocalDateTime now = LocalDateTime.now();
        List<Reminder> reminders = new ArrayList<>();

        ReminderList list = new ReminderList(1L, "Work", "#FF0000", now, reminders);

        assertThat(list.getId()).isEqualTo(1L);
        assertThat(list.getName()).isEqualTo("Work");
        assertThat(list.getColor()).isEqualTo("#FF0000");
        assertThat(list.getCreatedAt()).isEqualTo(now);
        assertThat(list.getReminders()).isSameAs(reminders);
    }

    @Test
    void equals_sameFieldValues_equal() {
        LocalDateTime now = LocalDateTime.now();

        ReminderList list1 = ReminderList.builder()
                .id(1L).name("Work").color("#FF0000").createdAt(now).build();
        ReminderList list2 = ReminderList.builder()
                .id(1L).name("Work").color("#FF0000").createdAt(now).build();

        assertThat(list1).isEqualTo(list2);
    }

    @Test
    void equals_differentName_notEqual() {
        ReminderList list1 = ReminderList.builder().name("Work").build();
        ReminderList list2 = ReminderList.builder().name("Personal").build();

        assertThat(list1).isNotEqualTo(list2);
    }

    @Test
    void equals_excludesReminders() {
        LocalDateTime now = LocalDateTime.now();

        ReminderList list1 = ReminderList.builder()
                .id(1L).name("Work").color("#FF0000").createdAt(now).build();
        ReminderList list2 = ReminderList.builder()
                .id(1L).name("Work").color("#FF0000").createdAt(now).build();

        list1.getReminders().add(Reminder.builder()
                .title("Task")
                .priority(Priority.NONE)
                .reminderList(list1)
                .build());

        assertThat(list1).isEqualTo(list2);
    }

    @Test
    void hashCode_excludesReminders() {
        LocalDateTime now = LocalDateTime.now();

        ReminderList list1 = ReminderList.builder()
                .id(1L).name("Work").color("#FF0000").createdAt(now).build();
        ReminderList list2 = ReminderList.builder()
                .id(1L).name("Work").color("#FF0000").createdAt(now).build();

        list1.getReminders().add(Reminder.builder()
                .title("Task")
                .priority(Priority.NONE)
                .reminderList(list1)
                .build());

        assertThat(list1.hashCode()).isEqualTo(list2.hashCode());
    }

    @Test
    void toString_doesNotContainRemindersField() {
        ReminderList list = ReminderList.builder()
                .name("Work")
                .color("#FF0000")
                .build();

        assertThat(list.toString()).contains("Work").doesNotContain("reminders");
    }
}
