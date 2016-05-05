//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.util.ArrayList;

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
    Config cfg = ConfigManager.get("config/main.properties");
    ResourceManager rm = new ResourceManager();

    SerialQueue sq = new SerialQueue();
    PacketQueue pq = new PacketQueue();
    Posture posture = new Posture();

    SerialReader sr = new SerialReader(sq);
    rm.add(sr);

    ArrayList<Thread> threads = new ArrayList<Thread>();
    threads.add(new Thread(new PacketReader(sq, pq)));
    threads.add(new Thread(new PacketProcessor(pq, posture)));
    threads.add(new Thread(new CloudConnector()));
    threads.add(new Thread(new Learner(posture, "walking")));

    try {
      Runtime.getRuntime().addShutdownHook(new Thread(rm));
      Banner.print(cfg.getAsString("startup.banner"));
      Runtime.getRuntime().addShutdownHook(new Thread(()-> {
        Banner.print(cfg.getAsString("shutdown.banner"));
      }));
      for (Thread thread: threads) {
        thread.start();
      }
      sr.open(cfg.getAsString("serial.name"));
      Thread.sleep(cfg.getAsInt("serial.listening.time"));
    } catch (InterruptedException ex) {
    } catch (FatalException ex) {
      log.fatal("aborting program. check log file for details.");
    } finally {
      for (Thread thread: threads) {
        thread.interrupt();
      }
    }
  }

}
