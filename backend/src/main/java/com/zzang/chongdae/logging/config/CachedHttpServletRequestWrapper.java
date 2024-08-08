package com.zzang.chongdae.logging.config;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CachedHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] cachedBody;

    public CachedHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        InputStream requestInputStream = request.getInputStream();
        this.cachedBody = inputStreamToByteArray(requestInputStream);
    }

    @Override
    public ServletInputStream getInputStream() {
        return new CachedServletInputStream(this.cachedBody);
    }

    private byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int read = inputStream.read();
        while (read != -1) {
            byteArrayOutputStream.write(read);
            read = inputStream.read();
        }
        return byteArrayOutputStream.toByteArray();
    }

    private static class CachedServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream byteArrayInputStream;

        public CachedServletInputStream(byte[] cachedBody) {
            this.byteArrayInputStream = new ByteArrayInputStream(cachedBody);
        }

        @Override
        public boolean isFinished() {
            return byteArrayInputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
        }

        @Override
        public int read() {
            return byteArrayInputStream.read();
        }
    }
}
