package Socket;

import org.springframework.stereotype.Repository;

@Repository
public class SocketRepository {

    private final StringBuilder sb = new StringBuilder();

    public StringBuilder addMessage(Object message) {
        this.sb.append(message);
        return sb;
    }
}
