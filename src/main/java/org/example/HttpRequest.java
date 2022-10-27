package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.http.HttpHeaders;

public class HttpRequest {
    private final RequestLine requestLine;

    // 현재 프로젝트에서는 중요하지 않아 생략
//    private final HttpHeaders httpHeaders;
//    private final Body body;


    public HttpRequest(BufferedReader br) throws IOException {
        this.requestLine = new RequestLine(br.readLine());
    }

    public boolean isGetRequest() {
        // requestLine 가 http method 를 갖고 있기 때문에 해당 객체에 묻는다.
        return requestLine.isGetRequest();

    }

    public boolean matchPath(String requestPath) {
        return requestLine.matchPath(requestPath);
    }

    public QueryStrings getQueryString() {
        return requestLine.getQueryStrings();
    }




}
