package com.zzang.chongdae.global.config.logging;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class CachedBodyHttpServletResponse extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream cachedBody;
    private final ServletOutputStream outputStream;
    private final PrintWriter writer;

    public CachedBodyHttpServletResponse(HttpServletResponse response) throws IOException {
        super(response);
        cachedBody = new ByteArrayOutputStream();
        outputStream = new CachedBodyServletOutputStream(cachedBody);
        writer = new PrintWriter(cachedBody);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
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

    private class CachedBodyServletOutputStream extends ServletOutputStream {

        private final ByteArrayOutputStream cachedBody;

        public CachedBodyServletOutputStream(ByteArrayOutputStream cachedBody) {
            this.cachedBody = cachedBody;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener listener) {
            // No implementation needed
        }

        @Override
        public void write(int b) throws IOException {
            cachedBody.write(b);
        }
    }
}