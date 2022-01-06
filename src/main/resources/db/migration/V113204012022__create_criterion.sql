CREATE TABLE familybenefit.criterion (

  "id" BIGINT NOT NULL,
  "name" TEXT NOT NULL,
  "info" TEXT NOT NULL,
  "id_type" BIGINT NULL,

  CONSTRAINT criterion_pk PRIMARY KEY ("id"),
  CONSTRAINT criterion_uniq_name UNIQUE ("name"),
  CONSTRAINT criterion_fk_type FOREIGN KEY ("id_type")
    REFERENCES familybenefit.criterion_type("id")
    ON DELETE SET NULL
    ON UPDATE CASCADE
);

COMMENT ON COLUMN familybenefit.criterion.id IS 'ID критерия';
COMMENT ON COLUMN familybenefit.criterion.name IS 'ID критерия';
COMMENT ON COLUMN familybenefit.criterion.info IS 'ID критерия';
COMMENT ON COLUMN familybenefit.criterion.id_type IS 'ID критерия';