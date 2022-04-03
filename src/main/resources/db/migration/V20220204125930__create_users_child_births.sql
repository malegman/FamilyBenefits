CREATE TABLE family_benefit.users_child_births(

  "id_user" TEXT NOT NULL,
  "id_child_birth" TEXT NOT NULL,

  CONSTRAINT users_child_births_pk PRIMARY KEY ("id_user", "id_child_birth"),
  CONSTRAINT users_child_births_fk_user FOREIGN KEY ("id_user")
    REFERENCES family_benefit.user("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT users_child_births_fk_child FOREIGN KEY ("id_child_birth")
    REFERENCES family_benefit.child("id")
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

COMMENT ON COLUMN family_benefit.users_child_births.id_user IS 'ID пользователя';
COMMENT ON COLUMN family_benefit.users_child_births.id_child_birth IS 'ID рождения ребенка';
