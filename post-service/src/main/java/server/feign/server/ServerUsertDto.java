package server.feign.server;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ServerUsertDto {
    private String username;
    private String profileImage;
}
