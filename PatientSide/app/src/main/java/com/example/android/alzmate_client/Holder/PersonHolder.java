package com.example.android.alzmate_client.Holder;



public class PersonHolder {
    public String name;
    public String relation;
    public String bio;
    public String ImgLocation;
    public PersonHolder(String name, String relation, String bio, String ImgLocation)
    {
        this.name=name;
        this.relation=relation;
        this.bio=bio;
        this.ImgLocation=ImgLocation;
    }
    public String getName()
    {
        return  name;
    }
    public String getRelation()
    {
        return relation;
    }
    public String getBio()
    {
        return  bio;
    }
    public String getImageLocation()
    {
        return ImgLocation;
    }
}
