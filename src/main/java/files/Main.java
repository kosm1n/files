package files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

//         File file = new File("G:/DCIM/Camera/Photo taken on 2011-08-23 19-08-08.jpg");
//         setCreationTimeFromFileName(file);

        File folder = new File("G:/DCIM/Camera/");
        List<File> listOfFiles = Arrays.asList(folder.listFiles());

        listOfFiles.forEach(Main::setCreationTimeFromFileName);

    }

    private static void setCreationTimeFromFileName(File file) {

        try {

            // Get File Attributes
            BasicFileAttributeView basicFileAttributeView = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);

            // Get Date from File Name
            Date date = RawDate.getDateFromFileName(file.getName());

            // Convert from Date to FileTime
            FileTime time = FileTime.fromMillis(date.getTime());

            // Set Modified, Acessed & Created Times
            basicFileAttributeView.setTimes(time, time, time);

            log.info("File: " + file.getName() + " new creation date: '{}'", basicFileAttributeView.readAttributes().creationTime());

        } catch (ParseException e) {
            log.error("Error parsing file name: " + file.getName(), e);
        } catch (IOException e) {
            log.error("Error setting file dates: " + file.getName(), e);
        } catch (Exception e) {
            log.error("Unknown error on file: " + file.getName(), e);
        }

    }

}
