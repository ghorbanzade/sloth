//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth.cli;

/**
 * Defines a process to be initiated when user enters a corresponding
 * instruction.
 *
 * @author Pejman Ghorbanzade
 * @see Cli
 * @see InvalidCommandException
 */
public abstract class Command {

  /**
   * Validates whether a given user instruction has the required components
   * to be converted into a specific command. By default, there is no
   * constraint for an instruction to be converted to a command. If a
   * command requires additional limitations e.g. number of arguments,
   * specific options, etc. it should override this method.
   *
   * @param instruction the instruction given by the user
   * @throws Cli.InvalidCommandException if instruction is missing required
   *         components to be executed.
   */
  public void check(Instruction instruction)
      throws Cli.InvalidCommandException {
    // intentionally left blank.
  }

  /**
   * Defines the effect that a given command execution will have.
   *
   * @param instruction the instruction given by the user
   * @throws Cli.Exception case an error occurs during command execution
   *         or user requests program to be terminated.
   */
  public abstract void execute(Instruction instruction)
      throws Cli.Exception;

}
