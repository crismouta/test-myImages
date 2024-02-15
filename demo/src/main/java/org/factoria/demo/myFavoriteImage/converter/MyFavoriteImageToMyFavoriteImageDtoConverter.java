package org.factoria.demo.myFavoriteImage.converter;

import org.factoria.demo.myFavoriteImage.MyFavoriteImage;
import org.factoria.demo.myFavoriteImage.dto.MyFavoriteImageDto;
import org.factoria.demo.imageUser.converter.UserToUserDtoConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MyFavoriteImageToMyFavoriteImageDtoConverter implements Converter<MyFavoriteImage, MyFavoriteImageDto> {
    @Override
    public MyFavoriteImageDto convert(MyFavoriteImage source) {
        MyFavoriteImageDto myFavoriteImageDto = new MyFavoriteImageDto(source.getId(), source.getTitle(), source.getDescription(), source.getUrl(),
                source.getOwner() != null ? source.getOwner().getName() : null);
        return myFavoriteImageDto;
    }
}
