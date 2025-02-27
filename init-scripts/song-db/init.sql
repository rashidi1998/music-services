CREATE TABLE songs
(
    id       BIGINT NOT NULL PRIMARY KEY,
    album    VARCHAR(100) NOT NULL,
    artist   VARCHAR(100) NOT NULL,
    duration VARCHAR(255) NOT NULL,
    name     VARCHAR(100) NOT NULL,
    year     VARCHAR(255) NOT NULL
);
