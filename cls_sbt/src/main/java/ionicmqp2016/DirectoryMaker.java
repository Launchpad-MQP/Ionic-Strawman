package ionicmqp2016;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;
import ionicmqp2016.Tuple;

public class DirectoryMaker {

  final static String BASEPATH = "../";

  /**
   * Iterate through the results from scala as (file, fileType) where fileType
   * is a path to a valid directory. Also cleans the workspace before writing.
   *
   * @param iter The iterator returned from Scala.
   * @throws IOException Never.
  **/
  public static void parseResults(Iterator<Tuple> iter) throws IOException {
    //make sure relevant directories exist
    makeDirectory("");  //BASEPATH directory is created
    makeDirectory("www");

    //clearing /js and /templates because there's only user-created code
    removeDirectory("www/js");
    makeDirectory("www/js");
    removeDirectory("www/templates");
    makeDirectory("www/templates");
    //removeDirectory("www/css");
    //makeDirectory("www/css");

    //parse
    while (iter.hasNext()) {
      Tuple boundFile = iter.next();
      boundFile.createFile();
    }
  }

  /**
   * Iterate through the results from scala as (file, fileType) where fileType
   * is a path to a valid directory. This function is for printing only.
   *
   * @param iter The iterator returned from Scala.
  **/
  public static void printResults(Iterator iter) {
    while (iter.hasNext()) {
      System.out.println("Found fragment:\n\t"+iter.next());
    }
  }

  /**
   * Makes the directory relative to BASEPATH. This function does not throw
   * errors, if directory already exists it will exit without doing anything.
   *
   * @param filepath The path to create, relative to BASEPATH
  **/
  public static void makeDirectory(String filepath) {
    File dir = new File(BASEPATH + filepath);
    if(dir.mkdir()) {
      System.out.println("Made directory " + filepath);
    } else {
      System.out.println("Couldn't make directory, or already exists.");
    }
  }

  /**
   * Removes the directory relative to BASEPATH. To do so, it must iterate
   * through all files in the directory and remove them first. If the directory
   * doesn't exist, it will return.
   *
   * @param filepath The path to remove, relative to BASEPATH
  **/
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
