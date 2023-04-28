# TodoFormData schema

# --- !Ups
CREATE TABLE IF NOT EXISTS `assign`.`baseForm` (
   `id`       VARCHAR(20) NOT NULL,
  `employeeID` VARCHAR(11) NOT NULL,
  `companyID` VARCHAR(45) NULL DEFAULT NULL,
  `details` VARCHAR(15) NULL DEFAULT NULL,
  PRIMARY KEY (`id `)
  )
DEFAULT CHARACTER SET = utf8

# --- !Downs
drop table 'baseForm'