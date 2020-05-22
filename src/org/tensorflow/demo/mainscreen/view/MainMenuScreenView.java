package org.tensorflow.demo.mainscreen.view;

import org.tensorflow.demo.app_logic.MvpView;

import java.util.List;

/**
 * Created by Marius Olariu <mariuslucian.olariu@gmail.com>
 */

public interface MainMenuScreenView extends MvpView {

  void displayAppFeatures(List<String> features);

  void setScreenListener(MainMenuScreenListener listener);

  void askTurnGpsOn();

  void askTurnInternetOn();
}
