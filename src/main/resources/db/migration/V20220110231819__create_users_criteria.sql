CREATE TABLE familybenefit.users_criteria (

  "id_user" NUMERIC NOT NULL,
  "id_criterion" NUMERIC NOT NULL,

  CONSTRAINT users_criteria_pk PRIMARY KEY ("id_user", "id_criterion"),
  CONSTRAINT users_criteria_fk_user FOREIGN KEY ("id_user")
    REFERENCES familybenefit.user("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT users_criteria_fk_criterion FOREIGN KEY ("id_criterion")
    REFERENCES familybenefit.criterion("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN familybenefit.users_criteria.id_user IS 'ID пользователя';
COMMENT ON COLUMN familybenefit.users_criteria.id_criterion IS 'ID критерия';
