CREATE TABLE family_benefit.users_criteria (

  "id_user" TEXT NOT NULL,
  "id_criterion" TEXT NOT NULL,

  CONSTRAINT users_criteria_pk PRIMARY KEY ("id_user", "id_criterion"),
  CONSTRAINT users_criteria_fk_user FOREIGN KEY ("id_user")
    REFERENCES family_benefit.user("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT users_criteria_fk_criterion FOREIGN KEY ("id_criterion")
    REFERENCES family_benefit.criterion("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN family_benefit.users_criteria.id_user IS 'ID пользователя';
COMMENT ON COLUMN family_benefit.users_criteria.id_criterion IS 'ID критерия';
