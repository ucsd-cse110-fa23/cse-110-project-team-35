package cse.project.team;

public class DalleMock extends Dalle {
    /* Shows a trollface image for every recipe, saved with the correct filename */
    public String generateDalle(String input) {
        return "https://upload.wikimedia.org/wikipedia/en/thumb/9/9a/Trollface_non-free.png/220px-Trollface_non-free.png";
    }
}

