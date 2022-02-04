CREATE TABLE family_benefit.recovery_pwd_code (

  "id_user" TEXT NOT NULL,
  "code" NUMERIC NOT NULL,
  "date_expiration" TIMESTAMP NOT NULL,
  "for_user" BIT NOT NULL,

  CONSTRAINT recovery_pwd_code_pk PRIMARY KEY ("id_user"),
  CONSTRAINT recovery_pwd_code_uniq_token UNIQUE ("code"),
  CONSTRAINT recovery_pwd_code_fk_user FOREIGN KEY ("id_user")
    REFERENCES family_benefit.user("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN family_benefit.recovery_pwd_code.id_user IS 'ID пользователя';
COMMENT ON COLUMN family_benefit.recovery_pwd_code.code IS 'Код восстановления пароля';
COMMENT ON COLUMN family_benefit.recovery_pwd_code.date_expiration IS 'Время истечения срока кода';
