package ku.piii.madanapp;

import com.google.common.io.Files;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import ku.piii.model.MusicMedia;
import ku.piii.model.MusicMediaCollection;
import ku.piii.model.MusicMediaColumnInfo;
import ku.piii.music.MusicService;
import ku.piii.music.MusicServiceFactory;
import ku.piii.twocollectionsmodel.TwoCollectionsModel;

public class FXMLController implements Initializable {

    private Map<String, Integer> map = new HashMap<>();  
    private Map<String, Integer> map2 = new HashMap<>();  
    private TwoCollectionsModel model = new TwoCollectionsModel();
    private String second, first;
    private final static MusicService MUSIC_SERVICE = MusicServiceFactory.getMusicServiceInstance();
    MusicMediaCollection collection ;
    private ObservableList<MusicMedia> dataForTableView;
    private ArrayList<Integer> sam;
    private ArrayList<Integer> diff = new ArrayList<>();
    private Stage stage;
    private Parent root; 
    private List<MusicMedia> playlist = new ArrayList<>();
    private List<MusicMedia> l2 = new ArrayList<>();
    private String currentD;
    
    @FXML
    private TableView<MusicMedia> tableView;
     @FXML
    private TextField firstField, secondField, desti, newTitle, newGenre, newLocation;
   
    
         
