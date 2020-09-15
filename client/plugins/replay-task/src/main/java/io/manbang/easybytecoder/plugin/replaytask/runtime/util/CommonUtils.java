package io.manbang.easybytecoder.plugin.replaytask.runtime.util;


import io.manbang.easybytecoder.plugin.replaytask.runtime.model.RESTMirrorData;
import io.manbang.easybytecoder.runtimecommonapi.utils.EasyByteCoderResourceObjectPool;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class CommonUtils {

    private static CloseableHttpClient httpClient;
    private static PoolingHttpClientConnectionManager cm;

    static {
        init();
        closeExpiredConnectionsPeriodTask(60);
    }

    static void init() {
        cm = new PoolingHttpClientConnectionManager();
        // max connections
        cm.setMaxTotal(200);
        // max connections per route
        cm.setDefaultMaxPerRoute(2);
        // set max connections for a specified route
       // cm.setMaxPerRoute(new HttpRoute(null), 50);

        final RequestConfig requestConfig = RequestConfig.custom()
                // the socket timeout (SO_TIMEOUT) in milliseconds
                .setSocketTimeout(5000)
                // the timeout in milliseconds until a connection is established.
                .setConnectTimeout(5000)
                // the timeout in milliseconds used when requesting a connection from the connection pool.
                .setConnectionRequestTimeout(5000)
                .build();
        httpClient = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(requestConfig).build();
    }

    public String sendHttpReqForMirror(RESTMirrorData restMirrorData) {
        HashMap<String, String> headers = restMirrorData.getRequestHeaders();
        String stringbody = restMirrorData.getRequestBody();
        headers.put("traceId", restMirrorData.getTraceId());
        String result = null;
        if ("GET".equalsIgnoreCase(restMirrorData.getReqMethod())) {
            result = httpGet("127.0.0.1", 8088,
                    restMirrorData.getRequestUri(), restMirrorData.getRequestQueryParams(), headers);
        } else {
            result = httpPost("127.0.0.1", 8088,
                    restMirrorData.getRequestUri(), restMirrorData.getRequestQueryParams(), stringbody,
                    headers);
        }
        return result;
    }


    public String httpPost(String Host, int port, String path, String query, String jsonString, HashMap<String, String> headers) {
        HttpResponse httpResponse = null;
        try {
            StringEntity entity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
            URI uri = new URI("http", null, Host, port, path, query, null);
            HttpUriRequest request = new HttpPost(uri);

            if (headers != null && !headers.isEmpty()) {

                for (String key : headers.keySet()) {
                    if (!key.equals("content-length")) {
                        request.setHeader(key, headers.get(key));
                    }
                }
            }
            ((HttpPost) request).setEntity(entity);

            httpResponse = httpClient.execute(request);
            String resp = EntityUtils.toString(httpResponse.getEntity(), Charset.forName("UTF-8"));
            EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().debug("http resp:" + resp);
            return resp;
        } catch (IOException e) {
            EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().error("httpPost  IOException  host:{} , port:{} , path:{},  query:{}  ,jsonString:{} , headers:{}  ", Host, port, path, query, jsonString, headers);
        } catch (URISyntaxException e) {
            EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().error("httpPost  URISyntaxException  host:{} , port:{} , path:{},  query:{}  ,jsonString:{} , headers:{} err:{}  ", Host, port, path, query, jsonString, headers, e);
        }
        return null;
    }

    public String httpGet(String Host, int port, String path, String query, HashMap<String, String> headers) {
        HttpResponse httpResponse = null;
        try {
            URI uri = new URI("http", null, Host, port, path, query, null);
            HttpUriRequest request = new HttpGet(uri);

            if (headers != null && !headers.isEmpty()) {

                for (String key : headers.keySet()) {
                    if (!key.equals("content-length")) {
                        request.setHeader(key, headers.get(key));
                    }
                }
            }


            httpResponse = httpClient.execute(request);
            String resp = EntityUtils.toString(httpResponse.getEntity(), Charset.forName("UTF-8"));

            EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().debug("http resp:" + resp);
            return resp;
        } catch (IOException e) {
            EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().error("httpGet  IOException  host:{} , port:{} , path:{},  query:{} , headers:{} err:{}  ", Host, port, path, query, headers, e);
        } catch (URISyntaxException e) {
            EasyByteCoderResourceObjectPool.getEasyByteCoderLogger().error("httpGet  URISyntaxException  host:{} , port:{} , path:{},  query:{} , headers:{} err:{}  ", Host, port, path, query, headers, e);
        } finally {

        }
        return null;
    }

    private static void closeExpiredConnectionsPeriodTask(final int timeUnitBySecond) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        TimeUnit.SECONDS.sleep(timeUnitBySecond);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    cm.closeExpiredConnections();
                }

            }
        }).start();
    }


}
