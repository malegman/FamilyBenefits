CREATE TABLE familybenefit.users_children (

  "id_user" NUMERIC NOT NULL,
  "id_child" NUMERIC NOT NULL,

  CONSTRAINT users_children_pk PRIMARY KEY ("id_user", "id_child"),
  CONSTRAINT users_children_fk_user FOREIGN KEY ("id_user")
    REFERENCES familybenefit.user("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT users_children_fk_child FOREIGN KEY ("id_child")
    REFERENCES familybenefit.child("id")
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMENT ON COLUMN familybenefit.users_children.id_user IS 'ID пользователя';
COMMENT ON COLUMN familybenefit.users_children.id_child IS 'ID ребенка';
