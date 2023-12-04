package cse.project.team;
import java.util.Random;

public class ColorPicker {
    public String tag(String mealType) {
        if (mealType.equals("Breakfast")) {
            return "#58B56B";
        } else if (mealType.equals("Lunch")) {
            return "#FF99C8";
        } else {
            return "#009FFD";
        }
    }

    public String highlight() {
        String[] colors = { "#F26B86", "#FFDFB6", "#05AEEF", "#0BBDA9", "#C1B7EE", "#89AFE8", "#EB6E52" };
        int randomNumber = new Random().nextInt(7);
        return colors[randomNumber];
    }
}
