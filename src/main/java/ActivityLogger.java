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
 * @see Activity
 * @see ActivityQueue
 * @see FileQueue
 */
public final class ActivityLogger implements Runnable {

  private final Logger log = Logger.getLogger(this.getClass());
  private final ConfigManager cm;
  private final ActivityQueue aq;
  private final FileQueue fq;

  /**
   *
   *
   * @param cm main configuration parameters of the program
   * @param aq the queue from which activity objects should be fetched for logging
   * @param fq the queue to which names of activity log files should be written
   */
  public ActivityLogger(ConfigManager cm, ActivityQueue aq, FileQueue fq) {
    this.cm = cm;
    this.aq = aq;
    this.fq = fq;
  }

  /**
   *
   */
  @Override
  public void run() {
    // TODO
  }

}
