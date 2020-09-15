package io.manbang.easybytecoder.plugin.restrecord.runtime;


import io.manbang.easybytecoder.runtimecommonapi.utils.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


/**
 * @author GaoYang 2018/12/23
 */
public class RequestInterceptorWrapper extends HttpServletRequestWrapper {

    private HttpServletRequest originalRequest = null;

    private byte[] body;

    private HashMap<String, String> requestHeaders;
    private URI uri;
    private String httpMethod;
    /**
     * doGet,queryParams
     */
    private String doGetQueryParams;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request the {@link HttpServletRequest} to be wrapped.
     * @throws IllegalArgumentException if the request is null
     */
    public RequestInterceptorWrapper(HttpServletRequest request)
            throws IOException, URISyntaxException {
        super(request);
        originalRequest = request;
        init();
    }

    private void init() throws IOException, URISyntaxException {
        body = getOriginBody();
        httpMethod = originalRequest.getMethod();
        uri = new URI(originalRequest.getRequestURI());
        requestHeaders = parseRequestHeaders();
        if (StringUtils.isNotEmpty(originalRequest.getQueryString())) {
            doGetQueryParams = originalRequest.getQueryString();
        }
    }

    private byte[] getOriginBody() {
        return getBodyString().getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public String getHeader(String name) {
        //DoomResourceObjectPool.getDoomLogger().error("record node, try to force dyeing to doom-record " + name);

        return originalRequest.getHeader(name);

    }

    private String getBodyString() {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = originalRequest.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    private HashMap<String, String> parseRequestHeaders() {
        final Enumeration<String> headerNames = originalRequest.getHeaderNames();
        HashMap<String, String> headersResult = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            final String name = headerNames.nextElement();
            final Enumeration<String> ve = originalRequest.getHeaders(name);
            while (ve.hasMoreElements()) {
                headersResult.put(name, ve.nextElement());
            }
        }
        return headersResult;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    public String getHeaderMsg() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
            stringBuilder
                    .append("key：")
                    .append(entry.getKey())
                    .append(", value：")
                    .append(entry.getValue());
        }
        return stringBuilder.toString();
    }

    public HttpServletRequest getOriginalRequest() {
        return originalRequest;
    }

    public void setOriginalRequest(HttpServletRequest originalRequest) {
        this.originalRequest = originalRequest;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public HashMap<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(HashMap<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getDoGetQueryParams() {
        return doGetQueryParams;
    }

    public void setDoGetQueryParams(String doGetQueryParams) {
        this.doGetQueryParams = doGetQueryParams;
    }
}
