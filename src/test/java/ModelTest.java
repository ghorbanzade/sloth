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
* Unit test suit for model and model manager.
*
* @author Pejman Ghorbanzade
*/
public class ModelTest {

  /**
   *
   */
  @Test
  public void checkNumTotalRegions() {
    assertThat(new Model(8).getTotalRegions(), is(26));
    assertThat(ModelManager.get(8).getTotalRegions(), is(26));
  }

}
