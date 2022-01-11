CREATE TABLE familybenefit.benefits_criterions (

"id_benefit" NUMERIC NOT NULL,
"id_criterion" NUMERIC NOT NULL,

CONSTRAINT benefits_criterions_pk PRIMARY KEY ("id_benefit", "id_criterion"),
CONSTRAINT benefits_criterions_fk_benefit FOREIGN KEY ("id_benefit")
  REFERENCES familybenefit.benefit("id")
  ON DELETE CASCADE
  ON UPDATE CASCADE,
CONSTRAINT benefits_criterions_fk_institution FOREIGN KEY ("id_criterion")
  REFERENCES familybenefit.criterion("id")
  ON DELETE CASCADE
  ON UPDATE CASCADE
);

COMMENT ON COLUMN familybenefit.benefits_criterions.id_benefit IS 'ID пособия';
COMMENT ON COLUMN familybenefit.benefits_criterions.id_criterion IS 'ID критерия';
