/**
 * The MIT License (MIT)
 * Copyright (c) 2016 Haines Chan
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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Test for RecordReader and RecordWriter.
 */
public class RecordRWTest {
  private final String test = "/tmp/test";
  private final int count = 8096;
  RecordWriter<IntRecord> writer;
  RecordReader<IntRecord> reader;
  private final List<IntRecord> original = new ArrayList<>(count);
  private final Random r = new Random(0);

  @Before
  public void setUp() {

    writer = new IntRecordWriter(test);
    reader = new IntRecordReader(test);
    for (int i = 0; i < count; ++i) {
      original.add(i, new IntRecord(r.nextInt()));
    }
  }

  @Test
  public void testWriter() throws Exception {
    for (int i = 0; i < count; ++i) {
      writer.write(original.get(i));
    }
    writer.flush();

    for (int i = 0; i < count; ++i) {
      Assert.assertEquals(original.get(i), reader.next());
    }
    // Reader has read all of the record.
    Assert.assertNull(reader.next());
  }

  @After
  public void tearDown() {
    try {
      writer.close();
      reader.close();
      new File(test).deleteOnExit();
    } catch (Exception e) {
      // Do nothing.
    }
  }
}
