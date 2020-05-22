package org.tensorflow.demo.util;

import android.content.Context;

import org.tensorflow.demo.Help.view.HelpScreenViewImpl;
import org.tensorflow.demo.app_logic.AppFeaturesEnum;
import org.tensorflow.demo.app_logic.MvpView;
import org.tensorflow.demo.directions.view.DirectionsScreenViewImpl;
import org.tensorflow.demo.location.view.LocationScreenViewImpl;
import org.tensorflow.demo.mainscreen.view.MainMenuScreenViewImpl;


/**
 * Created by Marius Olariu <mariuslucian.olariu@gmail.com>
 */
public class ViewsFactory {

  public static MvpView createView(Context context, AppFeaturesEnum featureId) {
    MvpView screenView = null;

    switch (featureId) {
      case DIRECTIONS:
        screenView = new DirectionsScreenViewImpl(context, null);
        break;

      case LOCATION:
        screenView = new LocationScreenViewImpl(context, null);
        break;
      case HELP:
        screenView = new HelpScreenViewImpl(context, null);
        break;

        //add the other features when they are implemented
      default:
        screenView = new MainMenuScreenViewImpl(context, null);
    }

    return screenView;
  }
  public static MvpView createView(Context context, AppFeaturesEnum featureId, String dest) {
    MvpView screenView = null;

    switch (featureId) {
      case DIRECTIONS:
        screenView = new DirectionsScreenViewImpl(context, null,dest);
        break;

      case LOCATION:
        screenView = new LocationScreenViewImpl(context, null);
        break;
      case HELP:
        screenView = new HelpScreenViewImpl(context, null);
        break;
      //add the other features when they are implemented
      default:
        screenView = new MainMenuScreenViewImpl(context, null);
    }

    return screenView;
  }

}
