create table offers
(
    offer_id    text        not null primary key,
    product_id  text        not null,
    country     text        not null,
    created_at  timestamptz not null,
    modified_at timestamptz not null
);

create index offer_id_idx on offers (
    offer_id
);
