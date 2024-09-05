CREATE SEQUENCE IF NOT EXISTS hibernate_sequence;

CREATE TABLE IF NOT EXISTS image
(
    id          uuid      not null
        constraint image_pkey
            primary key,
    url         varchar   not null,
    image_label varchar,
    created_at  timestamp not null,
    updated_at  timestamp not null
);

CREATE TABLE IF NOT EXISTS image_object
(
    id         uuid      not null
        constraint image_object_pkey
            primary key,
    object_tag varchar   not null,
    created_at timestamp not null,
    updated_at timestamp not null
);

CREATE TABLE IF NOT EXISTS image_image_object
(
    image_id uuid not null
        constraint image_image_object_fkey
            references image,
    image_object_id    uuid not null
        constraint image_object_image_fkey
            references image_object,
    primary key (image_id, image_object_id)
);

CREATE INDEX IF NOT EXISTS url_idx ON image (url);
CREATE INDEX IF NOT EXISTS object_tag_idx ON image_object (object_tag);