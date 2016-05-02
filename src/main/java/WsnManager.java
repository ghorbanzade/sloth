//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * Constructing a wireless sensor network is expensive as it may require
 * loading a json file and seting up its nodes. This class provides a
 * static method that ensures we construct the network object only once
 * and still have access to that object from within any class. This design
 * allows support for multiple wireless sensor networks at the same time.
 *
 * @author Pejman Ghorbanzade
 * @see Node
 * @see Wsn
 */
public final class WsnManager {

  private static final HashMap<String, Wsn> hm = new HashMap<String, Wsn>();
  private static final Logger log = Logger.getLogger(WsnManager.class);

  /**
   * This method creates a wireless sensor object based on the configuration
   * file that is given, in case such object is never asked for.
   *
   * @param filename the name of the file containing info about sensor network
   */
  public static Wsn getWsn(String filename) {
    if (hm.containsKey(filename)) {
      return hm.get(filename);
    } else {
      log.info("creating new wireless sensor network");
      Wsn wsn = new Wsn(filename);
      hm.put(filename, wsn);
      return wsn;
    }
  }

  /**
   * Prevent instantiation of this class.
   */
  private WsnManager() {
  }

}
