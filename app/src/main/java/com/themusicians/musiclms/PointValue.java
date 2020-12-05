package com.themusicians.musiclms;

public class PointValue {
  int xValue;
  long yValue;

  public PointValue() {
  }

  public PointValue(int xValue, long yValue) {
    this.xValue = xValue;
    this.yValue = yValue;
  }

  public int getxValue() {
    return xValue;
  }

  public void setxValue(int xValue) {
    this.xValue = xValue;
  }

  public long getyValue() {
    return yValue;
  }

  public void setyValue(long yValue) {
    this.yValue = yValue;
  }
}
