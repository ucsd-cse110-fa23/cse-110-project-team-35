package cse.project.team.server;
import java.io.*;
import java.net.URISyntaxException;

public class genMock implements genI {
    public String generate() throws IOException, URISyntaxException, Exception {
        String response = "Mashed potats?\n Take potatoe. Mash. Done. :)";
        Thread.sleep(5000);
        return response;
    }

    @Override
    public String audioGen(InputStream in) throws IOException, URISyntaxException, Exception {
        return "dinner potato";
    }

    @Override
    public String chatgen(String audio_generatedText) throws URISyntaxException, IOException, InterruptedException {
        String response = "Mashed potats?\n Take potatoe. Mash. Done. :)";
        Thread.sleep(5000);
        return response;
    }
}
