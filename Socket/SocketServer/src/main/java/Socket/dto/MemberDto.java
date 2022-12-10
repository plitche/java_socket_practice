package Socket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {

    private String id;
    private String password;
    private String name;
    private String type;

    public MemberDto(String id, String password, String name, String type) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.type = type;
    }
}
