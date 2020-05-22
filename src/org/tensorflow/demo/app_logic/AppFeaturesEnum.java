package org.tensorflow.demo.app_logic;

/**
 * Created by Marius Olariu <mariuslucian.olariu@gmail.com>
 */
public enum AppFeaturesEnum {
  MAIN_MENU("Main"), DIRECTIONS("Directions"), LOCATION("Location"), TEXT_RECOGNITION(
      "Text Recognition"), SCENE_DESCRIPTION("Scene Description"),HELP("Help");

  private String featureName;

  AppFeaturesEnum(String featureName) {
    this.featureName = featureName;
  }

  @Override
  public String toString() {
    return featureName;
  }

  public static AppFeaturesEnum stringToFeature(String text) {
    AppFeaturesEnum feature = null;

    text = text.toUpperCase();
    for (AppFeaturesEnum f : AppFeaturesEnum.values()) {
      if (text.equals(f.toString().toUpperCase())) {
        feature = f;
      }
    }

    return feature;
  }
}
