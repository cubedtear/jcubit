package io.github.aritzhack.aritzh.logging;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @author Aritz Lopez
 */
public class OSLogger extends ALogger {

    final PrintWriter pw;

    public OSLogger(OutputStream os) {
        this.pw = new PrintWriter(os, true);
    }
    @Override
    public void t(String msg) {

    }

    @Override
    public void t(String msg, Throwable t) {

    }

    @Override
    public void d(String msg) {

    }

    @Override
    public void d(String msg, Throwable t) {

    }

    @Override
    public void i(String msg) {

    }

    @Override
    public void i(String msg, Throwable t) {

    }

    @Override
    public void w(String msg) {

    }

    @Override
    public void w(String msg, Throwable t) {

    }

    @Override
    public void e(String msg) {

    }

    @Override
    public void e(String msg, Throwable t) {

    }
}
