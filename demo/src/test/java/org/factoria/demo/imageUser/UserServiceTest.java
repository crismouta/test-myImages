package org.factoria.demo.imageUser;

import org.factoria.demo.system.exception.ObjectByIdNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Mock
    PasswordEncoder encoder;

    List<ImageUser> imageUsers;

    @BeforeEach
    void setUp() {
        ImageUser user1 = new ImageUser();
        user1.setId(Long.valueOf("1"));
        user1.setName("User1");
        user1.setSurname("Surname1");
        user1.setPassword("123456");
        user1.setEnabled(true);
        user1.setRoles("user");

        ImageUser user2 = new ImageUser();
        user2.setId(Long.valueOf("2"));
        user2.setName("User2");
        user2.setSurname("Surname2");
        user2.setPassword("123456");
        user2.setEnabled(true);
        user2.setRoles("user");

        ImageUser user3 = new ImageUser();
        user3.setId(Long.valueOf("3"));
        user3.setName("User3");
        user3.setSurname("Surname3");
        user3.setPassword("123456");
        user3.setEnabled(true);
        user3.setRoles("user");

        this.imageUsers = new ArrayList<>();
        this.imageUsers.add(user1);
        this.imageUsers.add(user2);
        this.imageUsers.add(user3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAllSuccess() {
        //Given
        given(userRepository.findAll()).willReturn(this.imageUsers);

        //When
        List<ImageUser> actualUsers = userService.findAll();

        //Then
        assertThat(actualUsers.size()).isEqualTo(this.imageUsers.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findByIdSuccess() {
        //Given
        ImageUser user1 = new ImageUser();
        user1.setId(Long.valueOf("1"));
        user1.setName("User1");
        user1.setSurname("Surname1");
        user1.setPassword("123456");
        user1.setEnabled(true);
        user1.setRoles("user");

        given(userRepository.findById(Long.valueOf("1"))).willReturn(Optional.of(user1));

        //When
        ImageUser returnedUser = userService.findById(Long.valueOf("1"));

        //Then
        assertThat(returnedUser.getId()).isEqualTo(user1.getId());
        assertThat(returnedUser.getName()).isEqualTo(user1.getName());
        assertThat(returnedUser.getPassword()).isEqualTo(user1.getPassword());
        assertThat(returnedUser.isEnabled()).isEqualTo(user1.isEnabled());
        assertThat(returnedUser.getRoles()).isEqualTo(user1.getRoles());
        verify(this.userRepository, times(1)).findById(Long.valueOf("1"));
    }

    @Test
    void testFindByIdNotFound() {
        //Given
        given(userRepository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());

        //When
        Throwable thrown = catchThrowable(()-> {
            ImageUser returnedUser = userService.findById(Long.valueOf("1"));
        });

        //Then
        assertThat(thrown).isInstanceOf(ObjectByIdNotFoundException.class).hasMessage("Could not find user with id: 1");
        verify(userRepository, times(1)).findById(Long.valueOf("1"));
    }

    @Test
    void testSaveSuccess(){
        //Given
        ImageUser newUser = new ImageUser();
        newUser.setName("New User 1");
        newUser.setSurname("New Surname 1");
        newUser.setPassword("123456");
        newUser.setEnabled(true);
        newUser.setRoles("user");

        given(userRepository.save(newUser)).willReturn(newUser);

        //When
        ImageUser returnedUser = userService.save(newUser);

        //Then
        assertThat(returnedUser.getName()).isEqualTo(newUser.getName());
        assertThat(returnedUser.getPassword()).isEqualTo(newUser.getPassword());
        assertThat(returnedUser.isEnabled()).isEqualTo(newUser.isEnabled());
        assertThat(returnedUser.getRoles()).isEqualTo(newUser.getRoles());
        verify(this.userRepository, times(1)).save(newUser);
    }

    @Test
    void testUpdateSuccess() {
        //Given
        ImageUser oldUser = new ImageUser();
        oldUser.setName("New User 1");
        oldUser.setSurname("New Surname 1");
        oldUser.setPassword("123456");
        oldUser.setEnabled(true);
        oldUser.setRoles("user");

        ImageUser updatedUser = new ImageUser();
        updatedUser.setName("New User 1");
        updatedUser.setSurname("New Surname 1");
        updatedUser.setPassword("123456");
        updatedUser.setEnabled(true);
        updatedUser.setRoles("admin user");

        given(userRepository.findById(Long.valueOf("1"))).willReturn(Optional.of(oldUser));
        given(userRepository.save(oldUser)).willReturn(oldUser);

        //When
        ImageUser result = userService.update(Long.valueOf("1"), updatedUser);

        //Then
        assertThat(result.getId()).isEqualTo(updatedUser.getId());
        assertThat(result.getName()).isEqualTo(updatedUser.getName());
        assertThat(result.getSurname()).isEqualTo(updatedUser.getSurname());
        verify(userRepository, times(1)).findById(Long.valueOf("1"));
        verify(userRepository, times(1)).save(oldUser);
    }

    @Test
    void testUpdateNotFound() {
        //Given
        ImageUser updatedUser = new ImageUser();
        updatedUser.setName("New User 1");
        updatedUser.setSurname("New Surname 1");
        updatedUser.setPassword("123456");
        updatedUser.setEnabled(true);
        updatedUser.setRoles("admin user");

        given(userRepository.findById(Long.valueOf("1"))).willReturn(Optional.empty());

        //When
        Throwable thrown = assertThrows(ObjectByIdNotFoundException.class, () -> {
            userService.update(Long.valueOf("1"), updatedUser);
        });

        //Then
        assertThat(thrown).isInstanceOf(ObjectByIdNotFoundException.class).hasMessage("Could not find user with id: 1");
        verify(userRepository, times(1)).findById(Long.valueOf("1"));
    }

    @Test
    void testDeleteSuccess() {
        //Given
        ImageUser user = new ImageUser();
        user.setId(Long.valueOf("1"));
        user.setName("User1");
        user.setSurname("Surname1");
        user.setPassword("123456");
        user.setEnabled(true);
        user.setRoles("user");

        given(userRepository.findById(Long.valueOf("1"))).willReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(Long.valueOf("1"));

        //When
        userService.delete(Long.valueOf("1"));

        //Then
        verify(userRepository, times(1)).deleteById(Long.valueOf("1"));
    }

    @Test
    void testDeleteNotFound() {
        //Given
        given(userRepository.findById(Long.valueOf("1"))).willReturn(Optional.empty());

        //When
        assertThrows(ObjectByIdNotFoundException.class,() -> {
            userService.delete(Long.valueOf("1"));
        });

        //Then
        verify(userRepository, times(1)).findById(Long.valueOf("1"));
    }
}