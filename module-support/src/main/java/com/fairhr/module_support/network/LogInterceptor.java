package com.fairhr.module_support.network;


import com.fairhr.module_support.utils.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

public class LogInterceptor implements Interceptor {
    private static final String TAG = "_http";

    private static final Charset UTF8 = StandardCharsets.UTF_8;

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        return logResponse(chain.request(), chain);
    }

    private Response logResponse(Request request, Chain chain) throws IOException {
        boolean logBody = true;
        boolean logHeaders = true;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        String requestStartMessage = "------------------------> START " + protocol + ' ' + request.method() + ' ' + request.url();
        String requestPath = request.url().url().getPath();
        if (hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        LogUtil.w(TAG, requestStartMessage);

        // log params
        if ("POST".equals(request.method())) {
            if (requestBody instanceof FormBody) {
                FormBody formBody = (FormBody) requestBody;
                String url = request.url().url().toString();
                for (int i = 0; i < formBody.size(); i++) {
                    LogUtil.w(TAG, "FormBody params: " + formBody.name(i) + " = " + formBody.value(i));
                    if (i == 0) {
                        url += "?" + formBody.name(i) + "=" + formBody.value(i);
                    } else {
                        url += "&" + formBody.name(i) + "=" + formBody.value(i);
                    }
                }
                LogUtil.w(TAG, url);
            } else if (requestBody instanceof MultipartBody) {
                MultipartBody multipartBody = (MultipartBody) requestBody;
                Buffer buffer = new Buffer();
                multipartBody.writeTo(buffer);
                Charset charset = StandardCharsets.UTF_8;
                MediaType contentType = multipartBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }
                String paramsStr = buffer.readString(charset);
                LogUtil.w(TAG, "-------- MultipartBody (" + requestPath + ") :\n" + paramsStr);
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                Charset charset = StandardCharsets.UTF_8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }
                String paramsStr = buffer.readString(charset);
                LogUtil.w(TAG, "-------- RequestBody (" + requestPath + ") :\n" + paramsStr);
            }
        } else if ("GET".equals(request.method())) {
            HttpUrl httpUrl = request.url();
            Set<String> keys = httpUrl.queryParameterNames();
            for (String key : keys) {
                LogUtil.w(TAG, "params: " + key + " = " + httpUrl.queryParameter(key));
            }
        }

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    LogUtil.w(TAG, "Content-Type: " + requestBody.contentType());
                }
                if (requestBody.contentLength() != -1) {
                    LogUtil.w(TAG, "Content-Length: " + requestBody.contentLength() + "byte");
                }
            }

            Headers headers = request.headers();
            LogUtil.w(TAG, "-------- Request Header: " + headers.size());
//            for (int i = 0, count = headers.size(); i < count; i++) {
//                String name = headers.name(i);
//                // Skip headers from the request body as they are explicitly logged above.
//                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
//                    //id token
//                    if (AppUtil.INSTANCE.isDebug() && "x-id-token".equals(name)) {
//                        if (NetConfig.INSTANCE.getEscapeToken().equals(headers.value(i))) {
//                            LogUtil.e(TAG, "匿名token");
//                        } else {
//                            LogUtil.e(TAG, "id token");
//                        }
//                    }
//                    LogUtil.w(TAG, name + ": " + headers.value(i));
//                }
//            }

            if (!logBody || !hasRequestBody) {
                LogUtil.w(TAG, "--------> END " + request.method());
            } else if (bodyEncoded(request.headers())) {
                LogUtil.w(TAG, "--------> END " + request.method() + " (encoded body omitted)");
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

//                if (isPlaintext(buffer)) {
//                    //LogUtil.w(TAG, buffer.readString(charset));
//                    LogUtil.w(TAG, "--------> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)");
//                } else {
//                    LogUtil.w(TAG, "--------> END " + request.method() + " (binary " + requestBody.contentLength() + "-byte body omitted)");
//                }
            }
        }

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            LogUtil.e(TAG, "<------------------------ HTTP FAILED: " + e);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        LogUtil.w(TAG, response.code() + " " + response.message() + " (" + tookMs + "ms" + (!logHeaders ? ", "
                + bodySize + " body" : "") + ')');

        if (logHeaders) {
            Headers headers = response.headers();
            LogUtil.w(TAG, "-------- Response Header (" + requestPath + ") :");
            for (int i = 0, count = headers.size(); i < count; i++) {
                LogUtil.w(TAG, headers.name(i) + ": " + headers.value(i));
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                LogUtil.w(TAG, "<------------------------ END HTTP");
            } else if (bodyEncoded(response.headers())) {
                LogUtil.w(TAG, "<------------------------ END HTTP (encoded body omitted)");
            }
        }

        if (logBody) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    LogUtil.w(TAG, "Couldn't decode the response body; charset is likely malformed.");
                    LogUtil.w(TAG, "<------------------------ END HTTP");
                    LogUtil.w(TAG, "");
                    return response;
                }
            }

            if (!isPlaintext(buffer)) {
                LogUtil.w(TAG, "<------------------------ END HTTP (binary " + buffer.size() + "-byte body omitted)");
                LogUtil.w(TAG, "");
                return response;
            }

            if (contentLength != 0) {
                // result
                LogUtil.w(TAG, "-------- Response Body (" + requestPath + ") :");
                LogUtil.w(TAG, buffer.clone().readString(charset));
            }

            LogUtil.w(TAG, "<------------------------ END HTTP (" + buffer.size() + "-byte body)");
        }

        LogUtil.w(TAG, "");
        return response;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

}
