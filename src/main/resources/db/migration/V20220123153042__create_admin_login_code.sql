CREATE TABLE family_benefit.admin_login_code (

  "id_admin" NUMERIC NOT NULL,
  "code" NUMERIC NOT NULL,
  "date_expiration" TIMESTAMP NOT NULL,

  CONSTRAINT admin_login_code_pk PRIMARY KEY ("id_admin"),
  CONSTRAINT admin_login_code_token UNIQUE ("code"),
  CONSTRAINT admin_login_code_fk_user FOREIGN KEY ("id_admin")
    REFERENCES family_benefit.admin("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN family_benefit.admin_login_code.id_admin IS 'ID администратора';
COMMENT ON COLUMN family_benefit.admin_login_code.code IS 'Код для входа в систему';
COMMENT ON COLUMN family_benefit.admin_login_code.date_expiration IS 'Время истечения срока кода';
