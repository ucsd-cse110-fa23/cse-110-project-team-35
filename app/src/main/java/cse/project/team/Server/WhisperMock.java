package cse.project.team.Server;

import java.io.*;
import java.net.URISyntaxException;

public class WhisperMock implements WhisperI {
    public String audioGen(InputStream in) throws IOException, URISyntaxException,Exception {
        return "dinner potato";
    }
}
