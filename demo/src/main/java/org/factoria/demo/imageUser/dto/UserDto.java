package org.factoria.demo.imageUser.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserDto(Long id,
                      @NotEmpty(message = "name is required")
                      String name,
                      boolean enabled,
                      @NotEmpty(message = "roles are required")
                      String roles,
                      Integer numberOfImages) {
}
