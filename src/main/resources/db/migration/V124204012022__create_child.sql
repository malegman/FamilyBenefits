CREATE TABLE familybenefit.child (

  "id" BIGINT NOT NULL,
  "date_birth" DATE NOT NULL,

  CONSTRAINT child_pk PRIMARY KEY ("id")
);

COMMENT ON COLUMN familybenefit.child.id IS 'ID ребенка';
COMMENT ON COLUMN familybenefit.child.date_birth IS 'Дата рождения ребенка';
