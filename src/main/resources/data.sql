insert into member(username, password, email, name, role, create_date_time, modified_date_time)
values ('testUser', '$2a$10$1IzU6gG71SEZxZpjB6He4u4TM/UJMTcDfAJ4rt7RaH0maXJb1Lf4u', 'kmkim6368@gmail.com', '김강민', 'ROLE_USER', NOW(), NOW()),
       ('testUser2', '$2a$10$1IzU6gG71SEZxZpjB6He4u4TM/UJMTcDfAJ4rt7RaH0maXJb1Lf4u', 'dididiri1@naver.com', '김구라', 'ROLE_USER', NOW(), NOW());