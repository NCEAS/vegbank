package org.vegbank.filter;
/*
 * '$RCSfile: GZIPResponseStream.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2004-03-07 17:55:28 $'
 *	'$Revision: 1.1 $'
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA 
 */
 
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Gabriel
 *
 * This gzips up content before sending it to the browser if the browser 
 * supports this.
 * 
 * This code is taken from an article I found at
 * @link http://www.onjava.com/pub/a/onjava/2003/11/19/filters.html
 *
 */
public class GZIPResponseStream extends ServletOutputStream {
	
	/**
	 * Commons Logging instance.
	 */
	protected static Log log = LogFactory.getLog(GZIPResponseStream.class);

  // abstraction of the output stream used for compression
  protected OutputStream bufferedOutput = null;
  // state keeping variable for if close() has been called
  protected boolean closed = false;
  // reference to original response
  protected HttpServletResponse response = null;
  // reference to the output stream to the client's browser
  protected ServletOutputStream output = null;
  // default size of the in-memory buffer
  private int bufferSize = 50000;

  public GZIPResponseStream(HttpServletResponse response) throws IOException {
    super();
    closed = false;
    this.response = response;
    this.output = response.getOutputStream();
    bufferedOutput = new ByteArrayOutputStream();
  }

  public void close() throws IOException {
    // This hack shouldn't be needed, but it seems to make JBoss and Struts
    // like the code more without hurting anything.
    /*
    // verify the stream is yet to be closed
    if (closed) {
      throw new IOException("This output stream has already been closed");
    }
    */
    // if we buffered everything in memory, gzip it
    if (bufferedOutput instanceof ByteArrayOutputStream) {
      // get the content
      ByteArrayOutputStream baos = (ByteArrayOutputStream)bufferedOutput;
      // prepare a gzip stream
      ByteArrayOutputStream compressedContent = new ByteArrayOutputStream();
      GZIPOutputStream gzipstream = new GZIPOutputStream(compressedContent);
      byte[] bytes = baos.toByteArray();
      gzipstream.write(bytes);
      gzipstream.finish();
      // get the compressed content
      byte[] compressedBytes = compressedContent.toByteArray();
      // set appropriate HTTP headers
      response.setContentLength(compressedBytes.length); 
      response.addHeader("Content-Encoding", "gzip");
      output.write(compressedBytes);
      output.flush();
      output.close();
      closed = true;
    }
    // if things were not buffered in memory, finish the GZIP stream and response
    else if (bufferedOutput instanceof GZIPOutputStream) {
      // cast to appropriate type
      GZIPOutputStream gzipstream = (GZIPOutputStream)bufferedOutput;
      // finish the compression
      gzipstream.finish();
      // finish the response
      output.flush();
      output.close();
      closed = true;
    }
  }

  public void flush() throws IOException {
    if (closed) {
      throw new IOException("Cannot flush a closed output stream");
    }
    bufferedOutput.flush();
  }

  public void write(int b) throws IOException {
    if (closed) {
      throw new IOException("Cannot write to a closed output stream");
    }
    // make sure we aren't over the buffer's limit
    checkBufferSize(1);
    // write the byte to the temporary output
    bufferedOutput.write((byte)b);
  }

  private void checkBufferSize(int length) throws IOException {
    // check if we are buffering too large of a file
    if (bufferedOutput instanceof ByteArrayOutputStream) {
      ByteArrayOutputStream baos = (ByteArrayOutputStream)bufferedOutput;
      if (baos.size()+length > bufferSize) {
        // files too large to keep in memory are sent to the client without Content-Length specified
        response.addHeader("Content-Encoding", "gzip");
        // get existing bytes
        byte[] bytes = baos.toByteArray();
        // make new gzip stream using the response output stream
        GZIPOutputStream gzipstream = new GZIPOutputStream(output);
        gzipstream.write(bytes);
        // we are no longer buffering, send content via gzipstream
        bufferedOutput = gzipstream;
      }
    }
  }
  
  public void write(byte b[]) throws IOException {
    write(b, 0, b.length);
  }

  public void write(byte b[], int off, int len) throws IOException {
		log.debug("writing...");
    if (closed) {
      throw new IOException("Cannot write to a closed output stream");
    }
    // make sure we aren't over the buffer's limit
    checkBufferSize(len);
    // write the content to the buffer
    bufferedOutput.write(b, off, len);
  }

  public boolean closed() {
    return (this.closed);
  }
  
  public void reset() {
    //noop
  }
}
