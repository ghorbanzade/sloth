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
 * This class defines a queue of activity objects either learned or recognized
 * to be used by activity logger that writes them in files.
 *
 * @author Pejman Ghorbanzade
 * @see Activity
 * @see ActivityLogger
 * @see Classifier
 * @see Learner
 */
public final class ActivityQueue {

  private final Vector<Activity> queue = new Vector<Activity>();
  private static final Logger log = Logger.getLogger(ActivityQueue.class);

  /**
   * An activity queue is a container for recognized or learned activities
   * that is used by learner and classifier threads to transfer their product
   * to the activity logger thread.
   */
  public ActivityQueue() {
    // intentionally left blank
  }

  /**
   * This method is called by file logger to check if there are activity
   * objects waiting to be logged to a file.
   *
   * @return whether the queue is empty or not
   */
  public boolean isEmpty() {
    return this.queue.isEmpty();
  }

  /**
   * This method dequeues an element from the Activity queue and returns it
   * to the file logger object to generate a json file for it.
   *
   * @return the first element of the queue or null if queue is empty
   */
  public Activity get() {
    return (this.queue.isEmpty()) ? null : this.queue.remove(0);
  }

}

