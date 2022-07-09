create table promos
(
    promotion_id text        not null primary key,
    product_id   text        not null,
    country      text        not null,
    created_at   timestamptz not null,
    modified_at  timestamptz not null
);

create index product_id_idx on promos (
    product_id
);
