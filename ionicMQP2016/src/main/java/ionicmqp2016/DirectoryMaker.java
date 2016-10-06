package ionicmqp2016;
import java.util.Iterator;
import java.io.File;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class DirectoryMaker {

  //called by scala program to parse results (should only be one result, ideally)
  public static void parseResults(Iterator<String> it) {
    String file = it.next();
    String lines[] = file.split("\\r?\\n");

    String newfile = "";
    String newpath = "";

    //refresh all relevant directories
    refreshDirectory("www/TestDirectory");
    //refreshDirectory("www/js");
    //refreshDirectory("www/templates");
    //refreshDirectory("index.html");

    //parse
    for(String l : lines) {
      //check line for new file header
      //if valid header
        //createFile() with old file contents, if non-empty
        //clear newfile, newpath
        //set newpath to path pulled from header
      //add line to newfile
    }

    createFile("hi there", "www/TestDirectory/test.txt");
  }

  //clears the given directory/file and creates it again
  public static void refreshDirectory(String filepath) {
    File f = new File(".././" + filepath);
    String[] files = f.list();
    for(String s: files){
      File currentFile = new File(f.getPath(),s);
      currentFile.delete();
    }
    if(f.mkdir()) {
      System.out.println("Made directory " + filepath);
    }
  }

  //creates a file with the given contents at the given path
  public static void createFile(String filecontents, String pathname) {
    try (Writer writer = new BufferedWriter(new OutputStreamWriter
    (new FileOutputStream(".././" + pathname), "utf-8"))) {
      writer.write(filecontents);
    } catch (Exception e) {
      System.out.println("Couldn't write to file.");
    }
  }
}
