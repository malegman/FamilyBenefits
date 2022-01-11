CREATE TABLE familybenefit.child (

  -- Диапазон ID [1 000 000 000; 9 999 999 999]
  "id" NUMERIC NOT NULL DEFAULT (10000000000 - 1000000000) * random() + 1000000000,
  "date_birth" TIMESTAMP NOT NULL,

  CONSTRAINT child_pk PRIMARY KEY ("id"),
  CONSTRAINT child_uniq_birth UNIQUE ("date_birth")
);

COMMENT ON COLUMN familybenefit.child.id IS 'ID ребенка';
COMMENT ON COLUMN familybenefit.child.date_birth IS 'Дата рождения ребенка';
