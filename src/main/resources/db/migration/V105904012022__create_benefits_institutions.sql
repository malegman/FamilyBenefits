CREATE TABLE familybenefit.benefits_institutions (

  "id_benefit" BIGINT NOT NULL,
  "id_institution" BIGINT NOT NULL,

  CONSTRAINT benefits_institutions_pk PRIMARY KEY ("id_benefit", "id_institution"),
  CONSTRAINT benefits_institutions_fk_benefit FOREIGN KEY ("id_benefit")
    REFERENCES familybenefit.benefit("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT benefits_institutions_fk_institution FOREIGN KEY ("id_institution")
    REFERENCES familybenefit.institution("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN familybenefit.benefits_institutions.id_benefit IS 'ID пособия';
COMMENT ON COLUMN familybenefit.benefits_institutions.id_institution IS 'ID учреждения';
