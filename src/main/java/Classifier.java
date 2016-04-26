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

  private final Logger log = Logger.getLogger(this.getClass());
  private final ConfigManager cm;
  private final ActivityQueue aq;
  private final Posture posture;

  /**
   *
   *
   * @param cm main configuration parameters of the program
   * @param posture the posture based on which classification should be made
   * @param aq the queue on which classified activity should be put
   */
  public Classifier(ConfigManager cm, Posture posture, ActivityQueue aq) {
    this.cm = cm;
    this.aq = aq;
    this.posture = posture;
  }

  /**
   *
   */
  @Override
  public void run() {
    // TODO
  }

}

