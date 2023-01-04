package Socket;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class SocketRepository {

    private StringBuilder sb = new StringBuilder();

    public SocketRepository() {
    }

    public StringBuilder addMessage(String message) {
        message.replaceAll("\\{", "")
                .replaceAll("}", "");

        if (!this.sb.equals("")) this.sb.append("/");
        this.sb.append(message);
        return sb;
    }
}
