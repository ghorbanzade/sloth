//
// Sloth: Integrated Distributed Activity Recognition System
//        with Minimal Network Radio Communications
//
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

/**
 * This class demonstrates how a car can be constructed and used.
 *
 * @author Pejman Ghorbanzade
 * @see Car
 */
public final class CarMain {

  /**
   * this program builds a car and prints its price.
   *
   * @param args command line arguments given to program
   */
  public static void main(String[] args) {
    Car car = new Car(10, 20, 30);
    System.out.println(car.getPrice());
  }

  /**
   * This class must not be instantiated.
   */
  private CarMain() {
  }

}
