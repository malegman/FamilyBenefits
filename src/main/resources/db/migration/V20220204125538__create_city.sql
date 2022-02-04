CREATE TABLE family_benefit.city (

  "id" TEXT NOT NULL DEFAULT family_benefit.generate_id(20),
  "name" TEXT NOT NULL,
  "info" TEXT NULL,

  CONSTRAINT city_pk PRIMARY KEY ("id"),
  CONSTRAINT city_uniq_name UNIQUE ("name")
);

COMMENT ON COLUMN family_benefit.city.id IS 'ID города';
COMMENT ON COLUMN family_benefit.city.name IS 'Название города';
COMMENT ON COLUMN family_benefit.city.info IS 'Информация города';


