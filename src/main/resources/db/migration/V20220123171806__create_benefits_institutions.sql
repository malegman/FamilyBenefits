CREATE TABLE family_benefit.benefits_institutions (

  "id_benefit" NUMERIC NOT NULL,
  "id_institution" NUMERIC NOT NULL,

  CONSTRAINT benefits_institutions_pk PRIMARY KEY ("id_benefit", "id_institution"),
  CONSTRAINT benefits_institutions_fk_benefit FOREIGN KEY ("id_benefit")
    REFERENCES family_benefit.benefit("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT benefits_institutions_fk_institution FOREIGN KEY ("id_institution")
    REFERENCES family_benefit.institution("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN family_benefit.benefits_institutions.id_benefit IS 'ID пособия';
COMMENT ON COLUMN family_benefit.benefits_institutions.id_institution IS 'ID учреждения';
