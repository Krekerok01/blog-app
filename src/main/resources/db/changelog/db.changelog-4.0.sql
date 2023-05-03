-- liquibase formatted sql

-- changeset vmamatsiuk:1
create table if not exists post_comments (
  comment_id bigserial not null,
  app_user_id bigint not null,
  author_username varchar(50) not null,
  post_id bigint not null,
  message varchar(150) not null,

  constraint post_comments_pk primary key(comment_id),
  constraint post_commetns_fk foreign key (post_id) references posts(post_id)
 );