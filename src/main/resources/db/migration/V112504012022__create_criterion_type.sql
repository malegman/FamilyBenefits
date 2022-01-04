CREATE TABLE familybenefit.criterion_type (

  "id" BIGINT NOT NULL,
  "name" TEXT NOT NULL,
  "info" TEXT NOT NULL,

  CONSTRAINT criterion_type_pk PRIMARY KEY ("id"),
  CONSTRAINT criterion_type_uniq_name UNIQUE ("name")
);

COMMENT ON COLUMN familybenefit.criterion_type.id IS 'ID типа критерия';
COMMENT ON COLUMN familybenefit.criterion_type.name IS 'Название типа критерия';
COMMENT ON COLUMN familybenefit.criterion_type.info IS 'Информация типа критерия';
