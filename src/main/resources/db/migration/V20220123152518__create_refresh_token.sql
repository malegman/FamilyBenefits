CREATE TABLE family_benefit.refresh_token (

  "id_owner" NUMERIC NOT NULL,
  "token" TEXT NOT NULL,
  "date_expiration" TIMESTAMP NOT NULL,
  "for_user" BIT NOT NULL,

  CONSTRAINT refresh_token_pk PRIMARY KEY ("id_owner", "for_user"),
  CONSTRAINT refresh_token_uniq_token UNIQUE ("token"),
  CONSTRAINT refresh_token_fk_user FOREIGN KEY ("id_owner")
    REFERENCES family_benefit.user("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT refresh_token_fk_admin FOREIGN KEY ("id_owner")
    REFERENCES family_benefit.admin("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN family_benefit.refresh_token.id_owner IS 'ID владельца';
COMMENT ON COLUMN family_benefit.refresh_token.token IS 'Токен обновления токена доступа';
COMMENT ON COLUMN family_benefit.refresh_token.date_expiration IS 'Время истечения срока токена';
COMMENT ON COLUMN family_benefit.refresh_token.for_user IS 'true, если для пользователя';
