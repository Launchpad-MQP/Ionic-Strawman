package ionicmqp2016;
import java.util.Iterator;
import java.io.File;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class DirectoryMaker {

  final static String BASEPATH = "../TestDirectory/";

  //called by scala program to parse results (should only be one result, ideally)
  public static void parseResults(Iterator<String> it) {
    String file = it.next();
    String lines[] = file.split("\\r?\\n");

    String newfile = "";
    String newpath = "";
    boolean isPathLine = false;

    //refresh all relevant directories
    refreshDirectory("");
    refreshDirectory("www");
    refreshDirectory("www/js");
    refreshDirectory("www/templates");

    //parse
    for(String l : lines) {

      //check line for new file header
      if(isPathLine) {
        //set newpath to path pulled from header
        String paths[] = l.split(" ");
        if(paths.length>0)
          newpath = paths[paths.length-1];
        System.out.println("New Path Found: " + newpath);
        isPathLine = false;
      }

      //if valid header
      if(l.equals("/**") || l.equals("<--")) {
        //createFile() with old file contents, if non-empty
        if(!newfile.isEmpty() && !newpath.isEmpty()) {
          System.out.println("Creating file " + newpath);
          createFile(newfile, newpath);
        }
        //clear newfile, newpath
        newfile = "";
        newpath = "";
        isPathLine = true;
      }
      //add line to newfile
      newfile += l;
    }
    //create final file
    if(!newfile.isEmpty() && !newpath.isEmpty()) {
      System.out.println("Creating file " + newpath);
      createFile(newfile, newpath);
    }
  }

  //clears the given directory/file and creates it again
  public static void refreshDirectory(String filepath) {
    File f = new File(BASEPATH + filepath);
    String[] files = f.list();
    if(files != null) {
      System.out.println("Flushing directory " + filepath);
      for(String s: files){
        File currentFile = new File(f.getPath(),s);
        currentFile.delete();
      }
    } else {
      if(f.mkdir()) {
        System.out.println("Made directory " + filepath);
      } else {
        System.out.println("Couldn't make directory.");
      }
    }
  }

  //creates a file with the given contents at the given path
  public static void createFile(String filecontents, String pathname) {
    try (Writer writer = new BufferedWriter(new OutputStreamWriter
    (new FileOutputStream(BASEPATH + pathname), "utf-8"))) {
      writer.write(filecontents);
    } catch (Exception e) {
      System.out.println(e);
      System.out.println("Couldn't write to file.");
    }
  }
}
