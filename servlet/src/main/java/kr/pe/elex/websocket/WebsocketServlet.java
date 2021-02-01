/*
 * Copyright (c) 2021. Elex. All Rights Reserved.
 * https://www.elex-project.com/
 */

package kr.pe.elex.websocket;

import org.jetbrains.annotations.NotNull;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/")
public class WebsocketServlet {
	private static final Set<Session> sessions;

	static {
		sessions = new HashSet<>();
	}

	@OnOpen
	public void onOpen(@NotNull Session session) throws IOException {
		sessions.add(session);//session.
		session.getBasicRemote().sendText("Hello");
		session.getUserProperties().put("createdOn", LocalDateTime.now());
	}

	@OnClose
	public void onClose(Session session) {
		sessions.remove(session);
		session = null;
	}

	@OnMessage
	public void onMessage(@NotNull Session session, String message) throws IOException {
		session.getBasicRemote().sendText("you said, " + message);
	}

	@OnError
	public void onError(Session session, Throwable e) {

	}

	public static void broadcast(final String message){
		sessions.forEach(session -> {
			if (null!=session && session.isOpen()) {
				session.getAsyncRemote().sendText(message);
			}
		});
	}
}
