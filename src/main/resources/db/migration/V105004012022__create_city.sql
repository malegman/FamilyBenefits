CREATE TABLE familybenefit.city (

  "id" BIGINT NOT NULL,
  "name" TEXT NOT NULL,

  CONSTRAINT city_pk PRIMARY KEY ("id"),
  CONSTRAINT city_uniq_name UNIQUE ("name")
);

COMMENT ON COLUMN familybenefit.city.id IS 'ID города';
COMMENT ON COLUMN familybenefit.city.name IS 'Название города';


