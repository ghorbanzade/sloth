//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <pejman@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
* Unit test suit for banner class.
*
* @author Pejman Ghorbanzade
*/
public class BannerTest {

  /**
   * check to see whether static method prints content of a given resource
   * file as expected.
   */
  @Test
  public void checkPrintOutput() {
    try {
      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outContent, false, "UTF-8"));
      Banner.print("/test.banner");
      assertThat(outContent.toString("UTF-8"), is("\nSome banner\n\n"));
    } catch (UnsupportedEncodingException ex) {
      fail("specified encoding is not supported");
    }
  }

  /**
   * static print method should throw fatal exception if non existant
   * resource is asked for.
   */
  @Test(expected = FatalException.class)
  public void testPrintWithNonExistantResouce() {
    try {
      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outContent, false, "UTF-8"));
      Banner.print("/nonexistant.banner");
    } catch (UnsupportedEncodingException ex) {
      fail("specified encoding is not supported");
    }
  }

  /**
   * constructor of banner is private.
   */
  @Test
  public void privateConstructor() {
    Constructor<?>[] constructors = Banner.class.getDeclaredConstructors();
    for (Constructor<?> constructor : constructors) {
      assertThat(Modifier.isPrivate(constructor.getModifiers()), is(true));
    }
  }

}
