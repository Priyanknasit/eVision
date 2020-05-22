package org.tensorflow.demo.directions.view;

import org.tensorflow.demo.app_logic.MvpView;

/**
 * Created by Marius Olariu <mariuslucian.olariu@gmail.com>
 */
public interface DirectionsScreenView extends MvpView {

  void setScreenListener(DirectionsScreenListener listener);

  void setDestination(String destination);

  void setDistance(String distance);

  void setDuration(String duration);

  void findpathEvent();

  void suggesion();
}
