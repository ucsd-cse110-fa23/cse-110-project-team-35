_CSE 110 Project: Team 35_

# Our Burndown Chart:
![Burndown chart](burndown.png)

# Instructions for Running App
1. Clone the Repo
2. Download JavaFX

**VSCODE INSTRUCTIONS**


4. Create a launch.json file in the .vscode folder
5. Add run arguments
   
        {
            "type": "java",
            "name": "App",
            "request": "launch",
            "mainClass": "cse.project.team.App",
            "projectName": "app",
            "vmArgs": "--module-path '<PATH TO YOUR JAVAFX LIB DIR' --add-modules javafx.controls,javafx.fxml"
        }
   
6. Run Server.java to start up the server which will process http requests
7. Run App.java to launch the GUI and access the app's main functionality

Sources used: <br>
  The getSource() Method <br>
    Link:           https://chortle.ccsu.edu/java5/notes/chap64/ch64_10.html<br>
    Date captured:  10/2023<br>
    Use:            For information on using an EventHandler's getSource.<br>
    
  Model-View-Controller Framework <br>
    Link:           https://www.pragmaticcoding.ca/beginners/part5 <br>
    Date captured:  10/2023 <br>
    Use:            For review of MVC design.<br>

  How to Switch Scenes | JavaFX GUI Tutorial for Beginners (video)<br>
    Link:           https://www.youtube.com/watch?v=SB9AnciLmsw<br>
    Date captured:  10/2023<br>
    Use:            For information on switching scenes in JavaFX.<br>

  CSS Tutorial<br>
    Link:           https://www.w3schools.com/css/<br>
    Date captured:  10-11/2023<br>
    Use:            For learning about CSS.<br>

  Getting Started with JavaFX<br>
    Link:           https://openjfx.io/openjfx-docs/#gradle<br>
    Date captured:  11/2023<br>
    Use:            For understanding how to use JavaFX and Gradle together.<br>
    
  Spring: class file has wrong version 61.0, should be 55.0<br>
    Link:           https://stackoverflow.com/questions/74648576/spring-class-file-has-wrong-version-61-0-should-be-55-0<br>
    Date captured:  11/2023<br>
    Use:            For tips on debugging an issue we faced with a Gradle/Java version mismatch.<br>

  Transfer Audio File from Client to HTTP Server via URLConnection<br>
    Link:           https://stackoverflow.com/questions/37869483/transfer-audio-file-from-client-to-http-server-via-urlconnection<br>
    Date captured:  11/2023<br>
    Use:            Heavily referenced code written in top answer (by anon, posted on Jun 17, 2016) for handling Whisper 
                    requests through server.<br>

  Round Corners in JavaFX Pane<br>
    Link:           https://stackoverflow.com/questions/45258138/round-corners-in-java-fx-pane<br>
    Date captured:  10/2023<br>
    Use:            For information on working with background-radius properties in CSS.<br>

  Adding CSS File to Stylesheets in JavaFX<br>
    Link:           https://stackoverflow.com/questions/13946372/adding-css-file-to-stylesheets-in-javafx<br>
    Date captured:  11/2023<br>
    Use:            To learn how to add .css file to JavaFX objects. Referenced code written by user 'nikolaos'
                    on Apr 27, 2014 and later edited by user 'Rahil Wazir'.<br>
  
  Can I Have an Onclick Effect in CSS?<br>
    Link:           https://stackoverflow.com/questions/13630229/can-i-have-an-onclick-effect-in-css<br>
    Date captured:  11/2023<br>
    Use:            For information on handling style changes based on UI events (such as button clicks) in CSS.<br>

  How to get rid of focus highlighting in JavaFX<br>
    Link:           https://www.jensd.de/wordpress/?p=1245<br>
    Date captured:  11/2023<br>
    Use:            For information on hiding the blue glow that appears around editable objects (Buttons, TextAreas)
                    in JavaFX.<br>
  
  How to set AUTO-SCROLLING of JTextArea in Java GUI?<br>
    Link:           https://stackoverflow.com/questions/1627028/how-to-set-auto-scrolling-of-jtextarea-in-java-gui<br>
    Date captured:  11/2023<br>
    Use:            For information on using the .append() method to automatically scroll as text is added
                    to a TextArea.<br>

  JavaFX TextArea Hiding Scroll Bars<br>
    Link:           https://stackoverflow.com/questions/14206692/javafx-textarea-hiding-scroll-bars<br>
    Date captured:  11/2023<br>
    Use:            To learn how to hide scrollbars from view. Referenced CSS code from the second top answer written by user
                    'Michael Sims' on May 13, 2020.<br>

  How to remove this gray top border in JavaFX TextArea<br>
    Link:           https://stackoverflow.com/questions/26651198/how-to-remove-this-gray-top-border-in-javafx-textarea<br>
    Date captured:  11/2023<br>
    Use:            For information on ways to override the automatic styling of TextAreas in JavaFX. Heavily referenced code
                    from answer written by user 'José Pereda' on Oct 30, 2014. <br>

  Typing Animation on a Text with JavaFX<br>
    Link:           https://stackoverflow.com/questions/33646317/typing-animation-on-a-text-with-javafx<br>
    Date captured:  11/2023<br>
    Use:            For information on using the Timeline and Keyframe classes to create a "typed out" animation in JavaFX.<br>
