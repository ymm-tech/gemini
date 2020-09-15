package io.manbang.easybytecoder.plugin.restrecord.runtime;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * @author GaoYang 2018/12/23
 */

public class ResponseInterceptorWrapper extends HttpServletResponseWrapper {

    private ServletOutputStream contentStream;
    private PrintWriter writer;
    private OutputStreamCloner clonerStream;

    private HttpServletResponse originalResponse = null;


    public ResponseInterceptorWrapper(HttpServletResponse response) {
        super(response);
        originalResponse = response;
    }

    public void finishResponse() {
        try {
            if (writer != null) {
                writer.close();
            } else {
                if (clonerStream != null) {
                    clonerStream.close();
                }
            }
        } catch (IOException e) {
        }
    }

    @Override
    public void flushBuffer() throws IOException {
        if (writer != null) {
            writer.flush();
        } else if (contentStream != null) {
            clonerStream.flush();
        }
    }

    public byte[] getClone() {
        if (clonerStream != null) {
            return clonerStream.getClone();
        } else {
            return new byte[0];
        }
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getOutputStream called after getWriter");
        }

        if (contentStream == null) {
            contentStream = originalResponse.getOutputStream();
            clonerStream = new OutputStreamCloner(contentStream);
        }
        return clonerStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer != null) {
            return (writer);
        }

        if (contentStream != null) {
            throw new IllegalStateException("getWriter called after getOutputStream ");
        }

        clonerStream = new OutputStreamCloner(originalResponse.getOutputStream());
        writer =
                new PrintWriter(
                        new OutputStreamWriter(clonerStream, originalResponse.getCharacterEncoding()), true);
        return writer;
    }

    public int getSize() {
        return clonerStream.getClone().length;
  }

    public ServletOutputStream getContentStream() {
        return contentStream;
    }

    public void setContentStream(ServletOutputStream contentStream) {
        this.contentStream = contentStream;
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public OutputStreamCloner getClonerStream() {
        return clonerStream;
    }

    public void setClonerStream(OutputStreamCloner clonerStream) {
        this.clonerStream = clonerStream;
    }

    public HttpServletResponse getOriginalResponse() {
        return originalResponse;
    }

    public void setOriginalResponse(HttpServletResponse originalResponse) {
        this.originalResponse = originalResponse;
    }
}
