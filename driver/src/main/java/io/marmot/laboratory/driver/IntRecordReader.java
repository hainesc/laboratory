/**
 * The MIT License (MIT)
 * Copyright (c) 2015 Haines Chan
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

package io.marmot.laboratory.driver;

import io.marmot.laboratory.utils.RecordReader;

import java.io.*;

public class IntRecordReader implements RecordReader<IntRecord> {
  private BufferedReader br;
  public IntRecordReader(String from) {
    try {
      this.br = new BufferedReader(
          new InputStreamReader(
              new FileInputStream(from)));
    } catch (FileNotFoundException fne) {
      System.err.print("FIle " + from + " not found");
      System.exit(1);
    }
  }
  public IntRecord next() {
    String tmp;
    try {
      tmp = br.readLine();
    } catch (IOException ioe) {
      throw new RuntimeException("IOException while read next value.");
    }
    if ((tmp == null) || "".equals(tmp)) {
      return null;
    }
    return new IntRecord(Integer.parseInt(tmp));
  }
  @Override
  public void close() throws IOException {
    br.close();
  }
}
