package mr.demonid.web.client.services.events;

import java.util.UUID;

public interface WebSocketService {

    void sendMessage(UUID userId, String message);

}
