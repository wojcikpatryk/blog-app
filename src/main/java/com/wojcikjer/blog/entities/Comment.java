package com.wojcikjer.blog.entities;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Comment implements Comparable<Comment> {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String content;

    @ManyToOne
    private Post post;

    @DateTimeFormat(pattern = "dd/mm/yyyy")
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @PrePersist
    public void prePersist() {
        setCreatedAt(new Date());
    }

    @Override
    public int compareTo(Comment comment) {
        return this.createdAt.compareTo(comment.createdAt);
    }
}
