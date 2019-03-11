//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <pejman@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This threadsafe class defines posture as a map of sensor nodes to their
 * activity codes.
 *
 * @author Pejman Ghorbanzade
 * @see ActivityCode
 * @see Node
 */
public final class Posture {

  private final ConcurrentHashMap<Node, ActivityCode> hm;
  private static final Logger log = Logger.getLogger(Posture.class);

  /**
   * A posture is actually a wrapper around a hashmap between sensor nodes
   * and their activity code. The posture object is created only once and
   * is updated every time a packet is processed.
   */
  public Posture() {
    this.hm = new ConcurrentHashMap<Node, ActivityCode>();
  }

  /**
   * Resets the posture to restart activity recognition. Classifier calls
   * this method after every classification. It is wise to clear the entire
   * hashmap instead of reseting activity codes themselves since we don't know
   * what sensor nodes will sent their packets the next time.
   */
  public void reset() {
    this.hm.clear();
    log.trace("posture was reset");
  }

  /**
   * Updates the posture based on data retrieved from a packet. Packet
   * processor calls this method to update the posture based on
   * information retrieved from a raw packet.
   *
   * @param node the node whose activity code should be updated
   * @param region the region with which activity code should be updated
   */
  public void update(Node node, int region) {
    this.hm.putIfAbsent(node, new ActivityCode(node));
    this.hm.get(node).update(region);
  }

  /**
   * Updates the posture object with a new activity code received by
   * the sensor network. This method is used by packet processor if the
   * packet received is already a preprocessed activity code.
   *
   * @param code the code representing distribution of accelerations
   */
  public void update(ActivityCode code) {
    this.hm.putIfAbsent(code.getNode(), new ActivityCode(code.getNode()));
    this.hm.get(code.getNode()).update(code);
  }

  /**
   * Returns the activity code mapped to a given sensor node.
   *
   * @param node sensor node whose activity code is asked for
   * @return the activity code assigned to the sensor node
   */
  public ActivityCode get(Node node) {
    return this.hm.getOrDefault(node, new ActivityCode(node));
  }

  /**
   * Returns the keys inside the wrapped hashmap. This method is used by
   * classifier to access activity codes of different sensor nodes
   * one by one.
   *
   * @return an enumerated list of keys of the wrapped hashmap
   */
  public Set<Node> getNodes() {
    return this.hm.keySet();
  }

}
