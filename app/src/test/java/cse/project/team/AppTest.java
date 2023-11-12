package cse.project.team;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class AppTest {
    String m_title = "Mashed potat";
    String m_details = "Get potatoes. Mash. Done.";
    String p_title = "Pancakes";
    String p_details = "Get cake. Get pan. Put cake in pan. Done.";

    
    /* User Story 1 */
    /*
    @Test
    void viewRecipeListEmpty() {
        Model classUnderTest = new Model();
        List<String> recipeList = new ArrayList<>();
        assertEquals(classUnderTest.getRecipeList(), recipeList);
    }

    @Test
    void viewRecipeListSorted() {
        Model classUnderTest = new Model();
        classUnderTest.addData(m_title, m_details);
        classUnderTest.addData(p_title, p_details);

        List<String> recipeList = new ArrayList<>();
        recipeList.add(m_title);
        recipeList.add(p_title);
        assertEquals(classUnderTest.getRecipeList(), recipeList);
    }

    @Test
    void viewDetailsNotEmpty() {
        Model classUnderTest = new Model();
        classUnderTest.addData(m_title, m_details);
        assertEquals(classUnderTest.getDetails(m_title), m_details);
    }

    @Test
    void viewDetailsInvalidTitle() {
        Model classUnderTest = new Model();
        assertEquals(classUnderTest.getDetails(m_title), null);
    }

    @Test
    void viewDetailsEmpty() {
        Model classUnderTest = new Model();
        classUnderTest.addData(m_title, "");
        assertEquals(classUnderTest.getDetails(m_title), "");
    }

    @Test
    void saveRecipe() {
        Model classUnderTest = new Model();
        classUnderTest.putData(m_title, m_details);
        classUnderTest.putData(m_title, p_details);
        assertEquals(classUnderTest.getDetails(m_title), p_details);
    }

    @Test
     void deleteRecipeInvalid() {
        Model classUnderTest = new Model();
        classUnderTest.addData(m_title, m_details);
        classUnderTest.deleteData(p_title);

        List<String> recipeList = new ArrayList<>();
        recipeList.add(m_title);
        assertEquals(classUnderTest.getRecipeList(), recipeList);
     }

     @Test
     void deleteRecipe() {
        Model classUnderTest = new Model();
        classUnderTest.addData(p_title, p_details);
        classUnderTest.addData(m_title, m_details);
        
        List<String> recipeList = new ArrayList<>();
        recipeList.add(p_title);

        classUnderTest.deleteData(m_title);
        assertEquals(classUnderTest.getRecipeList(), recipeList);
     }
     */
}