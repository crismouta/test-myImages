package org.factoria.demo.imageUser.converter;

import org.factoria.demo.imageUser.ImageUser;
import org.factoria.demo.imageUser.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<ImageUser, UserDto> {
    @Override
    public UserDto convert(ImageUser source) {
        UserDto userDto = new UserDto(source.getId(),
                source.getName(),
                source.isEnabled(),
                source.getRoles(),
                source.getNumberOfImages());
        return userDto;
    }
}
