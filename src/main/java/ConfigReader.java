//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.NumberFormatException;
import java.util.Properties;


/**
 * This class allows defining configuration parameters in a separate properties
 * file instead of resorting to hard-coding them.
 *
 * @author Pejman Ghorbanzade
 */
public final class ConfigReader {

  /**
   * Since the configuration file to be read is a java properties file
   * an object of this class would have a properties attribute to hold
   * the properties in memory.
   */
  private final Properties config = new Properties();
  private final Logger log = Logger.getLogger(this.getClass());

  /**
   * Upon instantiation, an object of this class will read content of
   * a given properties file and loads all properties for easy access.
   *
   * @param path the path to the properties file to parse
   * @throws FatalException if configuration file cannot be loaded
   */
  public ConfigReader(String path) throws FatalException {
    try (InputStream fis = ConfigReader.class.getResourceAsStream(path)) {
      this.config.load(fis);
      fis.close();
      this.log.trace("loaded configuration file");
    } catch (FileNotFoundException ex) {
      this.log.fatal("configuration file is missing");
      throw new FatalException(this.getClass());
    } catch (IOException ex) {
      this.log.fatal("failed to read configuration file");
      throw new FatalException(this.getClass());
    }
  }

  /**
   * This method is used to access the value of a property. It accepts
   * a variable number of strings, first of which, indicates the format
   * for constructing the key.
   *
   * @param key the key to fetch value for
   * @return the value assigned to the given key
   */
  public String getAsString(String key) {
    log.debug("reading value for key " + key);
    return (this.config == null) ? "" : this.config.getProperty(key, "");
  }

  /**
   * A wrapper method to return a numeric value for a given key.
   *
   * @param key the key to fetch value for
   * @return numeric value assigned to the given key
   * @throws NumberFormatException if value cannot be converted to integer
   */
  public int getAsInt(String key) throws NumberFormatException {
    return Integer.parseInt(this.getAsString(key));
  }

}
