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
 */
public final class PacketProcessor implements Runnable {

  private final Logger log = Logger.getLogger(this.getClass());
  private final ConfigManager cm;
  private final PacketQueue pq;
  private final Posture posture;

  /**
   *
   *
   * @param cm main configuration parameters of the program
   * @param pq queue from which packets should be fetched for processing
   * @param posture the posture that should be updated with processed packet
   */
  public PacketProcessor(ConfigManager cm, PacketQueue pq, Posture posture) {
    this.cm = cm;
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
