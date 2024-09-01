package server.kafka;

import javax.websocket.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.zip.InflaterInputStream;

@ClientEndpoint
public class UpbitWebSocketClient {

    private static final String WS_URI = "wss://api.upbit.com/websocket/v1";

    public static void main(String[] args) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            container.connectToServer(UpbitWebSocketClient.class, URI.create(WS_URI));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to Upbit WebSocket server");

        try {
            // 서버로 메시지 전송 (티커 데이터 구독)
            String message = "[{\"ticket\":\"test\"},{\"type\":\"ticker\",\"codes\":[\"KRW-BTC\",\"KRW-ETH\"],\"isOnlyRealtime\":true}]";
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(ByteBuffer message) {
        // 수신된 바이너리 메시지를 해제압축하고 출력
        String decompressedMessage = decompress(message.array());
        System.out.println("Received: " + decompressedMessage);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("Error: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("WebSocket closed: " + reason);
    }

    private String decompress(byte[] data) {
        try (InflaterInputStream inflater = new InflaterInputStream(new ByteArrayInputStream(data))) {
            StringBuilder result = new StringBuilder();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inflater.read(buffer)) != -1) {
                result.append(new String(buffer, 0, length));
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}