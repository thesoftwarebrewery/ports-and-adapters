create table outbox_messages
(
    id           text        not null primary key,
    topic        text        not null,
    ordering_key text,
    attributes   text,
    data         bytea,
    created_at   timestamptz not null default now()
);

create index created_at_idx on outbox_messages (
    created_at
);
