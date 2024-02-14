package org.factoria.demo.imageUser;

import jakarta.validation.Valid;
import org.factoria.demo.imageUser.converter.UserDtoToUserConverter;
import org.factoria.demo.imageUser.converter.UserToUserDtoConverter;
import org.factoria.demo.imageUser.dto.UserDto;
import org.factoria.demo.system.Result;
import org.factoria.demo.system.StatusCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class UserController {
    private final UserService userService;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final UserDtoToUserConverter userDtoToUserConverter;

    public UserController(UserService userService, UserToUserDtoConverter userToUserDtoConverter, UserDtoToUserConverter userDtoToUserConverter) {
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.userDtoToUserConverter = userDtoToUserConverter;
    }

    @GetMapping
    public Result findAllUsers() {
        List<ImageUser> foundUsers = this.userService.findAll();
        List<UserDto> userDto = foundUsers.stream()
                .map(this.userToUserDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", userDto);
    }

    @GetMapping("/{userId}")
    public Result findUserById(@PathVariable Long userId){
        ImageUser foundUser = this.userService.findById(userId);
        UserDto userDto = this.userToUserDtoConverter.convert(foundUser);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", userDto);
    }

    /**
     * @param user
     * @return
     */

    @PostMapping
    public Result addUser(@Valid @RequestBody ImageUser user) {
        ImageUser savedUser = this.userService.save(user);
        UserDto savedUserDto = this.userToUserDtoConverter.convert(savedUser);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedUserDto );
    }

    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable Long userId, @Valid @RequestBody UserDto userDto) {
        ImageUser updatedUser = this.userDtoToUserConverter.convert(userDto);
        ImageUser result = this.userService.update(userId, updatedUser);

        UserDto updatedUserDto = this.userToUserDtoConverter.convert(result);
        return new Result(true, StatusCode.SUCCESS, "Updated Success", updatedUserDto );
    }

    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable Long userId) {
        this.userService.delete(userId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
