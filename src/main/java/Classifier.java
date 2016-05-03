//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

/**
 *
 *
 * @author Pejman Ghorbanzade
 * @see ActivityQueue
 * @see Queue
 */
public final class Classifier implements Runnable {

  private final Config cfg;
  private final Posture posture;
  private static final Logger log = Logger.getLogger(Classifier.class);

  /**
   *
   *
   * @param posture the posture based on which classification should be made
   * @param aq the queue on which classified activity should be put
   */
  public Classifier(Posture posture) {
    this.posture = posture;
    this.cfg = ConfigManager.get("config/main.properties");
  }

  /**
   *
   */
  @Override
  public void run() {
    // TODO
  }

}

