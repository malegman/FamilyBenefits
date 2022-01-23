CREATE TABLE family_benefit.benefits_cities (

  "id_benefit" NUMERIC NOT NULL,
  "id_city" NUMERIC NOT NULL,

  CONSTRAINT benefits_cities_pk PRIMARY KEY ("id_benefit", "id_city"),
  CONSTRAINT benefits_cities_fk_benefit FOREIGN KEY ("id_benefit")
    REFERENCES family_benefit.benefit("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT benefits_cities_fk_city FOREIGN KEY ("id_city")
    REFERENCES family_benefit.city("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN family_benefit.benefits_cities.id_benefit IS 'ID пособия';
COMMENT ON COLUMN family_benefit.benefits_cities.id_city IS 'ID города';
