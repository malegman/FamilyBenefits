CREATE TABLE family_benefit.users_roles (

  "id_user" NUMERIC NOT NULL,
  "id_role" NUMERIC NOT NULL,

  CONSTRAINT users_roles_pk PRIMARY KEY ("id_user", "id_role"),
  CONSTRAINT users_roles_fk_user FOREIGN KEY ("id_user")
    REFERENCES family_benefit.user("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT users_roles_fk_role FOREIGN KEY ("id_role")
    REFERENCES family_benefit.role("id")
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

COMMENT ON COLUMN family_benefit.users_roles.id_user IS 'ID пользователя';
COMMENT ON COLUMN family_benefit.users_roles.id_role IS 'ID роли';
