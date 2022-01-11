CREATE TABLE familybenefit.criterion (

  -- Диапазон ID [1 000 000 000; 9 999 999 999]
  "id" NUMERIC NOT NULL DEFAULT (10000000000 - 1000000000) * random() + 1000000000,
  "name" TEXT NOT NULL,
  "info" TEXT NOT NULL,
  "id_type" NUMERIC NULL,

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
