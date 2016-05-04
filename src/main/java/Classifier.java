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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 *
 *
 * @author Pejman Ghorbanzade
 * @see Activity
 * @see Learner
 * @see Posture
 */
public final class Classifier implements Runnable {

  private final Config cfg;
  private final Posture posture;
  private final ArrayList<Activity> acts;
  private static final Logger log = Logger.getLogger(Classifier.class);

  /**
   *
   *
   * @param posture the posture based on which classification should be made
   * @throws FatalException if learned activity samples cannot be loaded
   */
  public Classifier(Posture posture) throws FatalException {
    this.posture = posture;
    this.cfg = ConfigManager.get("config/main.properties");
    try {
      this.acts = load();
    } catch (IOException ex) {
      throw new FatalException(Classifier.class);
    }
  }

  /**
   * Loads all learned models as a list of learned activities to be used
   * later for classification.
   *
   * @return a list of activities previously learned
   */
  private ArrayList<Activity> load() throws IOException {
    File dir = new File(this.cfg.getAsString("dir.learned.activities"));
    FileUtils.forceMkdir(dir);
    String[] exts = {"json"};
    Iterator<File> it = FileUtils.iterateFiles(dir, exts, true);
    ArrayList<Activity> acts = new ArrayList<Activity>();
    Gson gson = new Gson();
    while (it.hasNext()) {
      File file = it.next();
      String content = FileUtils.readFileToString(file, "UTF-8");
      Activity act = gson.fromJson(content, Activity.Learned.class);
      System.out.println(act.getName());
      acts.add(act);
    }
    return acts;
  }

  /**
   *
   */
  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        Thread.sleep(this.cfg.getAsInt("classifier.sleep.interval"));
        this.classify();
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
        log.info("sleep interrupted");
      }
    }
    log.info("classifier stopped by the main thread");
  }

  /**
   *
   */
  private void classify() {
    String name = "Biking";
    double accuracy = 96.7;
    Activity act = new Activity.Classified(name, accuracy);
    this.posture.reset();
    act.log();
  }

}

