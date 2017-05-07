//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth.parser;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 *
 * @author Pejman Ghorbanzade
 */
public final class CommandFileParser {

  private static final Logger log =
      Logger.getLogger(CommandFileParser.class);
  private final File file;

  /**
   *
   */
  public CommandFileParser(String path) {
    this.file = new File(path);
  }

  /**
   *
   */
  public void parse() {
    try {
      Scanner sc = new Scanner(this.file, "UTF-8");
      while (sc.hasNextLine()) {
        System.out.println(sc.nextLine());
      }
    } catch (IOException ex) {
    }
  }

}
