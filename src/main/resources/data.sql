-- Default password for all role is '1234'
INSERT INTO `role` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_OWNER'),(3,'ROLE_USER');
insert into
   `user` (id, account_non_locked, created_date, display_name, email_id, enabled, first_name, last_name, modified_date, password, secret_code, two_factor_auth) 
values
   (
      1, TRUE, NOW(), 'AMS', 'amsadmin@gmail.com', TRUE, 'Admin', 'Admin', NOW(), '$2a$10$mrADhR6WtB4xVPSbe2SytO2Dy0aNkLextmR30JjU1L6LLAQlSoh9G', 'https://chart.googleapis.com/chart?chs=200x200&chld=M%7C0&cht=qr&chl=otpauth%3A%2F%2Ftotp%2FAadhar%2520Management%2520System%3Aamsadmin%40gmail.com%3Fsecret%3DNRUMXMBCXOMM5LQX%26issuer%3DAadhar%2BManagement%2BSystem%26algorithm%3DSHA1%26digits%3D6%26period%3D30', FALSE
   ),

   (
      2, TRUE, NOW(), 'Demo_Owner', 'amsowner@gmail.com', TRUE, 'Demo', 'Owner', NOW(), '$2a$10$mrADhR6WtB4xVPSbe2SytO2Dy0aNkLextmR30JjU1L6LLAQlSoh9G', 'https://chart.googleapis.com/chart?chs=200x200&chld=M%7C0&cht=qr&chl=otpauth%3A%2F%2Ftotp%2FAadhar%2520Management%2520System%3Aamsadmin%40gmail.com%3Fsecret%3DNRUMXMBCXOMM5LQX%26issuer%3DAadhar%2BManagement%2BSystem%26algorithm%3DSHA1%26digits%3D6%26period%3D30', FALSE
   ),

   (
      3, TRUE, NOW(), 'Demo_User', 'amsuser@gmail.com', TRUE, 'Demo', 'User', NOW(), '$2a$10$mrADhR6WtB4xVPSbe2SytO2Dy0aNkLextmR30JjU1L6LLAQlSoh9G', 'https://chart.googleapis.com/chart?chs=200x200&chld=M%7C0&cht=qr&chl=otpauth%3A%2F%2Ftotp%2FAadhar%2520Management%2520System%3Aamsadmin%40gmail.com%3Fsecret%3DNRUMXMBCXOMM5LQX%26issuer%3DAadhar%2BManagement%2BSystem%26algorithm%3DSHA1%26digits%3D6%26period%3D30', FALSE
   );
   

insert into users_roles (user_id, role_id) values (1, 1), (2, 2), (3, 3);
