//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* Unit test suit for activity class.
*
* @author Pejman Ghorbanzade
*/
public class ActivityTest {

  /**
   * create a classified activity instance and checks its properties.
   */
  @Test
  public void createClassifiedActivity() {
    Activity act = new Activity.Classified("activity-name", 10.0);
    assertThat(act.getName(), is("activity-name"));
    assertThat(act.getAccuracy(), is(10.0));
    assertThat(
        (double) act.getDate().getTime(),
        is(closeTo(new Date().getTime(), 1.0))
    );
    assertThat(act.toString(), is("activity-name"));
  }

  /**
   * create a learned activity instance and checks its properties.
   */
  @Test
  public void createLearnedActivity() {
    Node node = new Node(1, "name", "description");
    double code[] = new double[26];
    Arrays.fill(code, 0);
    ActivityCode ac = new ActivityCode(node, code);
    Map<Node, ActivityCode> posture = new HashMap<Node, ActivityCode>();
    posture.put(node, ac);
    Activity act = new Activity.Learned("activity-name", posture);
    assertThat(act.getName(), is("activity-name"));
    assertThat(act.getPosture(), is(posture));
    assertThat(act.toString(), is("activity-name"));
  }

}
