package org.factoria.demo.imageUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.factoria.demo.imageUser.dto.UserDto;
import org.factoria.demo.system.StatusCode;
import org.factoria.demo.system.exception.ObjectByIdNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    MockMvc myMockMvc;
    @MockBean
    UserService userService;
    @Autowired
    ObjectMapper objectMapper;
    List<ImageUser> users;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        this.users = new ArrayList<>();

        ImageUser user1 = new ImageUser();
        user1.setId(Long.valueOf("1"));
        user1.setName("User1");
        user1.setSurname("Surname1");
        user1.setPassword("123456");
        user1.setEnabled(true);
        user1.setRoles("user");
        this.users.add(user1);

        ImageUser user2 = new ImageUser();
        user2.setId(Long.valueOf("2"));
        user2.setName("User2");
        user2.setSurname("Surname2");
        user2.setPassword("123456");
        user2.setEnabled(true);
        user2.setRoles("user");
        this.users.add(user2);

        ImageUser user3 = new ImageUser();
        user3.setId(Long.valueOf("3"));
        user3.setName("User3");
        user3.setSurname("Surname3");
        user3.setPassword("123456");
        user3.setEnabled(true);
        user3.setRoles("user");
        this.users.add(user3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllUsersSuccess() throws Exception {
        //Given
        given(this.userService.findAll()).willReturn(this.users);

        //When and then
        this.myMockMvc.perform(get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.users.size())))
                .andExpect(jsonPath("$.data[0].id").value(Long.valueOf("1")))
                .andExpect(jsonPath("$.data[0].name").value("User1"))
                .andExpect(jsonPath("$.data[1].id").value(Long.valueOf("2")))
                .andExpect(jsonPath("$.data[1].name").value("User2"));
    }

    @Test
    void testFindUserByIdSuccess() throws Exception {
        //Given
        given(this.userService.findById(Long.valueOf("1"))).willReturn(this.users.get(0));

        //When and then
        this.myMockMvc.perform(get(this.baseUrl + "/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(Long.valueOf("1")))
                .andExpect(jsonPath("$.data.name").value("User1"));
    }

    @Test
    void testFindUserByIdNotFound() throws Exception {
        //Given
        given(this.userService.findById(Long.valueOf("1"))).willThrow(new ObjectByIdNotFoundException("user", Long.valueOf("1")));

        //When and then
        this.myMockMvc.perform(get(this.baseUrl + "/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id: 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testSaveUserSuccess() throws Exception {
        //Given
        ImageUser user = new ImageUser();
        user.setId(4L);
        user.setName("User 4");
        user.setPassword("654321");
        user.setEnabled(true);
        user.setRoles("user");
        String json = this.objectMapper.writeValueAsString(user);

        given(this.userService.save(Mockito.any(ImageUser.class))).willReturn(user);

        //When and then
        this.myMockMvc.perform(post(this.baseUrl + "/users").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(user.getName()))
                .andExpect(jsonPath("$.data.enabled").value(user.isEnabled()))
                .andExpect(jsonPath("$.data.roles").value(user.getRoles()));
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        //Given
        UserDto userDto = new UserDto(Long.valueOf("1"),
                "User1",
                true,
                "user",
                null);
        String json = this.objectMapper.writeValueAsString(userDto);

        ImageUser updatedUser = new ImageUser();
        updatedUser.setId(Long.valueOf("1"));
        updatedUser.setName("User1");
        updatedUser.setEnabled(true);
        updatedUser.setRoles("admin user");

        given(this.userService.update(eq(Long.valueOf("1")), Mockito.any(ImageUser.class))).willReturn(updatedUser);

        //When and then
        this.myMockMvc.perform(put(this.baseUrl + "/users/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Updated Success"))
                .andExpect(jsonPath("$.data.id").value(Long.valueOf("1")))
                .andExpect(jsonPath("$.data.name").value(updatedUser.getName()))
                .andExpect(jsonPath("$.data.enabled").value(updatedUser.isEnabled()))
                .andExpect(jsonPath("$.data.roles").value(updatedUser.getRoles()));
    }

    @Test
    void testUpdateUserErrorWithNonExistentId() throws Exception {
        //Given
        UserDto userDto = new UserDto(Long.valueOf("1"),
                "User1",
                true,
                "user",
                null);
        String json = this.objectMapper.writeValueAsString(userDto);

        given(this.userService.update(eq(Long.valueOf("1")), Mockito.any(ImageUser.class))).willThrow(new ObjectByIdNotFoundException("user", Long.valueOf("1")));

        //When and then
        this.myMockMvc.perform(put(this.baseUrl + "/users/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id: 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        //Given
        doNothing().when(this.userService).delete(Long.valueOf("1"));

        //When and then
        this.myMockMvc.perform(delete(this.baseUrl + "/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUserErrorWithNonExistentId() throws Exception {
        //Given
        doThrow(new ObjectByIdNotFoundException("user", Long.valueOf("1"))).when(this.userService).delete(Long.valueOf("1"));

        //When and then
        this.myMockMvc.perform(delete(this.baseUrl + "/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id: 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
