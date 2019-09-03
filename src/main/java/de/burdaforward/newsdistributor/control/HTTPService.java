package de.burdaforward.newsdistributor.control;

import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HTTPService {
    /* Global timeout variables */
    private static final Integer CONNECTION_TIMEOUT = 5 * 1000;
    private static final Integer CONNECTION_REQUEST_TIMEOUT = 5 * 1000;
    private static final Integer SOCKET_TIMEOUT = 5 * 1000;

    /**
     * Execute an HTTP GET request against the passed URL.
     *
     * @param url URL which should be used for the HTTP GET request.
     * @return Body of the HTTP GET response (as a String).
     * @throws IOException Will be thrown if the HTTP GET request cant be completed successfully.
     */
    public static String executeGetRequest(String url) throws IOException, IllegalStateException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);

            // Configure timeouts
            RequestConfig httpRequestConfig = RequestConfig.custom()
                    .setConnectTimeout(CONNECTION_TIMEOUT)
                    .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    .build();

            httpGet.setConfig(httpRequestConfig);

            // Execute the request and process the response
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                int responseStatusCode = response.getStatusLine().getStatusCode();

                if (responseStatusCode == HttpStatus.SC_OK) {
                    return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                } else {
                    throw new IllegalStateException("Server of " + url + " returned status-code " + responseStatusCode);
                }
            }
        }
    }
}
