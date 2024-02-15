package org.factoria.demo.imageUser.converter;

import org.factoria.demo.imageUser.ImageUser;
import org.factoria.demo.imageUser.dto.UserDto;
import org.factoria.demo.myFavoriteImage.converter.MyFavoriteImageToMyFavoriteImageDtoConverter;
import org.factoria.demo.myFavoriteImage.dto.MyFavoriteImageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserToUserDtoConverter implements Converter<ImageUser, UserDto> {
    @Autowired
    MyFavoriteImageToMyFavoriteImageDtoConverter imageConverter;

    @Override
    public UserDto convert(ImageUser source) {
        List<MyFavoriteImageDto> myFavoriteImageDtos = source.getImages().stream()
                .map(myFavoriteImage -> imageConverter.convert(myFavoriteImage))
                .toList();
        UserDto userDto = new UserDto(source.getId(),
                source.getName(),
                source.isEnabled(),
                source.getRoles(),
                myFavoriteImageDtos);
        return userDto;
    }
}
