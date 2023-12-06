package cse.project.team.Server;

import java.io.*;
import java.net.URISyntaxException;

public interface WhisperI {
    public String audioGen(InputStream in) throws IOException, URISyntaxException,Exception;
}