    @FXML
    private void saveNew() throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException{
     l2.clear();
     ObservableList<MusicMedia> data = tableView.getItems();
     String title = newTitle.getText();
     String genre = newGenre.getText();
     String place = newLocation.getText();
     
    
        System.out.println(title);
    List<MusicMedia> test = new ArrayList<>();
     l2.add(tableView.getSelectionModel().getSelectedItem());    
        Mp3File mp3file = new Mp3File(l2.get(0).getPath());
        ID3v1 id3v1Tag;
        if(mp3file.hasId3v1Tag()){
            id3v1Tag = mp3file.getId3v1Tag();
        }else { id3v1Tag = new ID3v1Tag(); mp3file.setId3v1Tag(id3v1Tag); }
        id3v1Tag.setTitle(title);
        
        ID3v2 id3v2Tag;
        if (mp3file.hasId3v2Tag()) {
        	id3v2Tag =  mp3file.getId3v2Tag();
        } else {
        	id3v2Tag = new ID3v24Tag();
        	mp3file.setId3v2Tag(id3v2Tag);
        }
        id3v2Tag.setTitle(title);
        id3v2Tag.setGenreDescription(genre);
        
        mp3file.save(place);
      /*  File d;
        File s = new File(l2.get(0).getPath());
        if(genre.equalsIgnoreCase("")){
        d = new File(l2.get(0).getPath());
        }else{
         d = new File(l2.get(0).getPath()+genre+".mp3");  
        }
        */
    }

        
    @FXML
    private void edit() throws IOException{
        stage = (Stage)tableView.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/fxml/edit.fxml")); 
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
   
 
    
    

    
    
    
    @FXML
    private void playTran() throws IOException{
        stage = (Stage)tableView.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/fxml/Playlist.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        System.out.println(tableView.getItems().toString());
    }
    
    @FXML
    private void add(ActionEvent event){
        playlist.add(tableView.getSelectionModel().getSelectedItem());
       }
    
    @FXML
    private void transfer(ActionEvent event) throws IOException{
        for(int i=0; i<=playlist.size()-1; i++){
     System.out.println(playlist.get(i).getPath());       
     File source = new File(playlist.get(i).getPath());
       File dst = new File(desti.getText()+"\\" + playlist.get(i).getTitle() +".mp3");
     Files.copy(source, dst);
   
             }
    }
    
        @FXML
    private void reset(){
      playlist.clear();
    }
       
    
    
   
    
    
    
      @FXML
    private void simiScreen() throws IOException{
        stage = (Stage)tableView.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/fxml/DiffSimi.fxml")); 
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private void findSimilarity(ActionEvent event){
        sam = new ArrayList<>();
        first = firstField.getText();
        second = secondField.getText();
      
          final MusicMediaCollection collection = MUSIC_SERVICE.createMusicMediaCollection(Paths.get(first));
          List<MusicMedia> lister = collection.getMusic();
          model.setFirstCollection(lister);
 
          final MusicMediaCollection collection2 = MUSIC_SERVICE.createMusicMediaCollection(Paths.get(second));
          List<MusicMedia> lister2 = collection2.getMusic();
          model.setSecondCollection(lister2);
 
          comp();
          displaySimi(); 
    }

    public void comp(){
        String firstTitle, secondTitle;
        int a = model.getFirstCollection().getMusic().size();
        int b = model.getSecondCollection().getMusic().size();
        for(int i=0; i<=a-1; i++){
            for(int j=0; j<=b-1; j++){
                firstTitle = model.getFirstCollection().getMusic().get(i).getTitle();
                secondTitle = model.getSecondCollection().getMusic().get(j).getTitle();
                     
                    if(firstTitle.equalsIgnoreCase(secondTitle)){  
                           sam.add(i);   
                    }else {
         
                    }
                }                                       
                }
            }
    
        @FXML
    private void displaySimi() {
        final MusicMediaCollection collection = MUSIC_SERVICE
                .createMusicMediaCollection(Paths.get(firstField.getText()));
              dataForTableView = FXCollections.observableArrayList();
     
       for(int i=0; i<=sam.size()-1; i++){
            dataForTableView.add(collection.getMusic().get(sam.get(i)));
        }
        dataForTableView.addListener(makeChangeListener(collection));
        List<MusicMediaColumnInfo> myColumnInfoList = TableViewFactory.makeColumnInfoList();
   
        tableView.setItems(dataForTableView);
        TableViewFactory.makeTable(tableView, myColumnInfoList);
        tableView.setEditable(true);
      }
  

    
    
    
    
        
    @FXML
    private void findDifference(){
        first = firstField.getText();
        second = secondField.getText();
          stage = (Stage)tableView.getScene().getWindow();
          stage.setTitle("Graph");
          stage.setWidth(800);
          stage.setHeight(500);
         Scene scene = new Scene(new Group());
        
        VBox root2 = new VBox();     
        NumberAxis yAxis = new NumberAxis();
        CategoryAxis xAxis = new CategoryAxis();
      
        yAxis.setLabel("Number");
        xAxis.setLabel("Genre");     
        difGenre1(first);
       difGenre2(second);
        int i = 0;
        HashSet<String> set=new HashSet<>(); 
         for(Map.Entry m: map.entrySet()){
           set.add((String) m.getKey());
           }
      for(Map.Entry m: map2.entrySet()){
            set.add((String) m.getKey());
      }
           System.out.println(set);
           xAxis.setCategories(FXCollections.<String> observableArrayList(set));
            final BarChart<String, Number> BarChart = 
                new BarChart<String, Number>(xAxis, yAxis);
        BarChart.setTitle("Number of Music with Genre in Both Collection");
        
        XYChart.Series<String,Number> series1 = new XYChart.Series();
        series1.setName("First Collection");
        
        for(Map.Entry m: map.entrySet()){
                   series1.getData().add(new XYChart.Data(m.getKey(),m.getValue()));
               }
       XYChart.Series<String,Number> series2 = new XYChart.Series();
     series2.setName("Second Collection");
     for(Map.Entry m: map2.entrySet()){
              series2.getData().add(new XYChart.Data(m.getKey(),m.getValue()));
            }
         BarChart.getData().addAll(series1, series2);
               
        final Button btn = new Button();
        btn.setText("Back");
        btn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                                 System.out.println("bACK");
                    stage = (Stage)btn.getScene().getWindow();
                try {
                    root = FXMLLoader.load(getClass().getResource("/fxml/start.fxml"));
                } catch (IOException ex) {
                    Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                          
            }
        });
       
        root2.getChildren().addAll(BarChart);
        root2.getChildren().add(btn);
        scene.setRoot(root2);
        stage.setScene(scene);
        stage.show();
        
    }
    
    @FXML
    private void difGenre1(String first){
        String gen;
        int counter = 0;
        final MusicMediaCollection collection = MUSIC_SERVICE.createMusicMediaCollection(Paths.get(first));
          List<MusicMedia> lister = collection.getMusic();
        for(int i=0; i<=lister.size()-1; i++){
            counter =0;
               gen = lister.get(i).getGenre();
         for(Map.Entry m : map.entrySet()){
            if(m.getKey()==gen){
                counter = (int) m.getValue();
            }
          }
       if(gen==null){
          map.put("Something Else", counter+1);
         }else{
         map.put(gen, counter+1); 
         }
        }
      for(Map.Entry m: map.entrySet()){
      System.out.println(m.getKey()+ " = "+m.getValue());
     }
   }
    
