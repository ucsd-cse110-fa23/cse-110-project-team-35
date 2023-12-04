package cse.project.team;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;
import javafx.scene.control.*;

interface SortingStrategy {
    public void sort(RecipeList recipeList);
}

class SortButtonsAZ implements SortingStrategy {
    public void sort(RecipeList recipeList) {
        // Extract buttons and sort them
        List<Button> sortedButtons = recipeList.getChildren().stream()
            .filter(node -> node instanceof Button)
            .map(node -> (Button) node)
            .sorted(Comparator.comparing(Button::getText, String.CASE_INSENSITIVE_ORDER))
            .collect(Collectors.toList());

        // Clear the list and re-add sorted buttons
        recipeList.getChildren().clear();
        recipeList.getChildren().addAll(sortedButtons);
    }
}

class SortButtonsZA implements SortingStrategy {
    public void sort(RecipeList recipeList) {
        // Extract buttons and sort them in descending order
        List<Button> sortedButtons = recipeList.getChildren().stream()
                .filter(node -> node instanceof Button)
                .map(node -> (Button) node)
                .sorted(Comparator.comparing(Button::getText, String.CASE_INSENSITIVE_ORDER).reversed())
                .collect(Collectors.toList());
    
        // Clear the current children and add the sorted buttons back
        recipeList.getChildren().clear();
        recipeList.getChildren().addAll(sortedButtons);
    }
}

class SortButtonsEL implements SortingStrategy {
    public void sort(RecipeList recipeList) {
        List<Button> buttons = recipeList.getChildren().stream()
        .filter(node -> node instanceof Button)
        .map(node -> (Button) node)
        .collect(Collectors.toList());

        Collections.reverse(buttons);
        // Clear the list and re-add buttons in their original order
        recipeList.getChildren().clear();
        recipeList.getChildren().addAll(buttons);
    }
}