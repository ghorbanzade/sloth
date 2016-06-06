//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth.cli;

/**
 * Frees all resources and terminates the program.
 *
 * @author Pejman Ghorbanzade
 * @see Command
 */
public final class ExitCommand implements Command {

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
    throw new Cli.GracefulExitException();
  }

}
