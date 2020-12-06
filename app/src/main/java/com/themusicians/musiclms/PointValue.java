package com.themusicians.musiclms;
/**
 * Class for the graph coordinates
 *
 * @author Jerome Lau
 * @since Dec 2, 2020
 *
 */
public class PointValue {
  long xValue;
  long yValue;

  public PointValue() {
  }

  public PointValue(long xValue, long yValue) {
    this.xValue = xValue;
    this.yValue = yValue;
  }

  public long getxValue() {
    return xValue;
  }

  public void setxValue(long xValue) {
    this.xValue = xValue;
  }

  public long getyValue() {
    return yValue;
  }

  public void setyValue(long yValue) {
    this.yValue = yValue;
  }
}
