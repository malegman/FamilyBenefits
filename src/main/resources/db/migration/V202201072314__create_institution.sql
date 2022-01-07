CREATE TABLE familybenefit.institution (

  "id" NUMERIC NOT NULL,
  "name" TEXT NOT NULL,
  "address" TEXT NOT NULL,
  "phone" TEXT NULL,
  "email" TEXT NULL,
  "schedule" TEXT NULL,
  "id_city" NUMERIC NOT NULL,

  CONSTRAINT institution_pk PRIMARY KEY ("id"),
  CONSTRAINT child_uniq_name UNIQUE ("name"),
  CONSTRAINT institution_fk_city FOREIGN KEY ("id_city")
    REFERENCES familybenefit.city("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN familybenefit.institution.id IS 'ID учреждения';
COMMENT ON COLUMN familybenefit.institution.name IS 'Название учреждения';
COMMENT ON COLUMN familybenefit.institution.address IS 'Адрес учреждения';
COMMENT ON COLUMN familybenefit.institution.phone IS 'Телефон учреждения';
COMMENT ON COLUMN familybenefit.institution.email IS 'Эл. почта учржедения';
COMMENT ON COLUMN familybenefit.institution.schedule IS 'График работы учреждения';
COMMENT ON COLUMN familybenefit.institution.id_city IS 'ID города учреждения';
