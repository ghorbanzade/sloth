//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

/**
 * A packet reader is a worker thread whose job is to take strings read
 * from the buffer, parse them to retrieve their information and find which
 * sensor node sent them and depending on whether they have been previously
 * processed or not, put them on packet queue for processing or directly
 * use their information to update the body posture.
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
   * A packet reader is constructed based on the configuration file,
   * the serial queue from which it should read data from, the packet
   * queue it should put raw data on and the posture to update based on
   * the processed packets.
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
    while (!Thread.currentThread().isInterrupted()) {
      try {
        Thread.sleep(this.cm.getAsInt("packet.reader.sleep.interval"));
        while (!this.sq.isEmpty()) {
          String data = this.sq.get();
          // TODO
          System.out.println(data);
        }
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
        log.info("sleep interrupted");
      }
    }
    log.info("packet reader stopped by the main thread");
  }

}
