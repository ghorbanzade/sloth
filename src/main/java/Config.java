//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * This class allows defining configuration parameters in a separate properties
 * file instead of resorting to hard-coding them.
 *
 * @author Pejman Ghorbanzade
 */
public final class Config {

  private final File file;
  private final Properties configs = new Properties();
  private static final Logger log = Logger.getLogger(Config.class);

  /**
   * Since the configuration file to be read is a java properties file
   * an object of this class would have a properties attribute to hold
   * the properties in memory.
   *
   * @param filename the path to the properties file
   */
  public Config(String filename) {
    this.file = new File(filename);
    if (this.file.isFile()) {
      try (InputStream fis = new FileInputStream(this.file)) {
        this.configs.load(fis);
        log.trace("loaded configuration file");
      } catch (IOException ex) {
        log.fatal("failed to load configuration file");
        throw new FatalException(this.getClass());
      }
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
    return (this.configs == null) ? "" : this.configs.getProperty(key, "");
  }

  /**
   * Convenience method to get numeric value of a configuration parameter.
   *
   * @param key the key to fetch value for
   * @return numeric value assigned to the given key
   * @throws NumberFormatException if value cannot be converted to integer
   */
  public int getAsInt(String key) throws NumberFormatException {
    return Integer.parseInt(this.getAsString(key));
  }

  /**
   * Convenience method to get numeric value of a configuration parameter.
   *
   * @param key the key to fetch value for
   * @return numeric value assigned to the given key
   * @throws NumberFormatException if value cannot be converted to integer
   */
  public double getAsDouble(String key) throws NumberFormatException {
    return Double.parseDouble(this.getAsString(key));
  }

  /**
   * A wrapper method that adds or updates a given key-value pair to propery
   * list.
   *
   * @param key the key to be updated or added
   * @param value the value to be assigned to the key
   */
  public void update(String key, String value) {
    log.trace("updated value of key " + key + " to " + value);
    this.configs.setProperty(key, value);
  }

  /**
   * Permenantly stores current properties in the file.
   */
  public void save() {
    try (FileOutputStream fos = new FileOutputStream(this.file)) {
      this.configs.store(fos, "auto-generated file");
      log.info("saved configuration file");
    } catch (FileNotFoundException ex) {
      log.error("unable to construct file stream");
    } catch (IOException ex) {
      log.error("unable to save configuration file");
    }
  }

}
