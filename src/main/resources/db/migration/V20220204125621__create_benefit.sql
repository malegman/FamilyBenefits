CREATE TABLE family_benefit.benefit (

  "id" TEXT NOT NULL DEFAULT family_benefit.generate_id(20),
  "name" TEXT NOT NULL,
  "info" TEXT NOT NULL,
  "documents" TEXT NOT NULL,

  CONSTRAINT benefit_pk PRIMARY KEY ("id"),
  CONSTRAINT benefit_uniq_name UNIQUE ("name")
);

COMMENT ON COLUMN family_benefit.benefit.id IS 'ID пособия';
COMMENT ON COLUMN family_benefit.benefit.name IS 'Название пособия';
COMMENT ON COLUMN family_benefit.benefit.info IS 'Информация пособия';
COMMENT ON COLUMN family_benefit.benefit.documents IS 'Документы для получения пособия';
