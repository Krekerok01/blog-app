package com.krekerok.blogapp.repository;

import com.krekerok.blogapp.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

}
