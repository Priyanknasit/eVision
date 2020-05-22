package org.tensorflow.demo.Help.model;

import org.tensorflow.demo.app_logic.MvpModel;

import java.util.List;

/**
 * Created by Marius Olariu <mariuslucian.olariu@gmail.com>
 */
public interface HelpModelManager  extends MvpModel{
    void addContact(String contactName, String phoneNumber);

    List<Contact> getContacts();
}
