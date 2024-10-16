package org.example.finalproject_backend;

import org.example.finalproject_backend.dto.UserDto;
import org.example.finalproject_backend.entity.Role;
import org.example.finalproject_backend.entity.User;
import org.example.finalproject_backend.mapper.UserMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();



    @Test
    public void testUserEntityToDto() {
        User user = new User();
        user.setUserId(1L);
        user.setFirstName("Pepek");
        user.setLastName("Kumar");
        user.setEmail("pepek@example.com");

        user.setRole(Role.USER);
        UserDto userDto = userMapper.userEntityToDto(user);

        assertEquals(user.getUserId(), userDto.getUserId());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getEmail(), userDto.getEmail());

        assertEquals(user.getRole(), userDto.getRole());
    }

}
