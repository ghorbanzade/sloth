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

  private final Vector<Packet> queue = new Vector<Packet>();
  private static final Logger log = Logger.getLogger(PacketQueue.class);

  /**
   * A packet queue is a container of raw unprocessed packets that are
   * waiting to be processed by the packet processor.
   */
  public PacketQueue() {
    // intentionally left blank
  }

  /**
   * This method is called by packet reader when it parses raw sensor data.
   * The packet is put on queue, waiting to be fetched by packet processor
   * for further processing.
   *
   * @param packet packet that should be put on queue
   */
  public void put(Packet packet) {
    this.queue.add(packet);
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
