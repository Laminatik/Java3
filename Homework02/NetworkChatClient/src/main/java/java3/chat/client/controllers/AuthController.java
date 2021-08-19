package java3.chat.client.controllers;

import java3.chat.client.ClientChat;
import java3.chat.client.dialogs.Dialogs;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java3.chat.client.model.Network;
import java3.chat.client.model.ReadCommandListener;
import java3.chat.clientserver.Command;
import java3.chat.clientserver.CommandType;
import java3.chat.clientserver.commands.AuthOkCommandData;

import java.io.IOException;

public class AuthController {

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button authButton;
    private ReadCommandListener readMessageListener;


    @FXML
    public void executeAuth(ActionEvent actionEvent) {
        String login = loginField.getText();
        String password = passwordField.getText();
        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            Dialogs.AuthError.EMPTY_CREDENTIALS.show();
            return;
        }

        if (!connectToServer()) {
            Dialogs.NetworkError.SERVER_CONNECT.show();
            return;
        }

        try {
            Network network = Network.getInstance();
            network.setLastLogin(login);
            network.setLastPassword(password);
            network.sendAuthMessage(login, password);
        } catch (IOException e) {
            Dialogs.NetworkError.SEND_MESSAGE.show();
            e.printStackTrace();
        }
    }

    private boolean connectToServer() {
        Network network = getNetwork();
        return network.isConnected() || network.connect();
    }

    private Network getNetwork() {
        return Network.getInstance();
    }

    public void initMessageHandler() {
        readMessageListener = getNetwork().addReadMessageListener(new ReadCommandListener() {
            @Override
            public void processReceivedCommand(Command command) {
                if (command.getType() == CommandType.AUTH_OK) {
                    AuthOkCommandData data = (AuthOkCommandData) command.getData();
                    String username = data.getUsername();
                    Network.getInstance().setCurrentUsername(username);
                    Platform.runLater(() -> ClientChat.INSTANCE.switchToMainChatWindow(username));
                } else {
                    Platform.runLater(Dialogs.AuthError.INVALID_CREDENTIALS::show);
                }
            }
        });
    }

    public void close() {
        getNetwork().removeReadMessageListener(readMessageListener);
    }
}