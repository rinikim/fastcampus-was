package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CustomWebApplicationServer {
    private final int port;

    private static final Logger logger = LoggerFactory.getLogger(CustomWebApplicationServer.class);

    public CustomWebApplicationServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        // 해당 포트가 서버소켓을 만들고, 서버소켓이 클라이언트를 기다리게 합니다.
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("[CustomWebApplicationServer] started {} port.", port);

            Socket clientSocket;
            logger.info("[CustomWebApplicationServer] waiting for client {} port.", port);

            while ((clientSocket = serverSocket.accept()) != null) {
                logger.info("[CustomWebApplicationServer] client connected.");

                /**
                 * step1 - 사용자 요청을 메인 Thread가 처리하도록 한다.
                 */

                try (InputStream in = clientSocket.getInputStream(); OutputStream out = clientSocket.getOutputStream()) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                    DataOutputStream dos = new DataOutputStream(out);

                    String line;
                    while ((line = br.readLine()) != "") {
                        System.out.println(line);
                    }
                }
            }
        }
    }
}
