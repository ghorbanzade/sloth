//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.lang.NumberFormatException;
import java.util.StringTokenizer;
import java.util.Arrays;

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

  private final Wsn wsn;
  private final Config cfg;
  private final PacketQueue pq;
  private final Posture posture;
  private final SerialQueue sq;
  private static final Logger log = Logger.getLogger(PacketReader.class);

  /**
   * A packet reader is constructed based on the serial queue from which
   * it should read data from, the packet queue it should put raw data on
   * and the posture to update based on the processed packets.
   *
   * @param sq queue from which sensor data should be read
   * @param pq queue to which unprocessed packets should written
   * @param posture the posture that should be updated with processed packet
   */
  public PacketReader(SerialQueue sq, PacketQueue pq, Posture posture) {
    this.sq = sq;
    this.pq = pq;
    this.posture = posture;
    this.cfg = ConfigManager.get("config/main.properties");
    this.wsn = WsnManager.getWsn(this.cfg.getAsString("config.file.wsn"));
  }

  /**
   *
   */
  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        Thread.sleep(this.cfg.getAsInt("packet.reader.sleep.interval"));
        while (!this.sq.isEmpty()) {
          String data = this.sq.get();
          try {
            Packet packet = this.parse(data);
            log.trace("received packet: " + packet.toString());
            this.pq.put(packet);
          } catch (CurruptPacketException ex) {
            log.info("currupt packet discarded");
          }
        }
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
        log.info("sleep interrupted");
      }
    }
    log.info("packet reader stopped by the main thread");
  }

  public Packet parse(String string) throws CurruptPacketException {
    StringTokenizer st = new StringTokenizer(string, "|");
    int[] components = new int[7];
    if (st.countTokens() != components.length) {
      throw new CurruptPacketException();
    }
    try {
      for (int i = 0; i < components.length; i++) {
        components[i] = Integer.parseInt(st.nextToken());
      }
    } catch (NumberFormatException ex) {
      throw new CurruptPacketException();
    }
    Packet packet = new Packet(
        this.wsn.getNode(components[0]),
        Arrays.copyOfRange(components, 1, components.length)
    );
    return packet;
  }

}
