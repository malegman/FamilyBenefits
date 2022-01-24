CREATE TABLE family_benefit.role (

  -- Диапазон ID [1 000 000 000; 9 999 999 999]
  "id" NUMERIC NOT NULL DEFAULT (10000000000 - 1000000000) * random() + 1000000000,
  "name" TEXT NOT NULL,

  CONSTRAINT role_pk PRIMARY KEY ("id"),
  CONSTRAINT role_uniq_name UNIQUE ("name")
);

COMMENT ON COLUMN family_benefit.role.id IS 'ID роли';
COMMENT ON COLUMN family_benefit.role.name IS 'Название роли';
