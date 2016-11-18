package com.style.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamUtil {

	public static byte[] read(InputStream in) throws Exception{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = in.read(buffer)) != -1) {
			out.write(buffer, 0, length);
		}
		in.close();
		return out.toByteArray();
	}

}
