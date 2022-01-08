CREATE TABLE familybenefit.verify_email_code (

  "id_user" NUMERIC NOT NULL,
  "code" NUMERIC NOT NULL,
  "date_expiration" TIMESTAMP NOT NULL,

  CONSTRAINT verify_email_code_pk PRIMARY KEY ("id_user"),
  CONSTRAINT verify_email_code_uniq_token UNIQUE ("code"),
  CONSTRAINT verify_email_code_fk_user FOREIGN KEY ("id_user")
    REFERENCES familybenefit.user("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN familybenefit.verify_email_code.id_user IS 'ID пользователя';
COMMENT ON COLUMN familybenefit.verify_email_code.code IS 'Код подтверждения почты';
COMMENT ON COLUMN familybenefit.verify_email_code.date_expiration IS 'Время истечения срока кода';
