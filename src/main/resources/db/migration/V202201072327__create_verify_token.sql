CREATE TABLE familybenefit.verify_token (

  "id_user" NUMERIC NOT NULL,
  "token" NUMERIC NOT NULL,
  "date_expiration" TIMESTAMP NOT NULL,

  CONSTRAINT verify_token_pk PRIMARY KEY ("id_user"),
  CONSTRAINT verify_token_uniq_token UNIQUE ("token"),
  CONSTRAINT resource_token_fk_user FOREIGN KEY ("id_user")
    REFERENCES familybenefit.user("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN familybenefit.verify_token.id_user IS 'ID пользователя';
COMMENT ON COLUMN familybenefit.verify_token.token IS 'Токен подтверждения';
COMMENT ON COLUMN familybenefit.verify_token.date_expiration IS 'Время истечения срока токена';
