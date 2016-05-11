//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author Pejman Ghorbanzade
 */
public final class Instruction {

  private final Date date;
  private final String name;
  private final List<String> arguments = new ArrayList<String>();
  private final List<String> options = new ArrayList<String>();

  /**
   *
   *
   * @param str sequence of characters given by the user
   */
  public Instruction(String str) {
    this.date = new Date();
    String[] strings = str.split("\\s+");
    List<String> words = new ArrayList<String>(Arrays.asList(strings));
    this.name = words.remove(0);
    for (String word: words) {
      if (word.charAt(0) == '-') {
        this.options.add(word.substring(1));
      } else {
        this.arguments.add(word);
      }
    }
  }

  /**
   * Returns the time at which the user entered this instruction in the CLI.
   *
   * @return the time at which the user input was entered in the CLI.
   */
  public Date getDate() {
    return (Date) this.date.clone();
  }

  /**
   * Returns the name of the command that the user initiated.
   *
   * @return the name of the command to be invoked
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the arguments that are given with the command by the user.
   *
   * @return arguments that are given with the command by the user.
   */
  public List<String> getArguments() {
    return this.arguments;
  }

  /**
   * Returns the options that are given with the command by the user.
   *
   * @return options that are given with the command by the user
   */
  public List<String> getOptions() {
    return this.options;
  }

  /**
   *
   *
   * @return
   */
  public String toString() {
    StringBuilder sb = new StringBuilder(this.name);
    for (String argument: this.arguments) {
      sb.append(String.format(" %s", argument));
    }
    for (String option: this.options) {
      sb.append(String.format(" -%s", option));
    }
    return sb.toString();
  }

}
