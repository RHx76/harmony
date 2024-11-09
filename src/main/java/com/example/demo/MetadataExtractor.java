package com.example.demo;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;


public class MetadataExtractor {

    private BufferedImage getCoverImage(File mp3File) {
        try {
            AudioFile audioFile = AudioFileIO.read(mp3File);
            Tag tag = audioFile.getTag();
            if (tag != null && tag.getFirstArtwork() != null) {
                Artwork artwork = tag.getFirstArtwork();
                ByteArrayInputStream bis = new ByteArrayInputStream(artwork.getBinaryData());
                return ImageIO.read(bis); // Returns the cover image as a BufferedImage
            }
        } catch (Exception e) {
            return null;
        }
        return null; // Return null if no artwork is found
    }

    private Image convertToFxImage(BufferedImage bufferedImage) {

        return bufferedImage==null?null:SwingFXUtils.toFXImage(bufferedImage, null);
    }

    private String getArtistName(File mp3File) {
        try {
            // Read the MP3 file and get the tag
            AudioFile audioFile = AudioFileIO.read(mp3File);
            Tag tag = audioFile.getTag();

            if (tag != null) {
                // Retrieve the artist name from the tag
                return tag.getFirst(FieldKey.ARTIST); // or TagFieldKey.ARTIST
            } else {
                System.out.println("No metadata tag found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if artist is not found
    }

    public String getSongTitle(File mp3File) {
        try {
            // Read the MP3 file and get the tag
            AudioFile audioFile = AudioFileIO.read(mp3File);
            Tag tag = audioFile.getTag();

            if (tag != null) {
                // Retrieve the song title from the tag
                return tag.getFirst(FieldKey.TITLE);
            } else {
                return mp3File.getName().substring(0,mp3File.getName().indexOf('.'));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if title is not found
    }

    public SongMetaData getMetadata(File mp3File)
    {
        BufferedImage buffImage=getCoverImage(mp3File);
        Image img=convertToFxImage(buffImage);
        String artistName=getArtistName(mp3File);
        String title=getSongTitle(mp3File);

        System.out.println("artistName:"+artistName);
        System.out.println("title:"+title);

        return new SongMetaData(title,artistName,img);
    }
}
