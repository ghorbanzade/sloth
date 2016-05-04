//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
* Unit test suit for model and model manager.
*
* @author Pejman Ghorbanzade
*/
public class ModelTest {

  /**
   * Checks whether a given number of segments in theta direction will
   * produce correct number of total regions on the sphere.
   */
  @Test
  public void checkNumTotalRegions() {
    assertThat(new Model(8).getTotalRegions(), is(26));
    assertThat(ModelManager.get(8).getTotalRegions(), is(26));
  }

}
