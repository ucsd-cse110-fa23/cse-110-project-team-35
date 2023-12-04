package cse.project.team;

public class ColorPicker {
    public String selectColor(String mealType) {
        if (mealType.equals("Breakfast")) {
            return "blue";
        }
        else if (mealType.equals("Lunch")) {
            return "yellow";
        }
        else {
            return "red";
        }
    }
}
