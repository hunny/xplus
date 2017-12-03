package com.example.bootweb.websocket.handler;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.example.bootweb.websocket.profile.WebSocketSessionDemo;

/**
 * Interceptor for WebSocket handshake requests. Can be used to inspect the
 * handshake request and response as well as to pass attributes to the target
 * {@link WebSocketHandler}.
 * 
 */
@WebSocketSessionDemo
public class WebSocketSessionHandShakeInterceptor implements HandshakeInterceptor {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * Invoked before the handshake is processed.
   * 
   * @param request
   *          the current request
   * @param response
   *          the current response
   * @param wsHandler
   *          the target WebSocket handler
   * @param attributes
   *          attributes from the HTTP handshake to associate with the WebSocket
   *          session; the provided attributes are copied, the original map is not
   *          used.
   * @return whether to proceed with the handshake ({@code true}) or abort
   *         ({@code false})
   */
  @Override
  public boolean beforeHandshake(ServerHttpRequest request, //
      ServerHttpResponse response, //
      WebSocketHandler handler, //
      Map<String, Object> attributes) throws Exception {
    if (request instanceof ServletServerHttpRequest) {
      ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
      HttpSession session = servletRequest.getServletRequest().getSession();
      logger.info("接收到请求, sessionId={}, attributes={}。", session.getId(), attributes);
      attributes.put("sessionId", session.getId());
    }
    return true;
  }

  /**
   * Invoked after the handshake is done. The response status and headers indicate
   * the results of the handshake, i.e. whether it was successful or not.
   * 
   * @param request
   *          the current request
   * @param response
   *          the current response
   * @param wsHandler
   *          the target WebSocket handler
   * @param exception
   *          an exception raised during the handshake, or {@code null} if none
   */
  @Override
  public void afterHandshake(ServerHttpRequest request, //
      ServerHttpResponse response, //
      WebSocketHandler handler, //
      Exception arg3) {

  }

}
