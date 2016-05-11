//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth.cli;

import com.ghorbanzade.sloth.FatalException;

/**
 *
 *
 * @author Pejman Ghorbanzade
 * @see Cli
 * @see InvalidCommandException
 */
public interface Command {

  /**
   *
   *
   * @param instruction the instruction given by the user
   * @throws InvalidCommandException if instruction is missing required
   *         components to be executed.
   */
  void check(Instruction instruction) throws InvalidCommandException;

  /**
   *
   *
   * @param instruction the instruction given by the user
   */
  void execute(Instruction instruction) throws FatalException;

}
