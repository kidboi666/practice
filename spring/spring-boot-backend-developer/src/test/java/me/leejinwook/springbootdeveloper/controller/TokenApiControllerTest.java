package me.leejinwook.springbootdeveloper.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import me.leejinwook.springbootdeveloper.config.jwt.JwtFactory;
import me.leejinwook.springbootdeveloper.config.jwt.JwtProperties;
import me.leejinwook.springbootdeveloper.domain.RefreshToken;
import me.leejinwook.springbootdeveloper.domain.User;
import me.leejinwook.springbootdeveloper.dto.CreateAccessTokenRequest;
import me.leejinwook.springbootdeveloper.repository.RefreshTokenRepository;
import me.leejinwook.springbootdeveloper.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
public class TokenApiControllerTest {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  JwtProperties jwtProperties;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private WebApplicationContext context;

  @BeforeEach
  public void mockMvcSetUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    userRepository.deleteAll();
  }

  @DisplayName("createNewAccessToken: 새로운 액세스 토큰을 발급한다.")
  @Test
  public void createNewAccessToken() throws Exception {
    // given
    final String url = "/api/token";

    User testUser = userRepository.save(
        User.builder().email("user@gmail.com").password("test").build());

    String refreshToken = JwtFactory.builder().claims(Map.of("id", testUser.getId())).build()
        .createToken(jwtProperties);

    refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToken));

    CreateAccessTokenRequest request = new CreateAccessTokenRequest();
    request.setRefreshToken(refreshToken);
    final String requestBody = objectMapper.writeValueAsString(request);

    // when
    ResultActions resultActions = mockMvc.perform(
        post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(requestBody));

    // then
    resultActions.andExpect(status().isCreated()).andExpect(jsonPath("$.accessToken").isNotEmpty());
  }
}
