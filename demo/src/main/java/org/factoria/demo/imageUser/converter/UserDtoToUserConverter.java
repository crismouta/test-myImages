package org.factoria.demo.imageUser.converter;

import org.factoria.demo.imageUser.ImageUser;
import org.factoria.demo.imageUser.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, ImageUser> {
    @Override
    public ImageUser convert(UserDto source) {
        ImageUser imageUser = new ImageUser();
        imageUser.setName(source.name());
        imageUser.setEnabled(source.enabled());
        imageUser.setRoles(source.roles());
        return imageUser;
    }
}
