//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 *
 *
 * @author Pejman Ghorbanzade
 */
public final class Learner implements Runnable {

  private final Config cfg;
  private final String name;
  private final Posture posture;
  private static final Logger log = Logger.getLogger(Learner.class);

  /**
   *
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
   *
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
   *
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
