CREATE TABLE familybenefit.user (

  -- Диапазон ID [1 000 000 000; 9 999 999 999]
  "id" NUMERIC NOT NULL DEFAULT (10000000000 - 1000000000) * random() + 1000000000,
  "name" TEXT NOT NULL,
  "email" TEXT NOT NULL,
  "password" TEXT NOT NULL,
  "is_verified_email" BIT NOT NULL,
  "date_registration" TIMESTAMP NOT NULL,
  "id_city" NUMERIC NULL,

  CONSTRAINT user_pk PRIMARY KEY ("id"),
  CONSTRAINT user_uniq_email UNIQUE ("email"),
  CONSTRAINT user_fk_city FOREIGN KEY ("id_city")
    REFERENCES familybenefit.city("id")
    ON DELETE SET NULL
    ON UPDATE CASCADE
);

COMMENT ON COLUMN familybenefit.user.id IS 'ID пользователя';
COMMENT ON COLUMN familybenefit.user.name IS 'Имя пользователя';
COMMENT ON COLUMN familybenefit.user.email IS 'Эл. почта пользователя';
COMMENT ON COLUMN familybenefit.user.password IS 'Хэш пароля пользователя';
COMMENT ON COLUMN familybenefit.user.is_verified_email IS 'Статус подтверждения почты';
COMMENT ON COLUMN familybenefit.user.date_registration IS 'Дата регистрации пользователя';
COMMENT ON COLUMN familybenefit.user.id_city IS 'ID города пользователя';
