package com.wojcikjer.blog.services;

import com.wojcikjer.blog.entities.Comment;
import com.wojcikjer.blog.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public void removeCommentById(long id) {
        commentRepository.deleteById(id);
    }

}
