package com.xiesx.fastboot.support.taglib;

import cn.hutool.core.util.StrUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class TagUtils {

  public static String replaceRegClear(String source) {
    return replaceRegText(source, "(.+?)", "");
  }

  public static String replaceRegText(String source, String key, String value) {
    String _regex = "@\\{" + key + "}";
    return source.replaceAll(_regex, value);
  }

  public static String includeJSP(
      HttpServletRequest request,
      HttpServletResponse response,
      String jspFile,
      String charsetEncoding)
      throws ServletException, IOException {
    final OutputStream _output = new ByteArrayOutputStream();
    final PrintWriter _writer =
        new PrintWriter(
            new OutputStreamWriter(
                _output, StrUtil.blankToDefault(charsetEncoding, response.getCharacterEncoding())));
    final ServletOutputStream _servletOutput =
        new ServletOutputStream() {

          @Override
          public boolean isReady() {
            return false;
          }

          @Override
          public void setWriteListener(WriteListener arg0) {}

          @Override
          public void write(int b) throws IOException {
            _output.write(b);
          }

          @Override
          public void write(byte[] b, int off, int len) throws IOException {
            _output.write(b, off, len);
          }
        };
    HttpServletResponse _response =
        new HttpServletResponseWrapper(response) {

          @Override
          public ServletOutputStream getOutputStream() {
            return _servletOutput;
          }

          @Override
          public PrintWriter getWriter() {
            return _writer;
          }
        };
    request.getRequestDispatcher(jspFile).include(request, _response);
    _writer.flush();
    return _output.toString();
  }

  public static Throwable unwrapThrow(Throwable e) {
    if (e == null) {
      return null;
    }
    if (e instanceof InvocationTargetException) {
      InvocationTargetException itE = (InvocationTargetException) e;
      if (itE.getTargetException() != null) {
        return unwrapThrow(itE.getTargetException());
      }
    }
    if (e.getCause() != null) {
      return unwrapThrow(e.getCause());
    }
    return e;
  }
}
