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
 * This class defines a queue of file paths that are fetched by uploader
 * to be transfered to the remote server.
 *
 * @author Pejman Ghorbanzade
 * @see ActivityLogger
 * @see Uploader
 */
public final class FileQueue {

  private final Vector<Path> queue = new Vector<Path>();
  private static final Logger log = Logger.getLogger(FileQueue.class);

  /**
   * A file queue is a container of paths to files that need to be uploaded
   * to the remote server by uploader. The queue is filled with paths to
   * files written by the activity logger.
   */
  public FileQueue() {
    // intentionally left blank
  }

  /**
   * This method is called by uploader to check if there are files waiting
   * to be uploaded to the remote server.
   *
   * @return whether the queue is empty or not
   */
  public boolean isEmpty() {
    return this.queue.isEmpty();
  }

  /**
   * This method dequeues an element from the File queue and returns it
   * to the uploader object to upload the file to the remote server.
   *
   * @return the first element of the queue or null if queue is empty
   */
  public Path get() {
    return (this.queue.isEmpty()) ? null : this.queue.remove(0);
  }

}
