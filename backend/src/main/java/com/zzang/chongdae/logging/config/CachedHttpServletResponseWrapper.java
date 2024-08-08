package com.zzang.chongdae.logging.config;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class CachedHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream cachedBody;
    private final ServletOutputStream outputStream;
    private final PrintWriter writer;

    public CachedHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
        this.cachedBody = new ByteArrayOutputStream();
        this.outputStream = new CachedServletOutputStream(cachedBody);
        this.writer = new PrintWriter(cachedBody);
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (writer != null) {
            writer.flush();
        }
        if (outputStream != null) {
            outputStream.flush();
        }
        super.flushBuffer();
    }

    public byte[] getCachedBody() {
        return cachedBody.toByteArray();
    }

    public void copyBodyToResponse() throws IOException {
        if (cachedBody.size() > 0) {
            HttpServletResponse response = (HttpServletResponse) getResponse();
            ServletOutputStream responseOutputStream = response.getOutputStream();
            responseOutputStream.write(cachedBody.toByteArray());
            responseOutputStream.flush();
        }
    }

    private static class CachedServletOutputStream extends ServletOutputStream {

        private final ByteArrayOutputStream byteArrayOutputStream;

        public CachedServletOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
            this.byteArrayOutputStream = byteArrayOutputStream;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener listener) {
        }

        @Override
        public void write(int buffer) {
            byteArrayOutputStream.write(buffer);
        }
    }
}
