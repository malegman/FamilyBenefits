CREATE TABLE family_benefit.admin (

  -- Диапазон ID [1 000 000 000; 9 999 999 999]
  "id" NUMERIC NOT NULL DEFAULT (10000000000 - 1000000000) * random() + 1000000000,
  "name" TEXT NOT NULL,
  "email" TEXT NOT NULL,
  "password" TEXT NOT NULL,
  "is_verified_email" BIT NOT NULL DEFAULT FALSE,

  CONSTRAINT admin_pk PRIMARY KEY ("id"),
  CONSTRAINT admin_uniq_email UNIQUE ("email")
);

COMMENT ON COLUMN family_benefit.admin.id IS 'ID пользователя';
COMMENT ON COLUMN family_benefit.admin.name IS 'Имя пользователя';
COMMENT ON COLUMN family_benefit.admin.email IS 'Эл. почта пользователя';
COMMENT ON COLUMN family_benefit.admin.password IS 'Хэш пароля пользователя';
COMMENT ON COLUMN family_benefit.admin.is_verified_email IS 'Статус подтверждения почты';
