package com.cdjdgm.dip.util;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * 
 * Some simple file IO primitives reimplemented in Java.
 * 
 * All methods are static since there is no state.
 */

public class FileUtil {
	// 参考org.aspectj.util.FileUtil

	/** Copy a file from one filename to another */
	public static void copyFile(String inName, String outName) throws FileNotFoundException, IOException {

		BufferedInputStream is = new BufferedInputStream(new FileInputStream(inName));

		BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(outName));

		copyFile(is, os, true);
	}

	/** Copy a file from an opened InputStream to an opened OutputStream */
	public static void copyFile(InputStream is, OutputStream os, boolean close) throws IOException {

		int b; // the byte read from the file

		while ((b = is.read()) != -1) {
			os.write(b);
		}

		is.close();
		if (close)
			os.close();

	}

	/** Copy a file from an opened Reader to an opened Writer */
	public static void copyFile(Reader is, Writer os, boolean close) throws IOException {

		int b; // the byte read from the file

		while ((b = is.read()) != -1) {
			os.write(b);
		}

		is.close();
		if (close)
			os.close();

	}

	/** Copy a file from a filename to a PrintWriter. */
	public static void copyFile(String inName, PrintWriter pw, boolean close) throws FileNotFoundException, IOException {
		BufferedReader ir = new BufferedReader(new FileReader(inName));
		copyFile(ir, pw, close);
	}

	/** Open a file and read the first line from it. */

	public static String readLine(String inName) throws FileNotFoundException, IOException {
		BufferedReader is = new BufferedReader(new FileReader(inName));
		String line = null;
		line = is.readLine();
		is.close();
		return line;
	}

	/** The size of blocking to use */
	protected static final int BLKSIZ = 8192;

	/**
	 * Copy a data file from one filename to another, alternate method.
	 * 
	 * As the name suggests, use my own buffer instead of letting
	 * 
	 * the BufferedReader allocate and use the buffer.
	 */

	public void copyFileBuffered(String inName, String outName) throws FileNotFoundException, IOException {

		InputStream is = new FileInputStream(inName);
		OutputStream os = new FileOutputStream(outName);

		int count = 0; // the byte count
		byte[] b = new byte[BLKSIZ]; // the bytes read from the file

		while ((count = is.read(b)) != -1) {
			os.write(b, 0, count);
		}

		is.close();
		os.close();
	}

	/**
	 * Returns the contents of this file as a String
	 */
	public static String readAsString(String filePath) {
		File file = new File(filePath);
		return readAsString(file);
	}
	
	/**
	 * Returns the contents of this file as a String
	 */
	public static String readAsString(File file) {
		try(BufferedReader r = new BufferedReader(new FileReader(file));){
			StringBuffer b = new StringBuffer();
			while (true) {
				int ch = r.read();
				if (ch == -1) {
					break;
				}
				b.append((char) ch);
			}
			r.close();
			return b.toString();
		} catch (IOException e){
			throw new RuntimeException(e);
		}
	}
	
	/** Read the entire content of a Reader into a String */
	protected static String readerToString(Reader is) throws IOException {
		StringBuffer sb = new StringBuffer();
		char[] b = new char[BLKSIZ];
		int n;

		// Read a block. If it gets any chars, append them.
		while ((n = is.read(b)) > 0) {
			sb.append(b, 0, n);
		}

		// Only construct the String object once, here.
		return sb.toString();
	}

	/** Read the content of a Stream into a String */
	public static String inputStreamToString(InputStream is) throws IOException {
		return readerToString(new InputStreamReader(is));
	}
}