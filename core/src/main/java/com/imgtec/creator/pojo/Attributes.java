package com.imgtec.creator.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes extends Hateoas {

  @SerializedName("Pmin")
  @Expose
  private Integer pmin;
  @SerializedName("Pmax")
  @Expose
  private Integer pmax;
  @SerializedName("Step")
  @Expose
  private Double step;
  @SerializedName("LessThan")
  @Expose
  private Integer lessThan;
  @SerializedName("GreaterThan")
  @Expose
  private Integer greaterThan;

  public Integer getPmin() {
    return pmin;
  }

  public void setPmin(Integer pmin) {
    this.pmin = pmin;
  }

  public Integer getPmax() {
    return pmax;
  }

  public void setPmax(Integer pmax) {
    this.pmax = pmax;
  }

  public Double getStep() {
    return step;
  }

  public void setStep(Double step) {
    this.step = step;
  }

  public Integer getLessThan() {
    return lessThan;
  }

  public void setLessThan(Integer lessThan) {
    this.lessThan = lessThan;
  }

  public Integer getGreaterThan() {
    return greaterThan;
  }

  public void setGreaterThan(Integer greaterThan) {
    this.greaterThan = greaterThan;
  }
}
