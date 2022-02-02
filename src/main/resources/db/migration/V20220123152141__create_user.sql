CREATE TABLE family_benefit.user (

  -- Диапазон ID [1 000 000 000; 9 999 999 999]
  "id" NUMERIC NOT NULL DEFAULT (10000000000 - 1000000000) * random() + 1000000000,
  "name" TEXT NOT NULL,
  "email" TEXT NOT NULL,
  "password" TEXT NOT NULL,
  "is_verified_email" BOOLEAN NOT NULL,
  "date_birth" DATE NOT NULL,
  "date_select_criterion" DATE NOT NULL,
  "is_fresh_benefits" BOOLEAN NOT NULL,
  "id_city" NUMERIC NULL,

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
COMMENT ON COLUMN family_benefit.user.password IS 'Хэш пароля пользователя';
COMMENT ON COLUMN family_benefit.user.is_verified_email IS 'Флаг подтверждения почты';
COMMENT ON COLUMN family_benefit.user.date_birth IS 'Дата рождения пользователя';
COMMENT ON COLUMN family_benefit.user.date_select_criterion IS 'Дата последнего выбора критерий пользователя';
COMMENT ON COLUMN family_benefit.user.is_fresh_benefits IS 'Флаг свежести подобранных пособий';
COMMENT ON COLUMN family_benefit.user.id_city IS 'ID города пользователя';
