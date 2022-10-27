package org.example;

import java.util.Objects;

/**
 *  받아온 정보(bufferReader) 중에 첫번째는 requestLine 이다.
 * GET /calculate?operand1=11&operator=*&operand2=55 HTTP/1.1 -> requestLine
 * Host: localhost:8080
 * Connection: Keep-Alive
 * User-Agent: Apache-HttpClient/4.5.13 (Java/11.0.15)
 * Accept-Encoding: gzip,deflate
 */
public class RequestLine {

    private final String method;    // GET
    private final String urlPath;   // /calculate
    private QueryStrings queryString;    // operand1=11&operator=*&operand2=55

    public RequestLine(String requestLine) {
        String[] tokens = requestLine.split(" ");
        this.method = tokens[0];
        String[] urlPathTokens = tokens[1].split("\\?");
        this.urlPath = urlPathTokens[0];

        if (urlPathTokens.length == 2) {
            this.queryString = new QueryStrings(urlPathTokens[1]);
        }
    }

    public RequestLine(String method, String urlPath, String queryString) {
        this.method = method;
        this.urlPath = urlPath;
        this.queryString = new QueryStrings(queryString);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestLine)) return false;
        RequestLine that = (RequestLine) o;
        return Objects.equals(method, that.method) && Objects.equals(urlPath, that.urlPath) && Objects.equals(queryString, that.queryString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, urlPath, queryString);
    }

    public boolean isGetRequest() {
        return "GET".equals(this.method);
    }

    public boolean matchPath(String requestPath) {
        return this.urlPath.equals(requestPath);
    }

    public QueryStrings getQueryStrings() {
        return this.queryString;
    }
}
