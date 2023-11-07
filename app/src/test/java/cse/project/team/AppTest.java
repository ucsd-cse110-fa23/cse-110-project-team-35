package cse.project.team;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.View;

class AppTest {
    /* User Story 1 */
    @Test
    void viewRecipeListEmpty() {
        Model classUnderTest = new Model();
        List<String> recipeList = new ArrayList<>();
        assertEquals(classUnderTest.getRecipeList(), recipeList);
    }

    @Test
    void viewRecipeListSorted() {
        Model classUnderTest = new Model();
        classUnderTest.addData("Mashed potat", "Get potatoes. Mash. Done.");
        classUnderTest.addData("Pancakes", "Get cake. Get pan. Put cake in pan. Done.");
        
        List<String> recipeList = new ArrayList<>();
        recipeList.add("Mashed potat");
        recipeList.add("Pancakes");
        assertEquals(classUnderTest.getRecipeList(), recipeList);
    }

    /* User Story 2 */
    @Test
    void viewDetailsNotEmpty() {
        Model classUnderTest = new Model();
        String details = "Create a crust. Oven it. Done.";
        String title = "Pizza";
        classUnderTest.addData(title, details);
        assertEquals(classUnderTest.getDetails(title), details);
    }

    @Test
    void viewDetailsInvalidTitle() {
        Model classUnderTest = new Model();
        String title = "Tejbegriz";
        assertEquals(classUnderTest.getDetails(title), null);
    }

    @Test
    void viewDetailsEmpty() {
        Model classUnderTest = new Model();
        String title = "Tejbegriz";
        classUnderTest.addData(title, "");
        assertEquals(classUnderTest.getDetails(title), "");
    }

    /* User Story 4 */
    @Test
    void saveRecipe() {
        Model classUnderTest = new Model();
        String details = "Create a crust. Oven it. Done.";
        String newDetails = "Just oven it.";
        String title = "Pizza";
        classUnderTest.addData(title, details);
        classUnderTest.addData(title, newDetails);
        assertEquals(classUnderTest.getDetails(title), newDetails);
    }

}