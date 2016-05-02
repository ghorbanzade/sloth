//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 *
 *
 *
 * @author Pejman Ghorbanzade
 */
public final class CloudConnector implements Runnable {

  private Config cfg;
  private static final Logger log = Logger.getLogger(PacketProcessor.class);

  /**
   *
   */
  public CloudConnector() {
    this.cfg = ConfigManager.get("config/main.properties");
  }

  /**
   *
   */
  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        Thread.sleep(this.cfg.getAsInt("cloud.connector.sleep.interval"));
        try {
          this.upload();
        } catch (IOException ex) {
          log.error("unable to create directory for classified activities");
        }
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
        log.info("sleep interrupted");
      }
    }
    log.info("cloud connector stopped by the main thread");
  }

  /**
   *
   *
   * @throws IOException
   */
  private void upload() throws IOException {
    File dir = new File(this.cfg.getAsString("dir.classified.activities"));
    FileUtils.forceMkdir(dir);
    String[] exts = {"json"};
    Iterator<File> it = FileUtils.iterateFiles(dir, exts, true);
    Gson gson = new Gson();
    while (it.hasNext()) {
      File file = it.next();
      String content = FileUtils.readFileToString(file, "UTF-8");
      Activity act = gson.fromJson(content, Activity.class);
      System.out.println(act.toString());
      FileUtils.forceDelete(file);
    }
  }

}

