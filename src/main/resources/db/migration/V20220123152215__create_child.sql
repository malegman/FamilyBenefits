CREATE TABLE family_benefit.child (

  -- Диапазон ID [1 000 000 000; 9 999 999 999]
  "id" NUMERIC NOT NULL DEFAULT (10000000000 - 1000000000) * random() + 1000000000,
  "date_birth" DATE NOT NULL,

  CONSTRAINT child_pk PRIMARY KEY ("id"),
  CONSTRAINT child_uniq_birth UNIQUE ("date_birth")
);

COMMENT ON COLUMN family_benefit.child.id IS 'ID ребенка';
COMMENT ON COLUMN family_benefit.child.date_birth IS 'Дата рождения ребенка';
