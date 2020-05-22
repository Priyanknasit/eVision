package org.tensorflow.demo.directions.presenter;


import org.tensorflow.demo.directions.model.PathDto;

import java.util.List;

/**
 * Created by Marius Olariu <mariuslucian.olariu@gmail.com>
 */
public interface PathFoundListener {

  /**
   * Callback method
   * @param pathDto - dto object
   */
  void onPathsFound(List<PathDto> pathDto);
}
