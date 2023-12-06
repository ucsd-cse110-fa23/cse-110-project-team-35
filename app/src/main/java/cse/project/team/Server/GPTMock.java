package cse.project.team.Server;

import java.io.*;
import java.net.URISyntaxException;

public class GPTMock implements GPTI {
    public String chatgen(String audio_generatedText) throws URISyntaxException, IOException, InterruptedException {
        return "Mashed potat\n Take potatoe. Mash. Done. :)";
    }
}
