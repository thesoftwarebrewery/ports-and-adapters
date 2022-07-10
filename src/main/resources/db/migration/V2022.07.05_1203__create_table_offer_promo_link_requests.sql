create table link_requests
(
    created_at   timestamptz not null default now(),
    trigger      text        not null,
    key          text        not null
);

create unique index trigger_key_idx on link_requests (
    trigger, key
);
