package cse.project.team.Views.Components;

import javafx.scene.layout.VBox;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeList extends VBox {
    public RecipeList() {
        this.getStyleClass().add("listCenter");
    }

    public List<String> getTitles() {
        return this.getChildren().stream()
                .filter(node -> node instanceof RecipeTitle)
                .map(node -> ((RecipeTitle) node).getTitle())
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    public List<String> getTypes() {
        return this.getChildren().stream()
                .filter(node -> node instanceof RecipeTitle)
                .map(node -> ((RecipeTitle) node).getMealType())
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    public void addRecipe(int index, String name, String mealtype) {
        RecipeTitle recipe = new RecipeTitle(name, mealtype);
        this.getChildren().add(index, recipe);
    }
}