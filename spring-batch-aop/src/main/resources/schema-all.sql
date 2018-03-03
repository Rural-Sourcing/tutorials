DROP TABLE cereal IF EXISTS;

CREATE TABLE cereal  (
    cereal_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(20),
    num_of_boxes BIGINT
);
