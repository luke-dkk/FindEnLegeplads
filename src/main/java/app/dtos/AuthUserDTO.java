package app.dtos;

import java.util.Set;

public record AuthUserDTO(
        Integer id,
        String email,
        Set<String> roles
) {}