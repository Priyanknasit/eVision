package org.tensorflow.demo.directions.model;

import android.location.Location;

import org.tensorflow.demo.app_logic.MvpModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Marius Olariu <mariuslucian.olariu@gmail.com>
 */
public interface DirectionsModelManager extends MvpModel {

  void fetchInstruction(Location newLocation);

  void setModelListener(DirectionsModelListener listener);

  void initialize(List<LatLng> currentPathCoordinates, Float radius, Float deltaT,
                  Float currentPhoneBearing);

}
