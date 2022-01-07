CREATE TABLE familybenefit.child (

  "id" NUMERIC NOT NULL,
  "date_birth" TIMESTAMP NOT NULL,

  CONSTRAINT child_pk PRIMARY KEY ("id"),
  CONSTRAINT child_uniq_birth UNIQUE ("date_birth")
);

COMMENT ON COLUMN familybenefit.child.id IS 'ID ребенка';
COMMENT ON COLUMN familybenefit.child.date_birth IS 'Дата рождения ребенка';
