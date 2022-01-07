CREATE TABLE familybenefit.role (

  "id" NUMERIC NOT NULL,
  "name" TEXT NOT NULL,
  "priority" INT NOT NULL,

  CONSTRAINT role_pk PRIMARY KEY ("id"),
  CONSTRAINT role_uniq_name UNIQUE ("name")
);

COMMENT ON COLUMN familybenefit.role.id IS 'ID роли';
COMMENT ON COLUMN familybenefit.role.name IS 'Название роли';
COMMENT ON COLUMN familybenefit.role.priority IS 'Приоритет роли';
