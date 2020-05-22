package org.tensorflow.demo.app_logic;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.tensorflow.demo.directions.presenter.DirectionsScreenPresenter;
import org.tensorflow.demo.location.presenter.LocationScreenPresenter;
import org.tensorflow.demo.scene_description.SceneDescPresenter;
import org.tensorflow.demo.text_recognition.TextRecPresenter;
import org.tensorflow.demo.util.HmsActivity;

/**
 * Created by Marius Olariu <mariuslucian.olariu@gmail.com>
 */
public class CommandProcessor {

  private static final CommandProcessor ourInstance = new CommandProcessor();

  private static final Class<?> directionPresClass = DirectionsScreenPresenter.class;
  private static final Class<?> locationPresClass = LocationScreenPresenter.class;
  private static final Class<?> textRecClass = TextRecPresenter.class;
  private static final Class<?> sceneDescClass = SceneDescPresenter.class;
  private static final Class<?> mainMenuClass = SceneDescPresenter.class;


  public static CommandProcessor getInstance() {

    return ourInstance;
  }

  /**
   * @param detectedText - detected text from voice speech
   */
  public void processCommand(String detectedText, HmsActivity hmsActivity) {
    AppFeaturesEnum feature = AppFeaturesEnum.stringToFeature(detectedText);

    if (feature != null) {
      changeScreen(feature, hmsActivity.getBaseContext());
    }    //if it's not a feature then it might be another command like "Take Picture"
    else {
      hmsActivity.execute(detectedText);
    }

  }

  private void changeScreen(AppFeaturesEnum feature, Context currentContext) {
    // Context currentContext = AppState.getInstance().getCurrentContext();

    switch (feature) {
      case DIRECTIONS:
        currentContext.startActivity(new Intent(currentContext, directionPresClass));
        break;

      case LOCATION:
        currentContext.startActivity(new Intent(currentContext, locationPresClass));
        break;

      case TEXT_RECOGNITION:
        Log.i(Constants.HMS_INFO, "Feature not implemented yet! ");
//        currentContext.startActivity(new Intent(currentContext, textRecClass));
        break;

      case SCENE_DESCRIPTION:
        Log.i(Constants.HMS_INFO, "Feature not implemented yet! ");
//        currentContext.startActivity(new Intent(currentContext, sceneDescClass));
        break;

      default:
        currentContext.startActivity(new Intent(currentContext, mainMenuClass));
    }
  }


}
