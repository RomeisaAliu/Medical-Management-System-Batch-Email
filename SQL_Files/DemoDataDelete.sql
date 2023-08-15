
use medical_management_system;
#delete
SET SQL_SAFE_UPDATES = 0;
DELETE FROM `appointments`;
DELETE FROM `user_speciality`;
DELETE FROM user_role;
DELETE FROM `users`;
DELETE FROM `speciality`;
DELETE FROM `role`;
DELETE FROM `user_notification_mapping`;
DELETE FROM `user_notification_type`;



