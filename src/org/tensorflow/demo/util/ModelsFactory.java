package org.tensorflow.demo.util;

import android.content.Context;

import org.tensorflow.demo.Help.model.HelpModelManagerImpl;
import org.tensorflow.demo.app_logic.AppFeaturesEnum;
import org.tensorflow.demo.app_logic.MvpModel;
import org.tensorflow.demo.directions.model.DirectionsModelManagerImpl;
import org.tensorflow.demo.location.model.LocationModelManagerImpl;

/**
 * Created by Marius Olariu <mariuslucian.olariu@gmail.com>
 */
public class ModelsFactory {

  public static MvpModel createModel(Context context, AppFeaturesEnum featureId) {
    MvpModel mvpModel = null;
    switch (featureId) {
      case DIRECTIONS:
        mvpModel = new DirectionsModelManagerImpl();

        break;

      case LOCATION:
        mvpModel = new LocationModelManagerImpl(context);

        break;

      case HELP:
        mvpModel = new HelpModelManagerImpl(context);

        break;
      default:
    }

    return mvpModel;
  }

}
