package cse.project.team.Controller.Components;

import java.util.List;
import java.util.stream.Collectors;

import cse.project.team.Views.Components.RecipeList;
import cse.project.team.Views.Components.RecipeTitle;

public class Filter {
    public static void filterSelection(String selection, RecipeList recipeList) {
        if (selection != "All") {
            List<RecipeTitle> filterRecipes = recipeList.getChildren().stream()
                    .filter(node -> node instanceof RecipeTitle)
                    .map(node -> (RecipeTitle) node)
                    .filter(RecipeTitle -> RecipeTitle.getMealType().equalsIgnoreCase(selection))
                    .collect(Collectors.toList());

            recipeList.getChildren().clear();
            recipeList.getChildren().addAll(filterRecipes);
        }
    }
}
