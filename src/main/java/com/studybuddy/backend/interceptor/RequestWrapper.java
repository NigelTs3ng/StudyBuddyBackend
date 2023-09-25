package com.studybuddy.backend.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.studybuddy.backend.config.ShareConfig;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Arrays;
import java.util.List;

@Getter
@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper {
    private String body;

    public RequestWrapper(HttpServletRequest request, ObjectMapper objectMapper, ShareConfig shareConfig) throws Exception {
        super(request);
        try {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = null;
            InputStream inputStream = null;
            try {
                inputStream = request.getInputStream();
                if (inputStream != null) {
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    char[] charBuffer = new char[128];
                    int bytesRead = -1;
                    while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                        stringBuilder.append(charBuffer, 0, bytesRead);
                    }

                } else {
                    stringBuilder.append("");
                }
            } catch (IOException ex) {
                throw ex;
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException ex) {
                        throw ex;
                    }
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            this.body = stringBuilder.toString();
            String requestBody = maskRequest(this.body, objectMapper, shareConfig);
            log.info("[REQUEST BODY]: {}", requestBody);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    private String maskRequest(String request, ObjectMapper objectMapper, ShareConfig shareConfig) {
        String newRequest = request;
        String mask = "******";
        try {
            String maskSensitiveData = shareConfig.getFieldSensitiveList();
            ObjectNode requestObjectNode = objectMapper.readTree(request).deepCopy();
            List<String> a = Arrays.asList(maskSensitiveData.split(","));
            a.forEach(f -> {
                if (requestObjectNode.get(f) != null) {
                    requestObjectNode.put(f, mask);
                }
            });
            newRequest = objectMapper.writeValueAsString(requestObjectNode);
        } catch (Exception e) {
        }

        return newRequest;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        ServletInputStream servletInputStream = new ServletInputStream() {
            public int read() throws IOException {
                return byteArrayInputStream.read();
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
            public void setReadListener(ReadListener listener) {

            }
        };
        return servletInputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

}
