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

import org.apache.commons.cli.*;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Laboratory.
 */
public class Driver {
  private static void printHelp(PrintStream out, Options options) {
    // String header = "Spider usage details: ";
    String header = null;
    String footer = "Any problem, please contact with Haines Chan.";
    PrintWriter writer = new PrintWriter(out);
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp(writer, 80, "Laboratory", header, options,
        1, 3, footer, true);
    writer.close();
  }

  private static void printUsage(final String appName,
                                 Options options,
                                 PrintStream out) {
    final PrintWriter writer = new PrintWriter(out);
    final HelpFormatter formatter = new HelpFormatter();
    formatter.printUsage(writer, 80, appName, options);
    writer.close();
  }

  private static void printVersion(PrintStream out) {
    out.println("Laboratory version 0.1");
  }

  public static void main(String[] args) {

    Options options = new Options();
    options.addOption(Option.builder("a")
        .argName("algorithm")
        .required()
        .hasArg()
        .longOpt("algorithm")
        .desc("The algorithm you choose")
        .build());

    options.addOption(Option.builder("f")
        .argName("file")
        .required()
        .desc("Input file")
        .longOpt("file")
        .hasArg()
        .build());

    options.addOption(Option.builder("v")
        .desc("Show version string")
        .longOpt("version")
        .build());

    options.addOption(Option.builder("h")
        .desc("Show help message")
        .longOpt("help")
        .build());

    CommandLineParser parser = new DefaultParser();
    try {
      CommandLine cmd = parser.parse(options, args);

      if (cmd.hasOption("h")) {
        printHelp(System.err, options);
      }

      if (cmd.hasOption("v")) {
        printVersion(System.out);
      }
      String file = cmd.getOptionValue("f");
      String algorithm = cmd.getOptionValue("f");

      System.out.println("I will do something on file " +
           file + " by " +
           algorithm + " later.");

    } catch (ParseException pe) {
      System.err.println("Parsing failed, " + pe.getMessage());
    }
  }
}

