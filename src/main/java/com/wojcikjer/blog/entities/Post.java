package com.wojcikjer.blog.entities;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Post implements Comparable<Post> {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @DateTimeFormat(pattern = "dd/mm/yyyy")
    private Date createdAt;

    @OneToMany(mappedBy = "post")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Comment> comments = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        setCreatedAt(new Date());
    }

    @Override
    public int compareTo(Post post) {
        return this.getCreatedAt().compareTo(post.getCreatedAt());
    }
}

