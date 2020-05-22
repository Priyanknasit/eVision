package org.tensorflow.demo.location.model;

import org.tensorflow.demo.app_logic.MvpModel;

import java.util.List;

/**
 * Created by Marius Olariu <mariuslucian.olariu@gmail.com>
 */
public interface LocationModelManager  extends MvpModel{
  void addContact(String contactName, String phoneNumber);

  List<Contact> getContacts();
}
