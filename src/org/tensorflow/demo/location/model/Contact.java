package org.tensorflow.demo.location.model;

/**
 * Created by Marius Olariu <mariuslucian.olariu@gmail.com>
 */
public class Contact {
  public String contactName;
  public String phoneNumber;

  public Contact(String contactName, String phoneNumber) {
    this.contactName = contactName;
    this.phoneNumber = phoneNumber;
  }

  @Override
  public String toString() {
    return contactName + " - " + phoneNumber;
  }
}
