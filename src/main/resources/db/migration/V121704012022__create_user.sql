CREATE TABLE familybenefit.user (

  "id" BIGINT NOT NULL,
  "name" TEXT NOT NULL,
  "email" TEXT NOT NULL,
  "password" TEXT NOT NULL,
  "date_registration" DATE NOT NULL,
  "id_role" BIGINT NOT NULL,
  "id_city" BIGINT NULL,

  CONSTRAINT user_pk PRIMARY KEY ("id"),
  CONSTRAINT user_uniq_email UNIQUE ("email"),
  CONSTRAINT user_fk_role FOREIGN KEY ("id_role")
    REFERENCES familybenefit.role("id")
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT user_fk_city FOREIGN KEY ("id_role")
    REFERENCES familybenefit.city("id")
    ON DELETE SET NULL
    ON UPDATE CASCADE
);

COMMENT ON COLUMN familybenefit.user.id IS 'ID пользователя';
COMMENT ON COLUMN familybenefit.user.name IS 'Имя пользователя';
COMMENT ON COLUMN familybenefit.user.email IS 'Эл. почта пользователя';
COMMENT ON COLUMN familybenefit.user.password IS 'Хэш пароля пользователя';
COMMENT ON COLUMN familybenefit.user.date_registration IS 'Дата регистрации пользователя';
COMMENT ON COLUMN familybenefit.user.id_role IS 'ID роли пользователя';
COMMENT ON COLUMN familybenefit.user.id_city IS 'ID города пользователя';
