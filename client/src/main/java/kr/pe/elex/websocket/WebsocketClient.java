/*
 * Copyright (c) 2021. Elex. All Rights Reserved.
 * https://www.elex-project.com/
 */

package kr.pe.elex.websocket;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

@Slf4j
@ClientEndpoint
public class WebsocketClient {
	private Session session = null;
	private WebsocketMessageHandler messageHandler;

	public WebsocketClient(URI endpointURI) {
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, endpointURI);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Callback hook for Connection open events.
	 *
	 * @param session the userSession which is opened.
	 */
	@OnOpen
	public void onOpen(@NotNull Session session) throws IOException {
		log.info("opening websocket");
		this.session = session;
		//session.getBasicRemote().sendText("Hi~~");
	}

	/**
	 * Callback hook for Connection close events.
	 *
	 * @param session the userSession which is getting closed.
	 * @param reason  the reason for connection close
	 */
	@OnClose
	public void onClose(Session session, CloseReason reason) {
		log.info("closing websocket");
		this.session = null;
	}

	/**
	 * Callback hook for Message Events. This method will be invoked when a client send a message.
	 *
	 * @param message The text message
	 */
	@OnMessage
	public void onMessage(String message) {
		if (this.messageHandler != null) {
			this.messageHandler.handleMessage(message);
		}
	}

	@OnMessage
	public void onMessage(PongMessage message) {
		if (this.messageHandler != null) {
			this.messageHandler.handleMessage("pong: " + message.toString());
		}
	}

	/**
	 * register message handler
	 *
	 * @param msgHandler
	 */
	public void setMessageHandler(WebsocketMessageHandler msgHandler) {
		this.messageHandler = msgHandler;
	}

	/**
	 * Send a message.
	 *
	 * @param message
	 */
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

	public void sendMessage(ByteBuffer message) throws IOException {
		this.session.getBasicRemote().sendBinary(message);
	}

	public void sendMessageAsync(String message) {
		this.session.getAsyncRemote().sendText(message);
	}

	public void sendMessageAsync(ByteBuffer message) {
		this.session.getAsyncRemote().sendBinary(message);
	}

	public static void main(String... args) throws URISyntaxException, IOException {
		WebsocketClient client = new WebsocketClient(new URI("ws://localhost:8080/websocket"));
		client.setMessageHandler(new WebsocketMessageHandler() {
			@Override
			public void handleMessage(final String message) {
				log.info("Rx: {}", message);
			}
		});
		client.sendMessage("Hello");
	}
}
