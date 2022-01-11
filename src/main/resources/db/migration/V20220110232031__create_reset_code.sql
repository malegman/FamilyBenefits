CREATE TABLE familybenefit.reset_code (

  "id_user" NUMERIC NOT NULL,
  "code" NUMERIC NOT NULL,
  "date_expiration" TIMESTAMP NOT NULL,

  CONSTRAINT reset_code_pk PRIMARY KEY ("id_user"),
  CONSTRAINT reset_code_uniq_token UNIQUE ("code"),
  CONSTRAINT reset_code_fk_user FOREIGN KEY ("id_user")
    REFERENCES familybenefit.user("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN familybenefit.reset_code.id_user IS 'ID пользователя';
COMMENT ON COLUMN familybenefit.reset_code.code IS 'Код сброса сессии';
COMMENT ON COLUMN familybenefit.reset_code.date_expiration IS 'Время истечения срока кода';