        @FXML
    private void difGenre2(String second){
        String gen;
        int counter2 = 0;
        final MusicMediaCollection collection = MUSIC_SERVICE.createMusicMediaCollection(Paths.get(second));
          List<MusicMedia> lister = collection.getMusic();
        for(int i=0; i<=lister.size()-1; i++){
            counter2 =0;
               gen = lister.get(i).getGenre();
         for(Map.Entry m : map2.entrySet()){
           if(m.getKey()==gen){
                counter2 = (int) m.getValue();
            }
          }
        
     if(gen==null){
         map2.put("Something Else", counter2+1);
         }else{
         map2.put(gen, counter2+1); 
        }
        }
      for(Map.Entry m: map2.entrySet()){
      System.out.println(m.getKey()+ " = "+m.getValue());
     }
   }
   
    
    
    
    
    
    
    
        @FXML
    private void handleOpenAction(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());
         if(selectedDirectory == null){
               System.out.println("No Directory selected");
                }else{
             currentD= selectedDirectory.getAbsolutePath();
             collection = MUSIC_SERVICE
                .createMusicMediaCollection(Paths.get(selectedDirectory.getAbsolutePath()));
        dataForTableView = FXCollections.observableArrayList(collection.getMusic());
        dataForTableView.addListener(makeChangeListener(collection));
        List<MusicMediaColumnInfo> myColumnInfoList = TableViewFactory.makeColumnInfoList();
        
        tableView.setItems(dataForTableView);
        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableViewFactory.makeTable(tableView, myColumnInfoList);
        tableView.setEditable(true);
        
                }
         }
    

    
    @FXML
    private void deleteFile(){
    String path;
    l2.add(tableView.getSelectionModel().getSelectedItem());
    path = l2.get(0).getPath();
    File file = new File(path);
    file.delete();
    l2.clear();
    dataForTableView.clear();
    collection = MUSIC_SERVICE
                .createMusicMediaCollection(Paths.get(currentD));
    dataForTableView = FXCollections.observableArrayList(collection.getMusic());
    dataForTableView.addListener(makeChangeListener(collection));
    List<MusicMediaColumnInfo> myColumnInfoList = TableViewFactory.makeColumnInfoList();
    tableView.setItems(dataForTableView);
    TableViewFactory.makeTable(tableView, myColumnInfoList);
    tableView.setEditable(true);
  //  dataForTableView.removeAll(dataForTableView);
   // FXCollections.copy(dataForTableView, collection.getMusic());
   // tableView.setItems(dataForTableView);
 // tableView.getItems().clear();
  //tableView.getItems().addAll(collection.getMusic());
    }
    
    @FXML
    private void close(){
        System.exit(0);
    }
        


    
    
    @FXML
    private void handleAboutAction(final ActionEvent event) {
    }

    @FXML
    private void handleKeyInput(final InputEvent event) {
        if (event instanceof KeyEvent) {
            final KeyEvent keyEvent = (KeyEvent) event;
            if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.A) {
                System.out.println("sfsds");
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    private static ListChangeListener<MusicMedia> makeChangeListener(final MusicMediaCollection collection) {
        return new ListChangeListener<MusicMedia>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends MusicMedia> change) {
                while (change.next()) {
                    if (change.wasAdded()) {
                        for (MusicMedia addedChild : change.getAddedSubList()) {
                            collection.addMusicMedia(addedChild);
                        }
                    }
                    if (change.wasRemoved()) {
                        for (MusicMedia removedChild : change.getRemoved()) {
                            collection.removeMusicMedia(removedChild);
                        }
                    }
                }
            }
        };
    }

}

        /*    for(int i=0; i<=playlist.size()-1; i++){
     System.out.println(playlist.get(i).getPath());       
     File source = new File(playlist.get(i).getPath());
     File dst = new File(desti.getText()+"\\" + playlist.get(i).getTitle() +".mp3");
     Files.copy(source, dst);
    */
    
    /*
        @FXML
    private void handleButtonAction(ActionEvent event) {
        final MusicMediaCollection collection = MUSIC_SERVICE
                .createMusicMediaCollection(Paths.get(pathScannedOnLoad));
        dataForTableView = FXCollections.observableArrayList(collection.getMusic());
        dataForTableView.addListener(makeChangeListener(collection));
        List<MusicMediaColumnInfo> myColumnInfoList = TableViewFactory.makeColumnInfoList();

        tableView.setItems(dataForTableView);
        TableViewFactory.makeTable(tableView, myColumnInfoList);
        tableView.setEditable(true);
       }
  */


   /* @FXML
    private void make(ActionEvent event) throws IOException{
        final MusicMediaCollection collection = MUSIC_SERVICE.createMusicMediaCollection(Paths.get(pathScannedOnLoad));
        List<MusicMedia> list = collection.getMusic();
        list.add(collection.getMusic().get(1));
        list.add(collection.getMusic().get(2));
        
        System.out.println(list.get(0).getPath());
        System.out.println(list.get(1).getPath());
        File source = new File(list.get(1).getPath());
        File dst = new File("C:\\Users\\Madan\\Downloads\\ByJanuary\\PIII-master\\PIII-master\\test-music-files\\collection-B\\unknown\\"+list.get(1).getTitle()+".mp3");
        Files.copy(source, dst);
    }
    */








          /*    System.out.println(model.getFirstCollection().getMusic().get(i).getArtist());
                firstGenre = model.getFirstCollection().getMusic().get(i).getGenre();
                secondGenre = model.getSecondCollection().getMusic().get(j).getGenre();
                System.out.println(firstGenre);
                System.out.println(secondGenre);
                
          
                if (secondGenre == null ) {
                    secondGenre="";
                    if(firstGenre == null){
                        firstGenre = "";
                        System.out.println(firstGenre + secondGenre + " Both Null");
                    }else{
                        System.out.println(secondGenre + firstGenre + " Only Second Null");
                    }
                    
                          } else{
                    if(firstGenre == null){
                      firstGenre ="";
                      System.out.println(firstGenre + secondGenre + "first null");
                    }
                System.out.println(firstGenre + secondGenre + "Both not NULL");
                }
                */
              /*  if(firstTitle == null){
                    firstTitle = "firstNull";
                     if(secondTitle == null){
                        secondTitle = "secondNull";
                        System.out.println("Both NULLLL");
                      }else{
                           System.out.println("First null , second not null");
                      }
                }
                if(secondTitle == null){
                    secondTitle = "";
                    System.out.println("Second null, first not null");
                }
                */
                /*
                if(model.getSecondCollection().getMusic().get(j).getGenre().equals(null)){
                    secondGenre = "nully";
                    System.out.println(secondGenre);
                }else{
                    secondGenre = model.getSecondCollection().getMusic().get(j).getGenre();
                    System.out.println(secondGenre);
                }
             System.out.println("Onepiece : " + model.getSecondCollection().getMusic().get(j).getGenre().equals(null));
                */
              /*  if(model.getSecondCollection().getMusic().get(j).getGenre().isEmpty()==true){
                    secondGenre = "";
               System.out.println(model.getFirstCollection().getMusic().get(i).getGenre().toString());
               System.out.println(secondGenre);
             //System.out.println(model.getSecondCollection().getMusic().get(j).getGenre().toString());
                }else
                {
               System.out.println(model.getFirstCollection().getMusic().get(i).getGenre().toString());
               System.out.println(model.getSecondCollection().getMusic().get(j).getGenre().toString());
                }
                */
            /*
              if(model.getSecondCollection().getMusic().get(j).getGenre().)
             {
                 System.out.println("Empty");
             }else{
                 System.out.println("Not Empty");
             }
              */
          //       System.out.println(model.getFirstCollection().getMusic().get(2).getTitle().toString());
      //   System.out.println(model.getSecondCollection().getMusic().get(2).getTitle().toString());
       
           //System.out.println(model.getFirstCollection().getMusic().get(i).getGenre().toString());
             //System.out.println(model.getSecondCollection().getMusic().get(j).getGenre().toString());
         
             /*   if(model.getFirstCollection().getMusic().get(i).getGenre().equals(null)){
                firstGenre = "";   
                if (model.getSecondCollection().getMusic().get(j).getGenre().equals(null)){
                    secondGenre = "";
                }else{
                          secondGenre= model.getSecondCollection().getMusic().get(j).toString();
                }
                
            } else if (model.getSecondCollection().getMusic().get(j).getGenre().equals(null)){
                secondGenre = "";
                    firstGenre = model.getFirstCollection().getMusic().get(i).toString();
                    
            } else {
                 firstGenre = model.getFirstCollection().getMusic().get(i).toString();
                secondGenre= model.getSecondCollection().getMusic().get(j).toString();
            }
            
            if(firstGenre.equalsIgnoreCase(second)){
            System.out.println(first);
            System.out.println(second);
            }
            
            */
                    
              //  if(model.getFirstCollection().getMusic().get(i).getGenre().equalsIgnoreCase(model.getSecondCollection().getMusic().get(j).getGenre())){
              //   System.out.println(model.getFirstCollection().getMusic().get(i).getGenre().toString());
                // System.out.println(model.getSecondCollection().getMusic().get(j).getGenre().toString());
            
              //  System.out.println(model.getFirstCollection().getMusic().get(i).getTitle().toString());
                //   System.out.println(model.getSecondCollection().getMusic().get(j).getTitle().toString());
              
              //  }else{
                    
               // }

                