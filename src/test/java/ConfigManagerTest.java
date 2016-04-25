//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
* Unit test suit for class config manager test.
*
* @author Pejman Ghorbanzade
*/
public class ConfigManagerTest {

  /**
   * It should be possible to create config manager that points to non-existant
   * configuration file.
   */
  @Test
  public void createCfgWithNonExistantFile() {
    new ConfigManager("nonexist.properties");
  }

  /**
   * Config manager must be able to load properties of an existing properties
   * file.
   */
  @Test
  public void initCfgWithExistingFile() {
    ConfigManager cfg = new ConfigManager("/test.properties");
    cfg.init();
  }

  /**
   * Method get as string should return value associated with a valid given
   * key in string format.
   */
  @Test
  public void getAsStringWithValidKey() {
    ConfigManager cfg = new ConfigManager("/test.properties");
    cfg.init();
    assertThat(cfg.getAsString("param1.string"), is("sample.string"));
    assertThat(cfg.getAsString("param2.int"), is("12345"));
  }

  /**
   * Method get as int should return value associated with valid given key
   * as int.
   */
  @Test
  public void getAsIntWithValidKey() {
    ConfigManager cfg = new ConfigManager("/test.properties");
    cfg.init();
    assertThat(cfg.getAsInt("param2.int"), is(12345));
  }

  /**
   * If config manager is not initialized, method get as string should return
   * empty string.
   */
  @Test
  public void getAsStringWithUninitializedCfg() {
    ConfigManager cfg = new ConfigManager("/test.properties");
    assertThat(cfg.getAsString("invalid.param"), is(""));
  }

  /**
   * If config manager is not initialized, getAsInt should throw an exception
   * even if valid key is passed.
   */
  @Test(expected = NumberFormatException.class)
  public void getAsIntWithUninitializedCfg() {
    ConfigManager cfg = new ConfigManager("/test.properties");
    cfg.getAsInt("param2.int");
  }

  /**
   * Method get as string should return empty string if given a key that
   * does not exist in the property list.
   */
  @Test
  public void getAsStringWithInvalidKey() {
    ConfigManager cfg = new ConfigManager("/test.properties");
    cfg.init();
    assertThat(cfg.getAsString("invalid.param"), is(""));
  }

  /**
   * If a key does not exists in property list, calling get as int should
   * throw an exception.
   */
  @Test(expected = NumberFormatException.class)
  public void getAsIntWithInvalidKey() {
    ConfigManager cfg = new ConfigManager("/test.properties");
    cfg.init();
    cfg.getAsInt("invalid.param");
  }

  /**
   * If value associated with a given key cannot be converted to integer,
   * calling get as int should throw an exception.
   */
  @Test(expected = NumberFormatException.class)
  public void getAsIntWithNonConvertibleValue() {
    ConfigManager cfg = new ConfigManager("/test.properties");
    cfg.init();
    cfg.getAsInt("param1.string");
  }

}
