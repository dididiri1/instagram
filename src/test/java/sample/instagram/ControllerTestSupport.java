package sample.instagram;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import sample.instagram.controller.api.comment.CommentApiController;
import sample.instagram.controller.api.image.ImageApiController;
import sample.instagram.controller.api.member.MemberApiController;
import sample.instagram.controller.api.subscribe.SubscribeApiController;
import sample.instagram.service.comment.CommentService;
import sample.instagram.service.image.ImageService;
import sample.instagram.service.like.LikeService;
import sample.instagram.service.member.MemberService;
import sample.instagram.service.subscribe.SubscribeService;

@WebMvcTest(controllers = {
        MemberApiController.class,
        SubscribeApiController.class,
        ImageApiController.class,
        CommentApiController.class
})
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected SubscribeService subscribeService;

    @MockBean
    protected ImageService imageService;

    @MockBean
    protected LikeService likeService;

    @MockBean
    protected CommentService commentService;

}
