//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <pejman@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth.cli;

import java.util.Map;

/**
 * Displays a list of most recent instructions executed by a given CLI.
 *
 * @author Pejman Ghorbanzade
 * @see Command
 */
public final class HistoryCommand implements Command {

  /**
   * Throws exception to signal CLI should free its resources and the program
   * should terminate per user request.
   *
   * @param cli the cli that is executing the given instruction
   * @param instruction the instruction as given by user
   * @throws Cli.Exception to signal user is gracefully terminating the program
   */
  @Override
  public void execute(Cli cli, Instruction instruction) throws Cli.Exception {
    for (Map.Entry<Integer, Instruction> entry: cli.getInstructions()) {
      System.out.printf(" %4d  %s%n", entry.getKey(), entry.getValue());
    }
  }

}
