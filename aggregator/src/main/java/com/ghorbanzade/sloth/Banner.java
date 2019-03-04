//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <pejman@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * This utility class provides a method to print content of a resource file
 * on the console.
 *
 * @author Pejman Ghorbanzade
 */
public final class Banner {

  private static final Logger log = Logger.getLogger(Banner.class);

  /**
   * This method takes the path to a resource file and prints its content
   * on the console. It is called to print startup and shutdown banners of
   * the program.
   *
   * @param resource path to the resource file to printed
   */
  public static void print(String resource) throws FatalException {
    try {
      System.out.print(IOUtils.toString(
          Banner.class.getResourceAsStream(resource), "UTF-8"
      ));
      log.debug("resource file printed on screen");
    } catch (NullPointerException ex) {
      log.error("resource file is missing");
      throw new FatalException(Banner.class);
    } catch (IOException ex) {
      log.error("failed to read resource file");
      throw new FatalException(Banner.class);
    }
  }

  /**
   * Prevent instantiation of this class.
   */
  private Banner() {
  }

}
