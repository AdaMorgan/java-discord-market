CREATE TABLE :message_id
(
    id         INTEGER DEFAULT :id         NOT NULL CONSTRAINT auction_id_key PRIMARY KEY,
    message_id INTEGER DEFAULT :message_id NOT NULL,
    item       VARCHAR DEFAULT :item       NOT NULL,
    author     VARCHAR DEFAULT :author     NOT NULL,
    author_id  INTEGER DEFAULT :author_id  NOT NULL,
    status     VARCHAR DEFAULT :status     NOT NULL,
    initial    INTEGER DEFAULT :initial    NOT NULL,
    current    INTEGER DEFAULT 0           NOT NULL,
    final      INTEGER DEFAULT 0           NOT NULL,
    start      DATE    DEFAULT :start      NOT NULL,
    "end"      DATE    DEFAULT :end        NOT NULL
);
