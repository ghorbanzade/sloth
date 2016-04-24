//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

/**
 *
 *
 * @author Pejman Ghorbanzade
 */
public class SlothMain {

  private static final Logger log = Logger.getLogger(SlothMain.class);

  /**
   *
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    try {
      ConfigReader cr = new ConfigReader("/config.properties");
      System.out.println(cr.getAsInt("serial.baudrate"));
      System.out.println(cr.getAsString("serial.name"));
    } catch (RuntimeException ex) {
      log.error("program aborted immaturely");
    }
  }

}
