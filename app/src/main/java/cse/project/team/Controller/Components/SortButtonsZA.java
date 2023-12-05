package cse.project.team.Controller.Components;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import cse.project.team.views.Components.RecipeList;
import cse.project.team.views.Components.RecipeTitle;

public class SortButtonsZA implements SortingStrategy {
    public void sort(RecipeList recipeList) {
        // Extract buttons and sort them in descending order
        List<RecipeTitle> sortedRecipes = recipeList.getChildren().stream()
                .filter(node -> node instanceof RecipeTitle)
                .map(node -> (RecipeTitle) node)
                .sorted(Comparator.comparing(RecipeTitle::getTitle, String.CASE_INSENSITIVE_ORDER).reversed())
                .collect(Collectors.toList());
    
        // Clear the current children and add the sorted buttons back
        recipeList.getChildren().clear();
        recipeList.getChildren().addAll(sortedRecipes);
    }
}