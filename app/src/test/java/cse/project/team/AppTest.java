package cse.project.team;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
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