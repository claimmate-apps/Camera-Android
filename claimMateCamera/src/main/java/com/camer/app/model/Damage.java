package com.camer.app.model;

import java.io.Serializable;

public class Damage
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  int damage_id;
  String damage_name;
  
  public Damage(int paramInt, String paramString)
  {
    this.damage_id = paramInt;
    this.damage_name = paramString;
  }
  
  public int getDamage_id()
  {
    return this.damage_id;
  }
  
  public String getDamage_name()
  {
    return this.damage_name;
  }
  
  public void setDamage_id(int paramInt)
  {
    this.damage_id = paramInt;
  }
  
  public void setDamage_name(String paramString)
  {
    this.damage_name = paramString;
  }
}


/* Location:              C:\Users\CTINFO\Desktop\apk\Claim Mate\classes-dex2jar.jar!\com\camer\app\model\Damage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */