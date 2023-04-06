-- liquibase formatted sql

-- changeset vmamatsiuk:1
create table if not exists blogs (
  blog_id bigserial not null,
  blog_name varchar(40) not null,
  created_at timestamp not null,
  modified_at timestamp not null,

  constraint blogs_pk primary key(blog_id)
 );


-- changeset vmamatsiuk:2
create table if not exists roles (
  role_id bigserial not null,
  role_name varchar(40) not null unique,
  constraint roles_pk primary key(role_id)
 );


-- changeset vmamatsiuk:3
create table if not exists app_users (
  user_id bigserial not null,
  username varchar(50) not null unique,
  password varchar(60) not null,
  email varchar(256) not null unique,
  blog_id bigint,
  created_at timestamp,

  constraint users_pk primary key(user_id),
  constraint app_users_fk_blogs foreign key (blog_id) references blogs(blog_id) on delete cascade
 );

-- changeset vmamatsiuk:4
create table if not exists app_users_roles (
  app_user_id bigint not null,
  role_id bigint not null,

  constraint users_roles_fk_users foreign key(app_user_id) references app_users(user_id),
  constraint users_roles_fk_roles foreign key(role_id) references roles(role_id)
 );

-- changeset vmamatsiuk:5
create table if not exists posts (
  post_id bigserial not null,
  header varchar(60) not null,
  text varchar(2500) not null,
  image_url varchar(200) not null,
  created_at timestamp not null,
  modified_at timestamp not null,
  blog_id bigint not null,

  constraint posts_pk primary key(post_id),
  constraint posts_fk_blogs foreign key (blog_id) references blogs(blog_id)
 );





