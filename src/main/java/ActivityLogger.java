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

  private final Config cfg;
  private final ActivityQueue aq;
  private final FileQueue fq;
  private static final Logger log = Logger.getLogger(ActivityLogger.class);

  /**
   *
   *
   * @param aq the queue from which activity objects should be fetched for logging
   * @param fq the queue to which names of activity log files should be written
   */
  public ActivityLogger(ActivityQueue aq, FileQueue fq) {
    this.aq = aq;
    this.fq = fq;
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
