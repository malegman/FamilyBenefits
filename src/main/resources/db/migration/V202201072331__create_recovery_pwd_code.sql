CREATE TABLE familybenefit.recovery_pwd_code (

  "id_user" NUMERIC NOT NULL,
  "code" NUMERIC NOT NULL,
  "date_expiration" TIMESTAMP NOT NULL,

  CONSTRAINT recovery_pwd_code_pk PRIMARY KEY ("id_user"),
  CONSTRAINT recovery_pwd_code_uniq_token UNIQUE ("code"),
  CONSTRAINT recovery_pwd_code_fk_user FOREIGN KEY ("id_user")
    REFERENCES familybenefit.user("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN familybenefit.recovery_pwd_code.id_user IS 'ID пользователя';
COMMENT ON COLUMN familybenefit.recovery_pwd_code.code IS 'Код восстановления пароля';
COMMENT ON COLUMN familybenefit.recovery_pwd_code.date_expiration IS 'Время истечения срока кода';
