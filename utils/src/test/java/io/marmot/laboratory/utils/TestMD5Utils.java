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

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * MD5Utils Unit test.
 */
public class TestMD5Utils {

  private byte[] b;
  @Before
  public void setUp() {
    b = new byte[10];
    for (int i = 0; i < 10; ++i) {
      b[i] = (byte) i;
    }
  }
  @Test
  public void test() {
    Assert.assertEquals(MD5Utils.format(MD5Utils.encryptMD5(b)),
        "C56BD5480F6E5413CB62A0AD9666613A");
  }
  @After
  public void tearDown() {
    // Do nothing. just a hook show how to use After tag.
  }
}


