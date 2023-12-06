package cse.project.team.Server;

import java.io.*;
import java.net.*;

public interface GPTI {
    public String chatgen(String audio_generatedText) throws URISyntaxException, IOException, InterruptedException;
}
