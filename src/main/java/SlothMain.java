//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.io.IOException;
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
    ResourceManager rm = new ResourceManager();
    ConfigManager cm = new ConfigManager("/config.properties");

    SerialQueue sq = new SerialQueue();
    PacketQueue pq = new PacketQueue();
    Posture posture = new Posture(cm);
    FileQueue fq = new FileQueue(cm);

    Uploader uploader = new Uploader(cm, fq);
    SerialReader sr = new SerialReader(cm, sq);
    PacketReader pr = new PacketReader(cm, sq, pq, posture);

    rm.add(sr);

    ArrayList<Thread> threads = new ArrayList<Thread>();
    threads.add(new Thread(pr));

    try {
      Runtime.getRuntime().addShutdownHook(new Thread(rm));
      cm.init();
      Banner.print(cm.getAsString("startup.banner"));
      Runtime.getRuntime().addShutdownHook(new Thread(()-> {
          Banner.print(cm.getAsString("shutdown.banner"));
      }));
      uploader.init();
      for (Thread thread: threads) {
        thread.start();
      }
      sr.open(cm.getAsString("serial.name"));
      Thread.sleep(10000);
      for (Thread thread: threads) {
        thread.interrupt();
      }
    } catch (InterruptedException ex) {
    } catch (FatalException ex) {
      log.fatal("aborting program. check log file for details.");
    }
  }

}
