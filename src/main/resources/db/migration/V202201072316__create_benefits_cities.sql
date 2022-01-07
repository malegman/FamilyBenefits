CREATE TABLE familybenefit.benefits_cities (

  "id_benefit" NUMERIC NOT NULL,
  "id_city" NUMERIC NOT NULL,

  CONSTRAINT benefits_cities_pk PRIMARY KEY ("id_benefit", "id_city"),
  CONSTRAINT benefits_cities_fk_benefit FOREIGN KEY ("id_benefit")
    REFERENCES familybenefit.benefit("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT benefits_cities_fk_city FOREIGN KEY ("id_city")
    REFERENCES familybenefit.city("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN familybenefit.benefits_cities.id_benefit IS 'ID пособия';
COMMENT ON COLUMN familybenefit.benefits_cities.id_city IS 'ID города';
