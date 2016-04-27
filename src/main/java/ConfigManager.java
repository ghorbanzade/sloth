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
 *
 *
 * @author Pejman Ghorbanzade
 * @see Config
 */
public final class ConfigManager {

  private static final HashMap<String, Config> hm =
      new HashMap<String, Config>();
  private static final Logger log = Logger.getLogger(ConfigManager.class);

  /**
   *
   *
   * @param filename the name of the file containing configuration
   */
  public static Config get(String filename) {
    if (hm.containsKey(filename)) {
      return hm.get(filename);
    } else {
      Config cfg = new Config(filename);
      hm.put(filename, cfg);
      return cfg;
    }
  }

  /**
   * Prevent instantiation of this class.
   */
  private ConfigManager() {
  }

}
