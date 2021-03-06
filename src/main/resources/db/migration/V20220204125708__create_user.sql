CREATE TABLE family_benefit.user (

  "id" TEXT NOT NULL DEFAULT family_benefit.generate_id(20),
  "name" TEXT NOT NULL,
  "email" TEXT NOT NULL,
  "date_birth" DATE NULL,
  "date_select_criterion" DATE NULL,
  "is_fresh_benefits" BOOLEAN NULL,
  "id_city" TEXT NULL,

  CONSTRAINT user_pk PRIMARY KEY ("id"),
  CONSTRAINT user_uniq_email UNIQUE ("email"),
  CONSTRAINT user_fk_city FOREIGN KEY ("id_city")
    REFERENCES family_benefit.city("id")
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

COMMENT ON COLUMN family_benefit.user.id IS 'ID пользователя';
COMMENT ON COLUMN family_benefit.user.name IS 'Имя пользователя';
COMMENT ON COLUMN family_benefit.user.email IS 'Эл. почта пользователя';
COMMENT ON COLUMN family_benefit.user.date_birth IS 'Дата рождения пользователя';
COMMENT ON COLUMN family_benefit.user.date_select_criterion IS 'Дата последнего выбора критерий пользователя';
COMMENT ON COLUMN family_benefit.user.is_fresh_benefits IS 'Флаг свежести подобранных пособий';
COMMENT ON COLUMN family_benefit.user.id_city IS 'ID города пользователя';
