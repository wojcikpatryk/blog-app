package com.wojcikjer.blog.controllers;

import com.wojcikjer.blog.configuration.securityUser.UserContext;
import com.wojcikjer.blog.entities.Comment;
import com.wojcikjer.blog.entities.Post;
import com.wojcikjer.blog.entities.User;
import com.wojcikjer.blog.services.CommentService;
import com.wojcikjer.blog.services.PostService;
import com.wojcikjer.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping({"/dashboard", "/"})
    public ModelAndView getDashboard() {

        User user = userService.findByUsername(UserContext.getCurrentUserUsername());

        List<Post> posts = postService.findAllPostsByFollowingUsers(user.getFollowing());
        posts.addAll(user.getPosts());
        Collections.sort(posts, Collections.reverseOrder());

        ModelAndView model = new ModelAndView("dashboard");
        model.addObject("posts", posts);
        model.addObject("post", new Post());
        model.addObject("comment", new Comment());
        model.addObject("username", new String());
        model.addObject("currentUsername", UserContext.getCurrentUserUsername());

        return model;
    }

    @PostMapping("/dashboard")
    public String addPost(@ModelAttribute Post post) {

        post.setUser(userService.findByUsername(UserContext.getCurrentUserUsername()));
        postService.save(post);

        return "redirect:/dashboard";
    }

    @GetMapping("/removePostFromDashboard")
    public String removePost(@RequestParam long id) {

        postService.removePostById(id);

        return "redirect:/dashboard";
    }

    @PostMapping("/addComment")
    public String addComment(@RequestParam long post,
                             @ModelAttribute Comment comment) {

        comment.setPost(postService.findPostById(post));
        comment.setUser(userService.findByUsername(UserContext.getCurrentUserUsername()));

        commentService.save(comment);

        return "redirect:/dashboard";
    }

    @GetMapping("/removeCommentFromDashboard")
    public String removeComment(@RequestParam long id) {

        commentService.removeCommentById(id);

        return "redirect:/dashboard";
    }
}
