package sn.ept.git.seminaire.poc.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MyFileReader {

    public List<String> read(String path) throws IOException  {
        return Files.readAllLines(Paths.get(path));
    }

}