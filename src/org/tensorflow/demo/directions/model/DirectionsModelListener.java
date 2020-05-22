package org.tensorflow.demo.directions.model;

/**
 * Created by Marius Olariu <mariuslucian.olariu@gmail.com>
 */
public interface DirectionsModelListener {

  void onInstrFetched(Instruction instruction);
}
