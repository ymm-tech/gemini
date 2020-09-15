package io.manbang.easybytecoder.plugin.restrecord.runtime;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author GaoYang 2018/12/23
 */
public class OutputStreamCloner extends ServletOutputStream {
    private ServletOutputStream outputStream;
    private ByteArrayOutputStream clonedStream = null;
    protected boolean closed = false;

    public OutputStreamCloner(ServletOutputStream outputStream) {
        super();
        closed = false;
        this.outputStream = outputStream;
        clonedStream = new ByteArrayOutputStream();
    }

    @Override
    public void close() throws IOException {
        if (closed) {
            throw new IOException("output stream has already been closed");
        }
        flush();
        outputStream.close();
        clonedStream.close();
        closed = true;
    }

    @Override
    public void flush() throws IOException {
        if (closed) {
            throw new IOException("cannot flush a closed output stream");
        }
        outputStream.flush();
        clonedStream.flush();
    }

    public byte[] getClone() {
        return clonedStream.toByteArray();
    }

    @Override
    public void write(int data) throws IOException {
        if (closed) {
            throw new IOException("cannot write to a closed output stream");
        }
        this.outputStream.write((byte) data);
        this.clonedStream.write((byte) data);
    }

    @Override
    public void write(byte data[]) throws IOException {
        write(data, 0, data.length);
    }

    @Override
    public void write(byte data[], int off, int len) throws IOException {
        if (closed) {
            throw new IOException("cannot write to a closed output stream");
        }
        this.outputStream.write(data, off, len);
        this.clonedStream.write(data, off, len);
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
    }

    public boolean closed() {
        return (this.closed);
    }

    public void reset() {
        // noop
  }
}
