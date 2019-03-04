//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <pejman@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth.cli;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import com.ghorbanzade.sloth.Config;
import com.ghorbanzade.sloth.ConfigManager;

import com.ghorbanzade.sloth.cli.Cli;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
* Unit test suit for Cli class.
*
* @author Pejman Ghorbanzade
*/
public class CliTest {

  /**
   * An instruction has a command name, a list of arguments and a list of
   * options. The position of the arguments and options does not affect the
   * instruction object.
   */
  @Test
  public void basicInstructionMethods() {
    Instruction ins = new Instruction("cmd -opt1 -opt2 arg1 arg2");
    assertThat(
        (double) ins.getDate().getTime(), is(closeTo(new Date().getTime(), 1.0))
    );
    assertThat(ins.getName(), is("cmd"));
    List<String> args = Arrays.asList("arg1", "arg2");
    assertThat(ins.getArguments(), is(args));
    List<String> opts = Arrays.asList("opt1", "opt2");
    assertThat(ins.getOptions(), is(opts));
    assertThat(ins.toString(), is("cmd arg1 arg2 -opt1 -opt2"));
  }

  /**
   * Two instructions are the same if and only if they have the same command
   * name, same list of arguments and same list of options.
   */
  @Test
  public void compareInstructions() {
    Instruction ins = new Instruction("cmd arg -opt");
    assertThat(ins, is(not("cmd arg -opt")));
    assertThat(ins, is(new Instruction("cmd -opt arg")));
    assertThat(ins, is(not(new Instruction("command -opt arg"))));
    assertThat(ins, is(not(new Instruction("cmd -option arg"))));
    assertThat(ins, is(not(new Instruction("cmd -opt argument"))));
    assertThat(ins.hashCode(), is(new Instruction("cmd -opt arg").hashCode()));
  }

  /**
   * Create a classified activity instance and checks its properties.
   */
  @Test
  public void testGetters() {
    Config cfg = ConfigManager.get("config/main.properties");
    Cli cli = new Cli(cfg);
    assertThat(cli.getPrompt(), is("$ "));
  }

  /**
   * CLI should parse a valid user input to a valid instruction.
   */
  @Test
  public void parseValidInstruction() {
    try {
      String userInput = "exit";
      System.setIn(new ByteArrayInputStream(userInput.getBytes("UTF-8")));
      Config cfg = ConfigManager.get("config/main.properties");
      Cli cli = new Cli(cfg);
      Instruction instr = cli.getInstruction();
      assertThat(instr, is(new Instruction(userInput)));
    } catch (UnsupportedEncodingException ex) {
      fail("specified encoding is not supported");
    } finally {
      System.setIn(System.in);
    }
  }

  /**
   * When user enters <pre>exit</pre>, graceful exception should be thrown by
   * CLI.
   */
  @Test(expected = Cli.GracefulExitException.class)
  public void exitShouldThrowException() {
    try {
      String userInput = "exit";
      System.setIn(new ByteArrayInputStream(userInput.getBytes("UTF-8")));
      Config cfg = ConfigManager.get("config/main.properties");
      Cli cli = new Cli(cfg);
      Instruction instr = cli.getInstruction();
      cli.execute(instr);
    } catch (UnsupportedEncodingException ex) {
      fail("specified encoding is not supported");
    } finally {
      System.setIn(System.in);
    }
  }

}
