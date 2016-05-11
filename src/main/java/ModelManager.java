//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import java.util.HashMap;

/**
 * Constructing a recognition model is computationally expensive. This class
 * provides a static method that helps avoid reconstruction of a model that has
 * previously been asked for. This design allows support for multiple
 * recognition models at the same time.
 *
 * @author Pejman Ghorbanzade
 * @see Model
 */
public final class ModelManager {

  private static final HashMap<Integer, Model> hm = new HashMap<Integer, Model>();

  /**
   * This static method allows client to access a recognition model
   * while preventing reconstruction of a model if it has previously
   * been asked for.
   *
   * @param regions number of theta segments of the recognition sphere
   */
  public static Model get(int regions) {
    if (hm.containsKey(regions)) {
      return hm.get(regions);
    } else {
      Model model = new Model(regions);
      hm.put(regions, model);
      return model;
    }
  }

  /**
   * Prevent instantiation of this class.
   */
  private ModelManager() {
  }

}
