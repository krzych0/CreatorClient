package com.imgtec.creatorclient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.imgtec.creator.pojo.Hateoas;

import java.util.List;


public class PointValue extends Hateoas {

	@SerializedName("SetPointValue")
	@Expose
	private float pointValue;

	@SerializedName("InstanceID")
	@Expose
	private String instanceID;


	public void setPointValue(float pointValue){
		this.pointValue = pointValue;
	}

	public float getPointValue(){
		return pointValue;
	}

	public void setInstanceID(String instanceID){
		this.instanceID = instanceID;
	}

	public String getInstanceID(){
		return instanceID;
	}

}