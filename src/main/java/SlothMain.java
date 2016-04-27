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
    Config cfg = ConfigManager.get("config/main.properties");
    ResourceManager rm = new ResourceManager();

    SerialQueue sq = new SerialQueue();
    PacketQueue pq = new PacketQueue();
    Posture posture = new Posture();
    ActivityQueue aq = new ActivityQueue();
    FileQueue fq = new FileQueue();

    SerialReader sr = new SerialReader(sq);
    PacketReader pr = new PacketReader(sq, pq, posture);
    PacketProcessor pp = new PacketProcessor(pq, posture);
    ActivityLogger al = new ActivityLogger(aq, fq);
    Uploader uploader = new Uploader(fq);

    rm.add(sr);

    ArrayList<Thread> threads = new ArrayList<Thread>();
    threads.add(new Thread(pr));
    threads.add(new Thread(pp));
    threads.add(new Thread(al));
    threads.add(new Thread(uploader));

    try {
      Runtime.getRuntime().addShutdownHook(new Thread(rm));
      Wsn wsn = WsnManager.getWsn(cfg.getAsString("wsn.config.file"));
      Banner.print(cfg.getAsString("startup.banner"));
      Runtime.getRuntime().addShutdownHook(new Thread(()-> {
        Banner.print(cfg.getAsString("shutdown.banner"));
      }));
      uploader.init();
      for (Thread thread: threads) {
        thread.start();
      }
      sr.open(cfg.getAsString("serial.name"));
      Thread.sleep(10000);
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
