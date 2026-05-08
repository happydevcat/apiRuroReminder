CREATE TABLE reminder_list (
    id         BIGSERIAL    PRIMARY KEY,
    name       VARCHAR(255) NOT NULL UNIQUE,
    color      VARCHAR(255),
    created_at TIMESTAMP    NOT NULL
);

CREATE TABLE reminder (
    id           BIGSERIAL    PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    notes        TEXT,
    due_date     TIMESTAMP,
    priority     VARCHAR(10)  NOT NULL DEFAULT 'NONE',
    is_completed BOOLEAN      NOT NULL DEFAULT FALSE,
    is_flagged   BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMP    NOT NULL,
    list_id      BIGINT       NOT NULL REFERENCES reminder_list(id) ON DELETE CASCADE
);