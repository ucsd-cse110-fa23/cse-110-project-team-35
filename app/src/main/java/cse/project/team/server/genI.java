package cse.project.team.server;
import java.io.*;
import java.net.URISyntaxException;

public interface genI {
    public String audioGen(InputStream in) throws IOException, URISyntaxException,Exception;
    public String chatgen(String audio_generatedText) throws URISyntaxException, IOException, InterruptedException;
}