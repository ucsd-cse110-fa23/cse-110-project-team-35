package cse.project.team.Controller.Components;

import java.util.List;
import java.util.stream.Collectors;

import cse.project.team.Views.Components.RecipeList;
import cse.project.team.Views.Components.RecipeTitle;

import java.util.Collections;

public class SortButtonsOF implements SortingStrategy {
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
