/**
 * The MIT License (MIT)
 * Copyright (c) 2015 Haines
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package io.marmot.laboratory.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 Utils.
 */
public class MD5Utils {
  public static String format(byte[] data) {
    char hexs[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    int l = data.length;
    char[] ret = new char[l * 2];
    int k = 0;
    for (int i = 0; i < l; ++i) {
      byte x = data[i];
      ret[k++] = hexs[x >>> 4 & 0xf];
      ret[k++] = hexs[x & 0xf];
    }
    return new String(ret);
  }

  public static byte[] encryptMD5(byte[] data) {
    try {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(data);
      return md5.digest();
    } catch (NoSuchAlgorithmException ne) {
      ne.printStackTrace();
      return null;
    }
  }
}
