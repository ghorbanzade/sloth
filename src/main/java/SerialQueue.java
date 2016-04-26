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
 * This class defines a queue of data of sensor nodes received from
 * serial port and waiting to be parsed by the packet reader.
 *
 * @author Pejman Ghorbanzade
 * @see PacketReader
 * @see SerialReader
 */
public final class SerialQueue {

  private final Vector<String> queue = new Vector<String>();
  private final Logger log = Logger.getLogger(this.getClass());

  /**
   * A serial queue is a container of data received from a serial ports
   * through serial reader. The data is fetches and parsed by the packet
   * reader which eventually updates the body posture based on its content.
   */
  public SerialQueue() {
    // intentionally left blank
  }

  /**
   * This method is called by serial reader to put data read from serial port
   * buffer on queue to be parsed by packet reader.
   *
   * @param data data that should be put on queue
   */
  public void put(String data) {
    this.queue.add(data);
  }

  /**
   * This method is called by packet reader to check if there is data
   * received from serial port that waiting to be parsed.
   *
   * @return whether the queue is empty or not
   */
  public boolean isEmpty() {
    return this.queue.isEmpty();
  }

  /**
   * This method dequeues an element from the serial queue and hands it
   * to the packet reader to parse it and construct either a packet
   * or an activity code.
   *
   * @return the first element of the queue or null if queue is empty
   */
  public String get() {
    return (this.queue.isEmpty()) ? null : this.queue.remove(0);
  }

}
