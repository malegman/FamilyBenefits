CREATE TABLE family_benefit.criterion_type (

  -- Диапазон ID [1 000 000 000; 9 999 999 999]
  "id" NUMERIC NOT NULL DEFAULT (10000000000 - 1000000000) * random() + 1000000000,
  "name" TEXT NOT NULL,
  "info" TEXT NOT NULL,

  CONSTRAINT criterion_type_pk PRIMARY KEY ("id"),
  CONSTRAINT criterion_type_uniq_name UNIQUE ("name")
);

COMMENT ON COLUMN family_benefit.criterion_type.id IS 'ID типа критерия';
COMMENT ON COLUMN family_benefit.criterion_type.name IS 'Название типа критерия';
COMMENT ON COLUMN family_benefit.criterion_type.info IS 'Информация типа критерия';
