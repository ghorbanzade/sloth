//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import com.ghorbanzade.sloth.cli.Cli;

import org.apache.log4j.Logger;

/**
 * Main class of sloth.
 *
 * @author Pejman Ghorbanzade
 */
public class SlothMain {

  private static final Logger log = Logger.getLogger(SlothMain.class);

  /**
   * Starts a command line interface that interacts with user requests one
   * at a time.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    Config cfg = ConfigManager.get("config/main.properties");
    try {
      Banner.print(cfg.getAsString("startup.banner"));
      Runtime.getRuntime().addShutdownHook(new Thread(()-> {
        Banner.print(cfg.getAsString("shutdown.banner"));
      }));
      Cli cli = new Cli();
      while (true) {
        cli.execute(cli.getInstruction());
      }
    } catch (Cli.GracefulExitException ex) {
      log.debug("program terminated by user");
    } catch (FatalException ex) {
      log.fatal("aborting program. check log file for details.");
    }
  }

  /**
   * Prevents instantiation from this class.
   */
  private SlothMain() {
  }

}
