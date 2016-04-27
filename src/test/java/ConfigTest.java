//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
* Unit test suit for class config manager test.
*
* @author Pejman Ghorbanzade
*/
public class ConfigTest {

  /**
   * It should be possible to create a config object that points to non-existant
   * configuration file.
   */
  @Test
  public void createCfgWithNonExistantFile() {
    Config cfg = ConfigManager.get("nonexist.properties");
    assertThat(cfg, is(not(nullValue())));
  }

  /**
   * It should be possible to create a config object that points to an existing
   * configuration file.
   */
  @Test
  public void initCfgWithExistingFile() {
    Config cfg = ConfigManager.get("src/test/resources/test.properties");
    assertThat(cfg, is(not(nullValue())));
  }

  /**
   * Method get as string should return value associated with a valid given
   * key in string format.
   */
  @Test
  public void getAsStringWithValidKey() {
    Config cfg = ConfigManager.get("src/test/resources/test.properties");
    assertThat(cfg.getAsString("param1.string"), is("sample.string"));
    assertThat(cfg.getAsString("param2.int"), is("12345"));
  }

  /**
   * Method get as int should return value associated with valid given key
   * as int.
   */
  @Test
  public void getAsIntWithValidKey() {
    Config cfg = ConfigManager.get("src/test/resources/test.properties");
    assertThat(cfg.getAsInt("param2.int"), is(12345));
  }

  /**
   * Method get as string should return empty string if given a key that
   * does not exist in the property list.
   */
  @Test
  public void getAsStringWithInvalidKey() {
    Config cfg = ConfigManager.get("src/test/resources/test.properties");
    assertThat(cfg.getAsString("invalid.param"), is(""));
  }

  /**
   * If a key does not exists in property list, calling get as int should
   * throw an exception.
   */
  @Test(expected = NumberFormatException.class)
  public void getAsIntWithInvalidKey() {
    Config cfg = ConfigManager.get("src/test/resources/test.properties");
    cfg.getAsInt("invalid.param");
  }

  /**
   * If value associated with a given key cannot be converted to integer,
   * calling get as int should throw an exception.
   */
  @Test(expected = NumberFormatException.class)
  public void getAsIntWithNonConvertibleValue() {
    Config cfg = ConfigManager.get("src/test/resources/test.properties");
    cfg.getAsInt("param1.string");
  }

  /**
   * update method should update value of existing key.
   */
  @Test
  public void updateValueOfExistingKey() {
    Config cfg = ConfigManager.get("src/test/resources/test.properties");
    assertThat(cfg.getAsString("param3.string"), is("to.be.updated"));
    cfg.update("param3.string", "updated.string");
    assertThat(cfg.getAsString("param3.string"), is("updated.string"));
  }

  /**
   * update method should be able to add a new key-value pair to properties.
   */
  @Test
  public void addKeyValuePair() {
    Config cfg = ConfigManager.get("src/test/resources/test.properties");
    cfg.update("param4.string", "new key value pair");
    assertThat(cfg.getAsString("param4.string"), is("new key value pair"));
    
  }

  /**
   * constructor of config manager is private.
   */
  @Test
  public void privateConstructor() {
    Constructor<?>[] constructors = ConfigManager.class.getDeclaredConstructors();
    for (Constructor<?> constructor : constructors) {
      assertThat(Modifier.isPrivate(constructor.getModifiers()), is(true));
    }
  }

}
