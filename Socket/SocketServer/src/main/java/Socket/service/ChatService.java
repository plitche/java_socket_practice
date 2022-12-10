package Socket.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    private List<String> applyMemberList = new ArrayList<>();

    public int applyChat(String memberId) {
        this.applyMemberList.add(memberId);
        return this.applyMemberList.size();
    }
}
