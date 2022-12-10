package Socket.service;

import Socket.dto.MemberDto;
import Socket.dto.MemberType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {

    private Integer seq = 0;
    private List<MemberDto> memberDtoList = new ArrayList<>();

    public MemberService() {
        this.memberDtoList.add(new MemberDto("plitche", "abcd1234!", "yskwon", MemberType.ADMIN.getValue()));
        this.memberDtoList.add(new MemberDto("ferdy", "abcd1234!", "yonsoo", MemberType.USER.getValue()));
    }

    public MemberDto getMemberInfoById(String id) throws Exception {
        return this.memberDtoList.stream()
                .findFirst().filter(m -> m.getId().equals(id))
                .orElseThrow(() -> new Exception("not exists id ERROR"));
    }

    public MemberDto login(String id, String password) throws Exception {
        MemberDto memberDto = getMemberInfoById(id);
        if(!memberDto.getPassword().equals(password)) new Exception("wrong password ERROR");
        return memberDto;
    }

    public MemberDto join(String id, String password, String name) throws Exception {
        MemberDto memberDto = getMemberInfoById(id);
        if(memberDto != null) new Exception("already exists id ERROR");
        else this.memberDtoList.add(new MemberDto(id, password, name, MemberType.USER.getValue()));
        return getMemberInfoById(id);
    }

}
