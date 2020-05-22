package org.tensorflow.demo.Help.view;

import org.tensorflow.demo.Help.presenter.HelpScreenListener;
import org.tensorflow.demo.app_logic.MvpView;

/**
 * Created by Marius Olariu <mariuslucian.olariu@gmail.com>
 */
public interface HelpScreenView extends MvpView {

    void displayUserCurrentLocation(String userCurrentLocation);

    void setListener(HelpScreenListener listener);

    void sendMsgActivity();
}
