package cse.project.team.Controller.Components;

import cse.project.team.views.Components.RecipeList;
import cse.project.team.views.Components.RecipeTitle;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SortButtonsAZ implements SortingStrategy {
    public void sort(RecipeList recipeList) {
        // Extract buttons and sort them
        List<RecipeTitle> sortedRecipes = recipeList.getChildren().stream()
            .filter(node -> node instanceof RecipeTitle)
            .map(node -> (RecipeTitle) node)
            .sorted(Comparator.comparing(RecipeTitle::getTitle, String.CASE_INSENSITIVE_ORDER))
            .collect(Collectors.toList());

        // Clear the list and re-add sorted buttons
        recipeList.getChildren().clear();
        recipeList.getChildren().addAll(sortedRecipes);
    }
}