//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.util.Vector;

/**
 * This class defines a queue of unprocessed packet objects that are produced
 * by packet reader and are fetched by packet processor.
 *
 * @author Pejman Ghorbanzade
 * @see Packet
 * @see PacketQueue
 * @see Posture
 */
public final class PacketQueue {

  private final ConfigManager cfg;
  private final Vector<Packet> queue = new Vector<Packet>();
  private final Logger log = Logger.getLogger(this.getClass());

  /**
   * A packet queue is a container of raw unprocessed packets that are
   * waiting to be processed by the packet processor.
   *
   * @param cfg main configuration parameters of the program
   */
  public PacketQueue(ConfigManager cfg) {
    this.cfg = cfg;
  }

  /**
   * This method is called by packet processor to check if there are packets
   * waiting to be processed.
   *
   * @return whether the queue is empty or not
   */
  public boolean isEmpty() {
    return this.queue.isEmpty();
  }

  /**
   * This method dequeues an element from the packet queue and hands it
   * to the packet processor to be processed and used to update the posture.
   *
   * @return the first element of the queue or null if queue is empty
   */
  public Packet get() {
    return (this.queue.isEmpty()) ? null : this.queue.remove(0);
  }

}
