//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.nio.file.Path;
import java.util.Vector;

/**
 *
 *
 * @author Pejman Ghorbanzade
 */
public final class FileQueue {

  /**
   *
   */
  private final Vector<Path> queue = new Vector<Path>();
  private final ConfigManager cfg;
  private final Logger log = Logger.getLogger(this.getClass());

  /**
   *
   *
   * @param cfg
   */
  public FileQueue(ConfigManager cfg) {
    this.cfg = cfg;
  }

  /**
   *
   *
   * @return
   */
  public Vector<Path> getQueue() {
    return this.queue;
  }

}
