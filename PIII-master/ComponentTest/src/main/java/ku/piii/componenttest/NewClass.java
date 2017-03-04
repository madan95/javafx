/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ku.piii.componenttest;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static javafx.scene.input.DataFormat.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import ku.piii.model.MusicMedia;
import ku.piii.model.MusicMediaCollection;
import ku.piii.music.MusicService;
import ku.piii.music.MusicServiceFactory;

/**
 *
 * @author Madan
 */
public class NewClass {
      final static MusicService MUSIC_SERVICE = MusicServiceFactory.getMusicServiceInstance();
 
    public static void main(String args[]) throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException{
    String pathScannedOnLoad = "../test-music-files/collection-A";
     final MusicMediaCollection collection = MUSIC_SERVICE.createMusicMediaCollection(Paths.get(pathScannedOnLoad));
      List<MusicMedia> test = new ArrayList<>();
        test = collection.getMusic();
        System.out.println(test.get(0).getTitle());
        System.out.println(test.get(0).getLengthInSeconds());
        String name = test.get(0).getTitle();
        test.get(0).setTitle("Layfon");
        Mp3File mp3file = new Mp3File(test.get(0).getPath());
        ID3v1 id3v1Tag;
        if(mp3file.hasId3v1Tag()){
            id3v1Tag = mp3file.getId3v1Tag();
        }else { id3v1Tag = new ID3v1Tag(); mp3file.setId3v1Tag(id3v1Tag); }
        id3v1Tag.setTitle("Layfon");
         ID3v2 id3v2Tag;
        if (mp3file.hasId3v2Tag()) {
        	id3v2Tag =  mp3file.getId3v2Tag();
        } else {
        	id3v2Tag = new ID3v24Tag();
        	mp3file.setId3v2Tag(id3v2Tag);
        }
        id3v2Tag.setGenreDescription("Rock");
       // id3v2Tag.setTitle("Madan");
        mp3file.save("C:\\Users\\Madan\\Downloads\\New folder\\s.mp3");
      
 
    }
}
