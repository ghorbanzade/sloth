//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.io.IOException;

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
    ConfigManager cm = new ConfigManager("/config.properties");
    FileQueue fq = new FileQueue(cm);
    Uploader uploader = new Uploader(cm, fq);
    try {
      cm.init();
      uploader.init();
      //Thread thread = new Thread(uploader);
      //thread.start();
    } catch (RuntimeException ex) {
      log.error("program aborted immaturely");
    }
  }

}
