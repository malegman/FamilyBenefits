CREATE TABLE family_benefit.institution (

  "id" TEXT NOT NULL DEFAULT family_benefit.generate_id(20),
  "name" TEXT NOT NULL,
  "info" TEXT NULL,
  "address" TEXT NOT NULL,
  "phone" TEXT NULL,
  "email" TEXT NULL,
  "schedule" TEXT NULL,
  "id_city" TEXT NOT NULL,

  CONSTRAINT institution_pk PRIMARY KEY ("id"),
  CONSTRAINT child_uniq_name UNIQUE ("name"),
  CONSTRAINT institution_fk_city FOREIGN KEY ("id_city")
    REFERENCES family_benefit.city("id")
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

COMMENT ON COLUMN family_benefit.institution.id IS 'ID учреждения';
COMMENT ON COLUMN family_benefit.institution.name IS 'Название учреждения';
COMMENT ON COLUMN family_benefit.institution.info IS 'Информация учреждения';
COMMENT ON COLUMN family_benefit.institution.address IS 'Адрес учреждения';
COMMENT ON COLUMN family_benefit.institution.phone IS 'Телефон учреждения';
COMMENT ON COLUMN family_benefit.institution.email IS 'Эл. почта учреждения';
COMMENT ON COLUMN family_benefit.institution.schedule IS 'График работы учреждения';
COMMENT ON COLUMN family_benefit.institution.id_city IS 'ID города учреждения';
