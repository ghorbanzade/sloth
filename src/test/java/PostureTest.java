//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
* Unit test suit for posture class.
*
* @author Pejman Ghorbanzade
*/
public class PostureTest {

	/**
   * Passing valid region to activity code should increment its index by one.
   */
	@Test
	public void updateCodeByRegion() {
		Node node = new Node(1, "name", "desc");
		ActivityCode code = new ActivityCode(node);
		code.update(1);
		assertThat(code.getCode()[0], is(1.0));
	}

	/**
   * Passing invalid region to activity code should throw exception.
   */
  @Test(expected = UnsupportedOperationException.class)
	public void updateCodeWithInvalidRegion() {
		Node node = new Node(1, "name", "desc");
		ActivityCode code = new ActivityCode(node);
		code.update(99);
	}

	/**
   * Passing valid code to activity code should add its regions element-wise.
   */
	@Test
	public void updateCodeByCode() {
		Node node = new Node(1, "name", "desc");
		ActivityCode code1 = new ActivityCode(node);
		code1.update(1);
		ActivityCode code2 = new ActivityCode(node);
		code2.update(2);
		code1.update(code2);
		assertThat(code1.getCode()[1], is(0.5));
	}

  /**
   * Updates a posture based on a new packet.
   */
  @Test
  public void updatePostureByRegion() {
    Posture posture = new Posture();
    Node node = new Node(1, "name", "desc");
    posture.update(node, 5);
    assertThat(posture.get(node).getCode()[4], is(1.0));
  }

	/**
   * Updates a posture based on a new activity code.
   */
	@Test
	public void updatePostureByCode() {
		Posture posture = new Posture();
		Node node = new Node(1, "name", "desc");
		ActivityCode code = new ActivityCode(node);
		code.update(5);
		posture.update(code);
		assertThat(posture.get(node).getCode()[4], is(1.0));
	}

  /**
   * Checks if reset method clears entries for all nodes.
   */
  @Test
  public void resetPosture() {
    Posture posture = new Posture();
    Node node = new Node(1, "name", "desc");
    posture.update(node, 5);
		assertThat(posture.getNodes(), is(not(empty())));
		posture.reset();
    assertThat(posture.getNodes(), is(empty()));
  }

}
