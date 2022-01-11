CREATE TABLE familybenefit.benefit (

  -- Диапазон ID [1 000 000 000; 9 999 999 999]
  "id" NUMERIC NOT NULL DEFAULT (10000000000 - 1000000000) * random() + 1000000000,
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
