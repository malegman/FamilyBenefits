ALTER TABLE familybenefit.child ADD CONSTRAINT child_uniq_birth UNIQUE ("date_birth");
ALTER TABLE familybenefit.institution ADD CONSTRAINT child_uniq_name UNIQUE ("name");
