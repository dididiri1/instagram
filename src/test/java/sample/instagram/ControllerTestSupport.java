package sample.instagram;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import sample.instagram.api.controller.image.ImageApiController;
import sample.instagram.api.controller.member.MemberApiController;
import sample.instagram.api.controller.subscribe.SubscribeApiController;
import sample.instagram.api.service.image.ImageService;
import sample.instagram.api.service.member.MemberService;
import sample.instagram.api.service.subscribe.SubscribeService;

@WebMvcTest(controllers = {
        MemberApiController.class,
        SubscribeApiController.class,
        ImageApiController.class
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

}
