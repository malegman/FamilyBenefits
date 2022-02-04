CREATE TABLE family_benefit.criterion (

  "id" TEXT NOT NULL DEFAULT family_benefit.generate_id(20),
  "name" TEXT NOT NULL,
  "info" TEXT NOT NULL,
  "id_type" TEXT NOT NULL,

  CONSTRAINT criterion_pk PRIMARY KEY ("id"),
  CONSTRAINT criterion_uniq_name UNIQUE ("name"),
  CONSTRAINT criterion_fk_type FOREIGN KEY ("id_type")
    REFERENCES family_benefit.criterion_type("id")
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

COMMENT ON COLUMN family_benefit.criterion.id IS 'ID критерия';
COMMENT ON COLUMN family_benefit.criterion.name IS 'ID критерия';
COMMENT ON COLUMN family_benefit.criterion.info IS 'ID критерия';
COMMENT ON COLUMN family_benefit.criterion.id_type IS 'ID критерия';
