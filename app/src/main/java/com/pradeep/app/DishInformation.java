package com.pradeep.app;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DishInformation {
    @SerializedName("id")
    public Integer id;
    @SerializedName("name")
    public String name;
    @SerializedName("image_url")
    public String image_url;
    @SerializedName("short_desc")
    public String short_desc;
    @SerializedName("wiki_link")
    public String wiki_link;
    @SerializedName("share_link")
    public String share_link;
    @SerializedName("more_images")
    public List<String> data = new ArrayList();

    @Override
    public String toString() {
        return "AUserList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image_url='" + image_url + '\'' +
                ", short_desc='" + short_desc + '\'' +
                ", wiki_link='" + wiki_link + '\'' +
                ", share_link='" + share_link + '\'' +
                ", data=" + data +
                '}';
    }
}
