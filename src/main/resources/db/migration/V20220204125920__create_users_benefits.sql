CREATE TABLE family_benefit.users_benefits (

  "id_user" TEXT NOT NULL,
  "id_benefit" TEXT NOT NULL,

  CONSTRAINT users_benefits_pk PRIMARY KEY ("id_user", "id_benefit"),
  CONSTRAINT users_benefits_fk_user FOREIGN KEY ("id_user")
    REFERENCES family_benefit.user("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT users_benefits_fk_benefit FOREIGN KEY ("id_benefit")
    REFERENCES family_benefit.benefit("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN family_benefit.users_benefits.id_user IS 'ID пользователя';
COMMENT ON COLUMN family_benefit.users_benefits.id_benefit IS 'ID пособия';
