CREATE TABLE familybenefit.resource_token (

  "id_user" NUMERIC NOT NULL,
  "token" NUMERIC NOT NULL,

  CONSTRAINT resource_token_pk PRIMARY KEY ("id_user"),
  CONSTRAINT resource_token_uniq_token UNIQUE ("token"),
  CONSTRAINT resource_token_fk_user FOREIGN KEY ("id_user")
    REFERENCES familybenefit.user("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN familybenefit.resource_token.id_user IS 'ID пользователя';
COMMENT ON COLUMN familybenefit.resource_token.token IS 'Токен ресурсов';
