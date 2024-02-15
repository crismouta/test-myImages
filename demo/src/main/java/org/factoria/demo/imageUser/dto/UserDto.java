package org.factoria.demo.imageUser.dto;

import jakarta.validation.constraints.NotEmpty;
import org.factoria.demo.myFavoriteImage.dto.MyFavoriteImageDto;

import java.util.List;

public record UserDto(Long id,
                      @NotEmpty(message = "name is required")
                      String name,
                      boolean enabled,
                      @NotEmpty(message = "roles are required")
                      String roles,
                      List<MyFavoriteImageDto> myFavoriteImages) {
}
