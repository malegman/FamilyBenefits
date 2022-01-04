CREATE TABLE familybenefit.users_criterions (

  "id_user" BIGINT NOT NULL,
  "id_criterion" BIGINT NOT NULL,

  CONSTRAINT users_criterions_pk PRIMARY KEY ("id_user", "id_criterion"),
  CONSTRAINT users_criterions_fk_user FOREIGN KEY ("id_user")
    REFERENCES familybenefit.user("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT users_criterions_fk_criterion FOREIGN KEY ("id_criterion")
    REFERENCES familybenefit.criterion("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN familybenefit.users_criterions.id_user IS 'ID пользователя';
COMMENT ON COLUMN familybenefit.users_criterions.id_criterion IS 'ID критерия';
