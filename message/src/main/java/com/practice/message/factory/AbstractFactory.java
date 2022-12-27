package com.practice.message.factory;

public interface AbstractFactory<T> {

  /**
   * To create the object for the type of Messaging.
   *
   * @param type email or sms
   * @return object
   */
  T create(String type);
}
