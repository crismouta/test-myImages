package org.factoria.demo.myFavoriteImage.converter;

import org.factoria.demo.myFavoriteImage.MyFavoriteImage;
import org.factoria.demo.myFavoriteImage.dto.MyFavoriteImageDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MyFavoriteImageDtoToMyFavoriteImageConverter implements Converter<MyFavoriteImageDto, MyFavoriteImage> {
    @Override
    public MyFavoriteImage convert(MyFavoriteImageDto source) {
        MyFavoriteImage myFavoriteImage = new MyFavoriteImage();
        myFavoriteImage.setId(source.id());
        myFavoriteImage.setTitle(source.title());
        myFavoriteImage.setDescription(source.description());
        myFavoriteImage.setUrl(source.imageUrl());
        return myFavoriteImage;
    }
}
