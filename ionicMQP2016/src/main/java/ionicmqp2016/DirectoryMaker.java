package ionicmqp2016;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;
import ionicmqp2016.Tuple;

public class DirectoryMaker {

  final static String BASEPATH = "../";

  //called by scala program to parse results (should only be one result, ideally)
  public static void parseResults(Iterator<Tuple> iter) throws IOException {
    //make sure relevant directories exist
    makeDirectory("");  //BASEPATH directory is created
    makeDirectory("www");

    //clearing /js and /templates because there's only user-created code
    removeDirectory("www/js");
    makeDirectory("www/js");
    removeDirectory("www/templates");
    makeDirectory("www/templates");

    //parse
    while (iter.hasNext()) {
      Tuple boundFile = iter.next();
      boundFile.createFile();
    }
  }

  //makes the given directory
  public static void makeDirectory(String filepath) {
    File dir = new File(BASEPATH + filepath);
    if(dir.mkdir()) {
      System.out.println("Made directory " + filepath);
    } else {
      System.out.println("Couldn't make directory, or already exists.");
    }
  }

  //removes the given directory
  public static void removeDirectory(String filepath) {
    File dir = new File(BASEPATH + filepath);
    String[] files = dir.list();
    if(files != null) {
      System.out.println("Flushing directory " + filepath);
      for(String file: files){
        File currentFile = new File(dir.getPath(), file);
        currentFile.delete();
      }
      dir.delete();
    }
  }
}
