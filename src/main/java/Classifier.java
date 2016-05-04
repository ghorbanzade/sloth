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
 * @see Activity
 * @see Learner
 * @see Posture
 */
public final class Classifier implements Runnable {

  private ArrayList<Activity> acts;
  private final Posture posture;
  private static final Logger log = Logger.getLogger(Classifier.class);
  private static final Config cfg =
      ConfigManager.get("config/main.properties");

  /**
   *
   *
   * @param posture the posture based on which classification should be made
   * @throws FatalException if learned activity samples cannot be loaded
   */
  public Classifier(Posture posture) {
    this.posture = posture;
  }

  /**
   *
   */
  @Override
  public void run() {
    try {
      this.acts = Activity.Learned.loadAll();
      while (!Thread.currentThread().isInterrupted()) {
        try {
          Thread.sleep(cfg.getAsInt("classifier.sleep.interval"));
          this.classify(this.acts);
        } catch (InterruptedException ex) {
          Thread.currentThread().interrupt();
          log.info("sleep interrupted");
        }
      }
      log.info("classifier stopped by the main thread");
    } catch (IOException ex) {
      log.fatal("unable to load activity models");
      Thread.currentThread().interrupt();
      throw new FatalException(Classifier.class);
    }
  }

  /**
   *
   *
   * @param acts activities to be compared against
   */
  private void classify(ArrayList<Activity> acts) {
    String name = "Biking";
    double accuracy = 96.7;
    Activity act = new Activity.Classified(name, accuracy);
    this.posture.reset();
    act.log();
  }

}
