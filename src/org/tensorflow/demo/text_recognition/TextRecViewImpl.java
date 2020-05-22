package org.tensorflow.demo.text_recognition;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tensorflow.demo.R;

/**
 * Created by Marius Olariu <mariuslucian.olariu@gmail.com>
 */
public class TextRecViewImpl implements TextRecView {
  private View rootView;
  private Context context;

  public TextRecViewImpl(Context context, ViewGroup container) {
    this.context = context;
    rootView = LayoutInflater.from(context).inflate(R.layout.text_rec_layout, container);
  }

  @Override
  public View getAndroidLayoutView() {
    return rootView;
  }

  @Override
  public Bundle getViewState() {
    return null;
  }

  @Override
  public void onSaveViewState(Bundle outState) {

  }

  @Override
  public void onRestoreInstanceState(Bundle inState) {

  }

}
