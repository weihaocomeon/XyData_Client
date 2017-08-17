package com.ztgeo.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.log4j.Logger;



public class SystemPrintln {
	public  Logger log = Logger.getLogger(SystemPrintln.class);
	public  SystemPrintln() {
		
		OutputStream textAreaStream = new OutputStream() {
			public void write(int b) throws IOException {
				log.info(String.valueOf((char) b));
			}
			public void write(byte b[]) throws IOException {
				log.info(new String(b));
			}
			public void write(byte b[], int off, int len) throws IOException {
				log.info(new String(b, off, len));
			}
		};

		PrintStream myOut = new PrintStream(textAreaStream);
		System.setOut(myOut);
		System.setErr(myOut);
	}
	
	
	
}
