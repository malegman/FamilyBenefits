CREATE TABLE family_benefit.role (

  "id" TEXT NOT NULL DEFAULT family_benefit.generate_id(20),
  "name" TEXT NOT NULL,

  CONSTRAINT role_pk PRIMARY KEY ("id"),
  CONSTRAINT role_uniq_name UNIQUE ("name")
);

COMMENT ON COLUMN family_benefit.role.id IS 'ID роли';
COMMENT ON COLUMN family_benefit.role.name IS 'Название роли';
