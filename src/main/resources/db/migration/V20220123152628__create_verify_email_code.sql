CREATE TABLE family_benefit.verify_email_code (

  "id_owner" NUMERIC NOT NULL,
  "code" NUMERIC NOT NULL,
  "date_expiration" TIMESTAMP NOT NULL,
  "for_user" BIT NOT NULL,

  CONSTRAINT verify_email_code_pk PRIMARY KEY ("id_owner", "for_user"),
  CONSTRAINT verify_email_code_uniq_token UNIQUE ("code"),
  CONSTRAINT verify_email_code_fk_user FOREIGN KEY ("id_owner")
    REFERENCES family_benefit.user("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT verify_email_code_fk_admin FOREIGN KEY ("id_owner")
    REFERENCES family_benefit.admin("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN family_benefit.verify_email_code.id_owner IS 'ID владельца';
COMMENT ON COLUMN family_benefit.verify_email_code.code IS 'Код подтверждения почты';
COMMENT ON COLUMN family_benefit.verify_email_code.date_expiration IS 'Время истечения срока кода';
COMMENT ON COLUMN family_benefit.verify_email_code.for_user IS 'true, если для пользователя';
