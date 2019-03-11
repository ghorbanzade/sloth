//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <pejman@ghorbanzade.com>
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
 */
public interface Command {

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
  default void check(Instruction instruction)
      throws Cli.InvalidCommandException {
    // intentionally left blank.
  }

  /**
   * Defines the effect that a given command execution will have.
   *
   * @param cli the cli that is executing the given instruction
   * @param instruction the instruction given by the user
   * @throws Cli.Exception case an error occurs during command execution
   *         or user requests program to be terminated.
   */
  void execute(Cli cli, Instruction instruction) throws Cli.Exception;

}
