//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <pejman@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * A worker thread that observes the body posture beings constructed
 * and stores it along with the name of the activity being performed
 * in a file using the JSON format.
 *
 * @author Pejman Ghorbanzade
 * @see Classifier
 * @see Posture
 */
public final class Learner implements Runnable {

  private final Config cfg;
  private final String name;
  private final Posture posture;
  private static final Logger log = Logger.getLogger(Learner.class);

  /**
   * A learner runnable takes the posture object being updated by packet
   * constructor and the name of the activity being perfomed and periodically
   * stores the posture as an instance of that activity.
   *
   * @param posture the posture that should be learned
   * @param name name of the activity to be learned
   */
  public Learner(Posture posture, String name) {
    this.name = name;
    this.posture = posture;
    this.cfg = ConfigManager.get("config/main.properties");
  }

  /**
   * Continually stores posture of a given activity in a file using the
   * JSON format in specific time intervals as long as the main thread
   * has not interrupted the thread invoking this method.
   */
  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        Thread.sleep(this.cfg.getAsInt("learner.sleep.interval"));
        this.learn();
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
        log.info("sleep interrupted");
      }
    }
    log.info("learner stopped by the main thread");
  }

  /**
   * Creates an activity instance for the given activity name and logs that
   * instance in a file as a learned activity.
   */
  private void learn() {
    HashMap<Node, ActivityCode> code = new HashMap<Node, ActivityCode>();
    for (Node node: this.posture.getNodes()) {
      code.put(node, this.posture.get(node));
    }
    Activity act = new Activity.Learned(this.name, code);
    this.posture.reset();
    act.log();
  }

}
