CREATE TABLE family_benefit.benefits_criteria (

  "id_benefit" TEXT NOT NULL,
  "id_criterion" TEXT NOT NULL,

  CONSTRAINT benefits_criteria_pk PRIMARY KEY ("id_benefit", "id_criterion"),
  CONSTRAINT benefits_criteria_fk_benefit FOREIGN KEY ("id_benefit")
    REFERENCES family_benefit.benefit("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT benefits_criteria_fk_institution FOREIGN KEY ("id_criterion")
    REFERENCES family_benefit.criterion("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN family_benefit.benefits_criteria.id_benefit IS 'ID пособия';
COMMENT ON COLUMN family_benefit.benefits_criteria.id_criterion IS 'ID критерия';
