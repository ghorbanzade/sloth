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
 * Constructing a configuration object is expensive as it may require
 * loading contents of a file. This class provides a static method that
 * ensures we construct the configuration object only once and still have
 * access to that object from within any class. This design allows support
 * for multiple configuration objects at the same time.
 *
 * @author Pejman Ghorbanzade
 * @see Config
 */
public final class ConfigManager {

  private static final HashMap<String, Config> hm =
      new HashMap<String, Config>();
  private static final Logger log = Logger.getLogger(ConfigManager.class);

  /**
   * This static method allows client to access configurations of a file
   * while preventing extensive construction of a configuration object
   * if the file has previously been asked for.
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
