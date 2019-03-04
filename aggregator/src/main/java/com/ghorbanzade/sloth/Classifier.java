//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <pejman@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class defines a classifier as a worker thread that reads the posture
 * every time interval and compares the posture with those of all learned
 * activities.
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
  private static final Config cfg = ConfigManager.get("config/main.properties");

  /**
   * Creates a classifier object that matches the posture being constructed
   * with all learned activity models to identify the activity whose posture
   * best matches current posture.
   *
   * @param posture the posture based on which classification should be made
   */
  public Classifier(Posture posture) {
    this.posture = posture;
  }

  /**
   * Classifier first loads all learned activity models in memory and then
   * continues to perform classification until it is interrupted by the
   * main thread.
   */
  @Override
  public void run() {
    try {
      this.acts = Activity.Learned.loadAll();
      log.trace("loaded activity models");
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
   * Classifies activity being performed based on a given list of activities.
   *
   * @param acts activities to be compared against
   */
  private void classify(ArrayList<Activity> acts) {
    if (this.posture.getNodes().isEmpty()) {
      return;
    }
    double accuracy = Double.MIN_VALUE;
    String name = null;
    for (Activity act: acts) {
      double similarity = 0;
      for (Node node: this.posture.getNodes()) {
        ActivityCode code = act.getPosture().get(node);
        if (code == null) {
          continue;
        }
        similarity += this.posture.get(node).findSimilarity(code);
      }
      similarity /= this.posture.getNodes().size();
      log.trace("similarity with " + act.getName() + ": " + similarity);
      if (similarity > accuracy) {
        name = act.getName();
        accuracy = similarity;
      }
    }
    accuracy *= 100;
    if (accuracy < cfg.getAsInt("classifier.accuracy.threshold")) {
      log.info("recognition accuracy too low to report activity");
      return;
    }
    Activity act = new Activity.Classified(name, accuracy);
    log.info("identified " + name + " with recognition accuracy " + accuracy);
    this.posture.reset();
    act.log();
  }

}
