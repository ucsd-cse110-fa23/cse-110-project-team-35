package cse.project.team;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

interface SortingStrategy {
    public void sort(RecipeList recipeList);
}

class SortButtonsAZ implements SortingStrategy {
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

class SortButtonsZA implements SortingStrategy {
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

class SortButtonsOF implements SortingStrategy {
    public void sort(RecipeList recipeList) {
        List<RecipeTitle> recipes = recipeList.getChildren().stream()
        .filter(node -> node instanceof RecipeTitle)
        .map(node -> (RecipeTitle) node)
        .collect(Collectors.toList());

        Collections.reverse(recipes);
        // Clear the list and re-add recipes in their original order
        recipeList.getChildren().clear();
        recipeList.getChildren().addAll(recipes);
    }
}