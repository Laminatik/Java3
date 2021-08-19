package java3.chat.client.model;

import java3.chat.clientserver.Command;

public interface ReadCommandListener {

    void processReceivedCommand(Command command);
}
