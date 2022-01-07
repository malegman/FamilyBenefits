CREATE TABLE familybenefit.users_benefits (

  "id_user" NUMERIC NOT NULL,
  "id_benefit" NUMERIC NOT NULL,

  CONSTRAINT users_benefits_pk PRIMARY KEY ("id_user", "id_benefit"),
  CONSTRAINT users_benefits_fk_user FOREIGN KEY ("id_user")
    REFERENCES familybenefit.user("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT users_benefits_fk_benefit FOREIGN KEY ("id_benefit")
    REFERENCES familybenefit.benefit("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN familybenefit.users_benefits.id_user IS 'ID пользователя';
COMMENT ON COLUMN familybenefit.users_benefits.id_benefit IS 'ID пособия';
