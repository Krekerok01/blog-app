package com.krekerok.blogapp.repository;

import com.krekerok.blogapp.entity.Blog;
import com.krekerok.blogapp.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByBlog(Blog blog);
}
