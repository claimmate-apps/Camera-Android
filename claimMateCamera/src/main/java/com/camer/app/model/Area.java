package com.camer.app.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Area
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  ArrayList<SubArea> SubAreaList;
  String area_name;
  int id;
  ArrayList<Material> materailList;
  
  public Area(int paramInt, String paramString, ArrayList<SubArea> paramArrayList, ArrayList<Material> paramArrayList1)
  {
    this.id = paramInt;
    this.area_name = paramString;
    this.SubAreaList = paramArrayList;
    this.materailList = paramArrayList1;
  }
  
  public String getArea_name()
  {
    return this.area_name;
  }
  
  public int getId()
  {
    return this.id;
  }
  
  public ArrayList<Material> getMaterailList()
  {
    return this.materailList;
  }
  
  public ArrayList<SubArea> getSubAreaList()
  {
    return this.SubAreaList;
  }
  
  public void setArea_name(String paramString)
  {
    this.area_name = paramString;
  }
  
  public void setId(int paramInt)
  {
    this.id = paramInt;
  }
  
  public void setMaterailList(ArrayList<Material> paramArrayList)
  {
    this.materailList = paramArrayList;
  }
  
  public void setSubAreaList(ArrayList<SubArea> paramArrayList)
  {
    this.SubAreaList = paramArrayList;
  }
}


/* Location:              C:\Users\CTINFO\Desktop\apk\Claim Mate\classes-dex2jar.jar!\com\camer\app\model\Area.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */