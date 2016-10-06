package ionicmqp2016;
import java.util.Iterator;
import java.io.File;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.BufferedReader;
import java.io.IOException;

public class DirectoryMaker {

  final static String BASEPATH = "../TestDirectory/";

  //called by scala program to parse results (should only be one result, ideally)
  public static void parseResults(Iterator<String> iter) throws IOException {
    BufferedReader br = new BufferedReader(new StringReader(iter.next()));
    String buffer = "";
    String path = null;

    //refresh all relevant directories
    refreshDirectory("");
    refreshDirectory("www");
    refreshDirectory("www/js");
    refreshDirectory("www/templates");

    //parse
    String line;
    while ((line = br.readLine()) != null) {
      // Found the start of a new file (as noted by a block comment). Flush the
      // output of the buffer to the old file,
      if("/**".equals(line) || "<--".equals(line)) {
        if (path != null) {
          System.out.println("Creating file " + path);
          createFile(buffer, path);
          buffer = "";
        }
        // Find the path, located on the next line
        path = br.readLine().split("Path: ")[1];
      }
      //add line to buffer
      buffer += line;
    }
    //create final file
    if(path != null) {
      System.out.println("Creating file " + path);
      createFile(buffer, path);
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
