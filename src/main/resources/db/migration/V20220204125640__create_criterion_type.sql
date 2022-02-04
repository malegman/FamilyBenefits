CREATE TABLE family_benefit.criterion_type (

  "id" TEXT NOT NULL DEFAULT family_benefit.generate_id(20),
  "name" TEXT NOT NULL,
  "info" TEXT NOT NULL,

  CONSTRAINT criterion_type_pk PRIMARY KEY ("id"),
  CONSTRAINT criterion_type_uniq_name UNIQUE ("name")
);

COMMENT ON COLUMN family_benefit.criterion_type.id IS 'ID типа критерия';
COMMENT ON COLUMN family_benefit.criterion_type.name IS 'Название типа критерия';
COMMENT ON COLUMN family_benefit.criterion_type.info IS 'Информация типа критерия';
