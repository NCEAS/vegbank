/*
 *	'$RCSfile: Base64.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2004-08-04 20:58:50 $'
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

package org.vegbank.common.utility;

/**
 *  Base64 encoder/decoder.  Does not stream, so be careful with
 *  using large amounts of data.
 *  Base64 encode only in the ASCII character set [A-Za-z0-9+=/]
 */


import java.io.*;


public final class Base64 {

	/**
	*  Encode some data and return a String.
	*/
	public final static String encode(byte[] d) {
		if (d == null) return null;
		byte data[] = new byte[d.length+2];
		System.arraycopy(d, 0, data, 0, d.length);
		byte dest[] = new byte[(data.length/3)*4];

		// 3-byte to 4-byte conversion
		for (int sidx = 0, didx=0; sidx < d.length; sidx += 3, didx += 4) {
			dest[didx]   = (byte) ((data[sidx] >>> 2) & 077);
			dest[didx+1] = (byte) ((data[sidx+1] >>> 4) & 017 |
				(data[sidx] << 4) & 077);
			dest[didx+2] = (byte) ((data[sidx+2] >>> 6) & 003 |
				(data[sidx+1] << 2) & 077);
			dest[didx+3] = (byte) (data[sidx+2] & 077);
		}

		// 0-63 to ascii printable conversion
		for (int idx = 0; idx <dest.length; idx++) {
			if (dest[idx] < 26)       dest[idx] = (byte)(dest[idx] + 'A');
			else if (dest[idx] < 52)  dest[idx] = (byte)(dest[idx] + 'a' - 26);
			else if (dest[idx] < 62)  dest[idx] = (byte)(dest[idx] + '0' - 52);
			else if (dest[idx] < 63)  dest[idx] = (byte)'+';
			else                      dest[idx] = (byte)'/';
		}

		// add padding
		for (int idx = dest.length-1; idx > (d.length*4)/3; idx--) {
			dest[idx] = (byte)'=';
		}
		return new String(dest);
	}


	/**
	*  Encode data and return a String.
	*/
	public final static String encode(String str) {
		if (str == null)  return  null;
		return encode(str.getBytes());
	}


	/**
	*  Decode data and return bytes.
	*/
	public final static byte[] decode(String str) {
		if (str == null)  return  null;
		byte data[] = new byte[str.length()];
		data = str.getBytes();
		// str.getBytes(0, str.length(), data, 0); // faster but deprecated
		return decode(data);
	}


	/**
	*  Decode data and return bytes.  Assumes that the data passed
	*  in is ASCII text.
	*/
	public final static byte[] decode(byte[] data) {

		int tail = data.length;
		while (data[tail-1] == '=')  tail--;
		byte dest[] = new byte[tail - data.length/4];

		// ascii printable to 0-63 conversion
		for (int idx = 0; idx <data.length; idx++) {
			if (data[idx] == '=')    data[idx] = 0;
			else if (data[idx] == '/') data[idx] = 63;
			else if (data[idx] == '+') data[idx] = 62;
			else if (data[idx] >= '0'  &&  data[idx] <= '9')
				data[idx] = (byte)(data[idx] - ('0' - 52));
			else if (data[idx] >= 'a'  &&  data[idx] <= 'z')
				data[idx] = (byte)(data[idx] - ('a' - 26));
			else if (data[idx] >= 'A'  &&  data[idx] <= 'Z')
				data[idx] = (byte)(data[idx] - 'A');
		}

		// 4-byte to 3-byte conversion
		int sidx, didx;
		for (sidx = 0, didx=0; didx < dest.length-2; sidx += 4, didx += 3) {
			dest[didx]   = (byte) ( ((data[sidx] << 2) & 255) |
				((data[sidx+1] >>> 4) & 3) );
			dest[didx+1] = (byte) ( ((data[sidx+1] << 4) & 255) |
				((data[sidx+2] >>> 2) & 017) );
			dest[didx+2] = (byte) ( ((data[sidx+2] << 6) & 255) |
				(data[sidx+3] & 077) );
		}
		if (didx < dest.length) {
			dest[didx]   = (byte) ( ((data[sidx] << 2) & 255) |
				((data[sidx+1] >>> 4) & 3) );
		}
		if (++didx < dest.length) {
			dest[didx]   = (byte) ( ((data[sidx+1] << 4) & 255) |
				((data[sidx+2] >>> 2) & 017) );
		}
		return dest;
	}





	/**
	*  A simple test that encodes and decodes a file specified as 
	*  first commandline argument.
	*/
	public static final void main(String args[]) {
		if (args.length != 1) {
			System.out.println("Usage: Base64 filename");
			System.exit(0);
		}

		try {
			FileInputStream in = new FileInputStream( args[0] );
			ByteArrayOutputStream bin = new ByteArrayOutputStream();
			int b;
			while( (b=in.read()) != -1) {
				bin.write(b);
			}
			in.close();
			System.out.println("input file: " + args[0] + " read...");

			String encoded = Base64.encode( bin.toByteArray() );
			System.out.println("content BASE64 encoded...");

			PrintWriter writer = 
				new PrintWriter( new FileOutputStream( args[0] + ".base64" ));
			writer.println( encoded );
			writer.close();
			System.out.println("content BASE64 stored in file: " + 
					args[0] + ".base64");


			byte[] decoded = Base64.decode( encoded );
			System.out.println("content BASE64 decoded...");

			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			bout.write( decoded, 0, decoded.length );

			FileOutputStream out = new FileOutputStream( "new." + args[0] );
			bout.writeTo( out );
			out.close();
			System.out.println("output file: new." + args[0] + " writen...");

		} catch (Exception x) {
			x.printStackTrace();
		}
	}
}

