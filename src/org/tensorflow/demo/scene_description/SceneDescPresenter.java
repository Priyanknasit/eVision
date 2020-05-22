package org.tensorflow.demo.scene_description;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.tensorflow.demo.util.HmsActivity;

/**
 * Created by Marius Olariu <mariuslucian.olariu@gmail.com>
 */
public class SceneDescPresenter extends HmsActivity {
  private SceneDescView rootView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    rootView = new SceneDescViewImpl(this, null);
    setContentView(rootView.getAndroidLayoutView());
  }

  @Override
  public void execute(String detectedText) {

  }
}
