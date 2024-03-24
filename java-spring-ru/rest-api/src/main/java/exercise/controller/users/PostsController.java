package exercise.controller.users;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import exercise.model.Post;
import exercise.Data;

// BEGIN
@RestController
@RequestMapping("/api")
public class PostsController {

    private List<Post> posts = Data.getPosts();

    @GetMapping("/users/{id}/posts")
    public List<Post>getUserIdPosts(@PathVariable int id){
        
        var UserIdPosts = posts.stream()
                .filter(p -> p.getUserId() == id)
                .collect(Collectors.toList());
        
        return UserIdPosts;
    }

    @PostMapping("/users/{id}/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post createUserIdPost(@PathVariable int id, @RequestBody Post post){
        
        Post userIdPost = new Post();
        
        userIdPost.setUserId(id);
        userIdPost.setSlug(post.getSlug());
        userIdPost.setTitle(post.getTitle());
        userIdPost.setBody(post.getBody());

        posts.add(userIdPost);
        
        return userIdPost;
    }
}
// END
