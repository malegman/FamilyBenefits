CREATE TABLE familybenefit.benefit (

  "id" NUMERIC NOT NULL,
  "name" TEXT NOT NULL,
  "info" TEXT NOT NULL,
  "documents" TEXT NULL,

  CONSTRAINT benefit_pk PRIMARY KEY ("id"),
  CONSTRAINT benefit_uniq_name UNIQUE ("name")
);

COMMENT ON COLUMN familybenefit.benefit.id IS 'ID пособия';
COMMENT ON COLUMN familybenefit.benefit.name IS 'Название пособия';
COMMENT ON COLUMN familybenefit.benefit.info IS 'Информация пособия';
COMMENT ON COLUMN familybenefit.benefit.documents IS 'Документы для получения пособия';
