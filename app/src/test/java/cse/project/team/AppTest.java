package cse.project.team;

import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.HttpServer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class AppTest {
    String m_title = "Mashed potat";
    String m_details = "Get potatoes. Mash. Done.";
    String p_title = "Pancakes";
    String p_details = "Get cake. Get pan. Put cake in pan. Done.";
    
    // the server should be already running

    @Test
    public void testHandleGet() throws IOException {

        String expectedResponse = "";
        Model model = new Model();
        // Perform the GET request
        //String responseDelete = model.performRequest("DELETE", null, null, "Mashed potat");
        String response = model.performRequest("GET",m_title,m_details,null);

        assertEquals(expectedResponse, response);
    }

     @Test
    public void testHandlePUTandGET() throws IOException {

        String expectedResponse_title = "Mashed potat";
        String expectedResponse_detail = m_details;
        String putdone = "Did Something?";
        Model model = new Model();
        String response_put = model.performRequest("PUT",m_title,m_details,null);
        String response = model.performRequest("GET",m_title,m_details,null);

         assertEquals(response_put, putdone);
         assertEquals(response, expectedResponse_title);

    }

    @Test
    public void testHandlDELETE() throws Exception{
        String expectedResponse_title = "Mashed potat";
        String expectedResponse_detail = m_details;
        Model model = new Model();
        String response_post = model.performRequest("PUT",m_title,m_details,null);
        String response = model.performRequest("GET",m_title,m_details,null);
        String response_detail = model.performRequest("GET", null, null, response);
        String responseDelete = model.performRequest("DELETE", null, null, response);
        assertEquals(expectedResponse_title, response);
        assertEquals(expectedResponse_detail, response_detail);
    }



    /* User Story 1 */
    /* 
    @Test
    void viewRecipeListEmpty() {
        Model classUnderTest = new Model();
        List<String> recipeList = new ArrayList<>();
        assertEquals(classUnderTest.getRecipeList(), recipeList);
    }
    */

    /* 
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