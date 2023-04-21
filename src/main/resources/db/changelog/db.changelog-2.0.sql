-- liquibase formatted sql

-- changeset vmamatsiuk:1
create table if not exists post_likes (
  like_id bigserial not null,
  app_user_id bigint not null,
  post_id bigint not null,

  constraint post_likes_pk primary key(like_id),
  constraint post_likes_fk foreign key (post_id) references posts(post_id)
 );





