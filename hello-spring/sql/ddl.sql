create table member
(
 id bigint generated by default as identity, // bigint = Long 타
 name varchar(255),
 primary key (id)
);