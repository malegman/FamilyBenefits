CREATE TABLE familybenefit.refresh_token (

  "id_user" NUMERIC NOT NULL,
  "token" TEXT NOT NULL,
  "date_expiration" TIMESTAMP NOT NULL,

  CONSTRAINT refresh_token_pk PRIMARY KEY ("id_user"),
  CONSTRAINT refresh_token_uniq_token UNIQUE ("token"),
  CONSTRAINT refresh_token_fk_user FOREIGN KEY ("id_user")
    REFERENCES familybenefit.user("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN familybenefit.refresh_token.id_user IS 'ID пользователя';
COMMENT ON COLUMN familybenefit.refresh_token.token IS 'Токен обновления токена доступа';
COMMENT ON COLUMN familybenefit.refresh_token.date_expiration IS 'Время истечения срока токена';
