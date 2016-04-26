//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class defines a runnable object that can be passed to a shutdown hook
 * thread. Once shutdown is initiated, the thread will try to close all the
 * resources previously declared to it.
 *
 * @author Pejman Ghorbanzade
 * @see SerialReader
 */
public final class ResourceManager implements Runnable {

  private final Logger log = Logger.getLogger(this.getClass());
  private final ArrayList<Closeable> resources;

  /**
   * A resource manager is first created with an empty list of closeable
   * objects.
   */
  public ResourceManager() {
    this.resources = new ArrayList<Closeable>();
  }

  /**
   * This method allows adding multiple closeable resources to the resource
   * manager.
   *
   * @param resource the new closeable resource to be closed at shutdown
   */
  public void add(Closeable resource) {
    this.resources.add(resource);
  }

  /**
   * Once shutdown is initiated, resource manager should try to close all
   * resources previously added for clean up.
   */
  @Override
  public void run() {
    log.info("starting to clean up resources");
    for (Closeable resource: this.resources) {
      try {
        log.info("closing resource" + resource.getClass().getName());
        resource.close();
      } catch (IOException ex) {
        log.error("unable to close resource"+resource.getClass().getSimpleName());
      }
    }
  }

}

