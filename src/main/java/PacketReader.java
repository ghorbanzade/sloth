//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.lang.NumberFormatException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

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
   * A packet reader should read sensor data from serial queue, parse it
   * to a packet object and put the packet on the packe queue for further
   * processing.
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
            System.out.println(packet);
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

  /**
   * This method parses the received sensor data into either a packet object
   * or an activity code object. It throws exception if performing this task
   * is not possible.
   *
   * @param string the buffer data received from a sensor node
   * @throws CurruptPacketException if data is missing expected information
   */
  private Packet parse(String string) throws CurruptPacketException {
    StringTokenizer st = new StringTokenizer(string, "|");
    try {
      Node node = this.wsn.getNode(Integer.parseInt(st.nextToken()));
      int[] components = Packet.parse(st);
      return new Packet(node, components);
    } catch (NoSuchElementException |
        NumberFormatException |
        NoSuchNodeException |
        PacketMismatchException ex
    ) {
      throw new CurruptPacketException();
    }
  }

}
