package org.factoria.demo.myFavoriteImage.dto;

import jakarta.validation.constraints.NotEmpty;
import org.factoria.demo.imageUser.dto.UserDto;

public record MyFavoriteImageDto(Long id,
                                 @NotEmpty(message = "title is required")
                                 String title,
                                 @NotEmpty(message = "description is required")
                                 String description,
                                 @NotEmpty(message = "url is required")
                                 String imageUrl,
                                 UserDto owner) {
}
