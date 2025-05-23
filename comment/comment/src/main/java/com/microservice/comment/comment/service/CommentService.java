package com.microservice.comment.comment.service;

import com.microservice.comment.comment.entity.Comment;
import com.microservice.comment.comment.payload.Post;
import com.microservice.comment.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Comment saveComment(Comment comment) {
        // Check if the post exists by calling post service
        Post post = restTemplate.getForObject(
                "http://post-service/api/posts/"+comment.getPostId(), Post.class // "http://localhost:8081/api/posts/"+comment.getPostId(), Post.class
        );

        if (post != null) {
            String commentId = UUID.randomUUID().toString();
            comment.setCommentId(commentId);
            return commentRepository.save(comment);
        } else {
            throw new RuntimeException("Post not found with ID: " + comment.getPostId());
        }
    }

    public List<Comment> getAllCommentByPostId(String postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments;
    }
}
