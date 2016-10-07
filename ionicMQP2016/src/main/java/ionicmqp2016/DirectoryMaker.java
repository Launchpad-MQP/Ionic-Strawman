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

    //make sure relevant directories exist
    makeDirectory("");  //BASEPATH directory is created
    makeDirectory("www");

    //clearing /js and /templates because there's only user-created code
    removeDirectory("www/js");
    makeDirectory("www/js");
    removeDirectory("www/templates");
    makeDirectory("www/templates");

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
      buffer += line + '\n';
    }
    //create final file
    if(path != null) {
      System.out.println("Creating file " + path);
      createFile(buffer, path);
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
