package server.feign.server;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientUserDto {
    private String username;
    private String profileImage;
}

