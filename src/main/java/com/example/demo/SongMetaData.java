package com.example.demo;

import javafx.scene.image.Image;

import java.io.File;

public class SongMetaData {
    String songName;
    String artistName;
    Image coverImage;

    SongMetaData(String songName,String artistName,Image coverImage)
    {
        this.songName=songName==null || songName.isEmpty()?"unknown":songName;
        this.artistName=artistName==null || artistName.isEmpty()?"unknown":artistName;
        this.coverImage=coverImage==null?getDefaultImage():coverImage;
    }

    static private Image getDefaultImage() {
        System.out.println("ran!!!!!!!!!!!");
        return new Image("disc.png");
    }
}
