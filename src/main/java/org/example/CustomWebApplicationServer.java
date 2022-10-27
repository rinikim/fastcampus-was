package org.example;


import org.example.calculatebyinterface.Calculator;
import org.example.calculatebyinterface.PositiveNumber;
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

                    /**
                     *  아래와 같은 값 출력
                     * GET /calculate?operand1=11&operator=*&operand2=55 HTTP/1.1
                     * Host: localhost:8080
                     * Connection: Keep-Alive
                     * User-Agent: Apache-HttpClient/4.5.13 (Java/11.0.15)
                     * Accept-Encoding: gzip,deflate
                     */
                    String line;
                    while ((line = br.readLine()) != "") {
                        System.out.println(line);
                    }

                    // br.readLine() -> 첫번째는 requestLine 이 출력된다.
                    // ex : GET /calculate?operand1=11&operator=*&operand2=55 HTTP/1.1
                    HttpRequest httpRequest = new HttpRequest(br);

                    if (httpRequest.isGetRequest() && httpRequest.matchPath("calculate")) {
                        QueryStrings queryStrings = httpRequest.getQueryString();

                        int operand1 = Integer.parseInt(queryStrings.getValue("operand1"));
                        String operator = queryStrings.getValue("operator");
                        int operand2 = Integer.parseInt(queryStrings.getValue("operand2"));

                        int result = Calculator.calculate(new PositiveNumber(operand1), operator, new PositiveNumber(operand2));
                        logger.info("result : " + result);
                        byte[] body = String.valueOf(result).getBytes();

                        HttpResponse response = new HttpResponse(dos);
                        response.response200Header("application/json", body.length);
                        response.responseBody(body);

                    }
                }
            }
        }
    }
}
