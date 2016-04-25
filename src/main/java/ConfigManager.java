//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
public final class ConfigManager {

  /**
   * Since the configuration file to be read is a java properties file
   * an object of this class would have a properties attribute to hold
   * the properties in memory.
   */
  private final String path;
  private final Properties config = new Properties();
  private final Logger log = Logger.getLogger(this.getClass());

  /**
   * Constructing a config manager object should not be expensive.
   *
   * @param path the path to the properties file
   */
  public ConfigManager(String path) {
    this.path = path;
  }

  /**
   * This method checks whether a given configuration file exists or not.
   * If it exists, it loads the key-value properties to memory. If it does
   * not exist, it creates the file by calling save.
   */
  public void init() {
    if (ConfigManager.class.getResource(this.path).getFile().isEmpty()) {
      log.info("creating configuration file");
      this.save();
    } else {
      log.trace("configuration file already exists");
      this.reload();
    }
  }

  /**
   * Tries to read content of a given properties file and loads all
   * properties for easy access.
   *
   * @throws FatalException if configuration file cannot be loaded
   */
  private void reload() throws FatalException {
    try (InputStream fis = ConfigManager.class.getResourceAsStream(this.path)) {
      this.config.load(fis);
      fis.close();
      log.trace("loaded configuration file");
    } catch (FileNotFoundException ex) {
      log.fatal("configuration file is missing");
      throw new FatalException(this.getClass());
    } catch (IOException ex) {
      log.fatal("failed to read configuration file");
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

  /**
   * A wrapper method that adds or updates a given key-value pair to propery
   * list.
   *
   * @param key the key to be updated or added
   * @param value the value to be assigned to the key
   */
  public void update(String key, String value) {
    this.config.setProperty(key, value);
  }

  /**
   *
   */
  public void save() {
    try (FileOutputStream fos = new FileOutputStream(new File(this.path))) {
      this.config.store(fos, "auto-generated file");
    } catch (FileNotFoundException ex) {
      log.error("unable to construct file stream");
    } catch (IOException ex) {
      log.error("saving properties was unsuccessful");
    }
  }

}
