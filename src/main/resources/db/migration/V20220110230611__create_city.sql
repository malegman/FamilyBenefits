CREATE TABLE familybenefit.city (

  -- Диапазон ID [1 000 000 000; 9 999 999 999]
  "id" NUMERIC NOT NULL DEFAULT (10000000000 - 1000000000) * random() + 1000000000,
  "name" TEXT NOT NULL,
  "info" TEXT NULL,

  CONSTRAINT city_pk PRIMARY KEY ("id"),
  CONSTRAINT city_uniq_name UNIQUE ("name")
);

COMMENT ON COLUMN familybenefit.city.id IS 'ID города';
COMMENT ON COLUMN familybenefit.city.name IS 'Название города';
COMMENT ON COLUMN familybenefit.city.info IS 'Информация города';


