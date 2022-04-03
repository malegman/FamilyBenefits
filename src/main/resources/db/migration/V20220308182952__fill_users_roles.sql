INSERT INTO family_benefit.users_roles
SELECT family_benefit.user.id, family_benefit.role.id
FROM family_benefit.user, family_benefit.role
WHERE family_benefit.user.email = 'smegovic@gmail.com' AND
  (family_benefit.role.id = 'ID_SUPER_ADMIN' OR
   family_benefit.role.id = 'ID_ADMIN');
