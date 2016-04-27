//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

/**
 * A packet processor is a worker thread whose job is to take raw unprocessed
 * packets, process them and update the posture based on their information.
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
   * A packet processor is constructed based on the configuration file,
   * the packet queue from which it takes raw packets and the posture
   * that it should update based on packets.
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
   * A worker simply takes packets from the packet queue, if available,
   * processes them and updates the posture based on their information.
   */
  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        Thread.sleep(this.cm.getAsInt("packet.processor.sleep.interval"));
        while (!this.pq.isEmpty()) {
          Packet packet = this.pq.get();
          packet.process();
          this.posture.update(packet.getNode(), packet.getRegion());
        }
      } catch (CurruptPacketException ex) {
        log.info("currupt packet discarded");
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
        log.info("sleep interrupted");
      }
    }
    log.info("packet processor stopped by the main thread");
  }

}
