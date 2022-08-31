CREATE TABLE IF NOT EXISTS perfumer
(
    id          SERIAL PRIMARY KEY,
    first_name  VARCHAR(50) NOT NULL,
    last_name   VARCHAR(50) NOT NULL,
    country_id  SMALLINT,
    email       VARCHAR(255),
    description VARCHAR
);
---------------------------------------------
CREATE TABLE IF NOT EXISTS aroma
(
    id          SERIAL PRIMARY KEY,
    perfumer_id INT,
    country_id  SMALLINT,
    aroma_name  VARCHAR(100) NOT NULL,
    description VARCHAR
);
---------------------------------------------
CREATE TABLE IF NOT EXISTS brand
(
    id          SERIAL PRIMARY KEY,
    brand_name  VARCHAR(100) NOT NULL,
    country_id  SMALLINT,
    description VARCHAR
);

CREATE TABLE IF NOT EXISTS brands_perfumers
(
    brand_id    INT REFERENCES brand (id),
    perfumer_id INT REFERENCES perfumer (id)
);

CREATE TABLE IF NOT EXISTS brands_perfumes
(
    brand_id   INT REFERENCES brand (id),
    perfume_id INT REFERENCES aroma (id)
);
---------------------------------------------
CREATE TABLE IF NOT EXISTS family
(
    id          SERIAL PRIMARY KEY,
    family_name VARCHAR(50) NOT NULL,
    description VARCHAR
);

CREATE TABLE IF NOT EXISTS families_perfumes
(
    family_id  INT REFERENCES family (id),
    perfume_id INT REFERENCES aroma (id)
);
---------------------------------------------
CREATE TABLE IF NOT EXISTS accord
(
    id          SERIAL PRIMARY KEY,
    accord_name VARCHAR(50) NOT NULL,
    description VARCHAR
);

CREATE TABLE IF NOT EXISTS accords_families
(
    accord_id INT REFERENCES accord (id),
    family_id INT REFERENCES family (id)
);
---------------------------------------------
CREATE TABLE IF NOT EXISTS note
(
    id          SERIAL PRIMARY KEY,
    note_name   VARCHAR(50) NOT NULL,
    description VARCHAR
);

CREATE TABLE IF NOT EXISTS notes_accords
(
    note_id   INT REFERENCES note (id),
    accord_id INT REFERENCES accord (id)
);
---------------------------------------------
CREATE TABLE IF NOT EXISTS country
(
    id           SERIAL2 PRIMARY KEY,
    country_code VARCHAR(3),
    country_name VARCHAR(70)
);