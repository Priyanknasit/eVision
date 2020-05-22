package org.tensorflow.demo.location.view;

import org.tensorflow.demo.app_logic.MvpView;
import org.tensorflow.demo.location.presenter.LocationScreenListener;

/**
 * Created by Marius Olariu <mariuslucian.olariu@gmail.com>
 */
public interface LocationScreenView extends MvpView {

  void displayUserCurrentLocation(String userCurrentLocation);

  void setListener(LocationScreenListener listener);

  void sendMsgActivity();

  String readLocation();
}
