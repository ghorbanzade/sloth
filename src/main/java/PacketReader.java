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
 * @see Packet
 * @see PacketQueue
 * @see Posture
 * @see SerialQueue
 */
public final class PacketReader implements Runnable {

  private final Logger log = Logger.getLogger(this.getClass());
  private final ConfigManager cm;
  private final PacketQueue pq;
  private final Posture posture;
  private final SerialQueue sq;

  /**
   *
   *
   * @param cm main configuration parameters of the program
   * @param sq queue from which sensor data should be read
   * @param pq queue to which unprocessed packets should written
   * @param posture the posture that should be updated with processed packet
   */
  public PacketReader(
      ConfigManager cm, SerialQueue sq, PacketQueue pq, Posture posture
  ) {
    this.cm = cm;
    this.sq = sq;
    this.pq = pq;
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
