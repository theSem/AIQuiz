package com.example.sam.aiquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Integer> questionIDs = new ArrayList<>(); //Keeps record of all the questions in the quiz


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
            This is the part where you define your quiz question!
            The format is like this:
            createRadioQuestion(question, realAnswer, fakeAnswers...)
            createFreeResponse(question, answer)
            createCheckBox(question, asList(answer1, answer2...), asList(fakeAnswer1, fakeAnswer2...))
            you can have as many fake answers as you'd like!
         */

        createFreeResponse("What is the first name of the creator of the Turing Test?",
                "alan");

        createRadioQuestion("Which of the following games has AI not beaten the best human player?",
                "Nothing",
                "Chess", "Checkers", "Go");

        createCheckBox("Which of the following companies formed a non-profit partnership together focusing on AI?",
                asList("Amazon", "Google", "Facebook", "IBM", "Microsoft"), asList("Dell", "Apple", "Intel"));

        createRadioQuestion("Which of the following books features an AI named HAL-9000?",
                "2001: A Space Odyssey",
                "Do Androids Dream of Electric Sheep?", "Robot", "There Will Come Soft Rains");

        createFreeResponse("What is the name of the AI that beat the No. 1 ranked Go player of all time?",
                "AlphaGo");

        createCheckBox("Who in the following has expressed their concern for an AI future?",
                asList("Barack Obama", "Elon Musk", "Bill Gates"),
                asList("Mark Zuckerberg", "John Cena")
                );

        //Creating the submit button for the app
        Button submit = new Button(getApplicationContext());

        //Add some parameters to our LayoutParameters holder and set the text for the button
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.setMargins(16,16,16,16);
        submit.setText("Submit");

        //Finally, add the button to the main view
        LinearLayout listView = findViewById(R.id.questionLayout);
        listView.addView(submit,layoutParams);

        //Tell the app what to do when the button is clicked!
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Make a piece of toast that reveals the users score
                Toast toast = Toast.makeText(getApplicationContext(),checkAnswers() + "/" + questionIDs.size() + " correct.",Toast.LENGTH_LONG);
                toast.show();

                //Reset the quiz to play again
                clearCheckedAnswers();
            }
        });
    }

    /**
     * This method transforms components of a quiz question into the radiogroup format to display to
     * the user. To differentiate the real answer from the fakes, real answers have a tag '1' while
     * fake answers have a tag '0'. The radiogroup is constructed, a text view is inserted to act as
     * the question and the answers are randomly put into radio buttons.
     * @param question The question that gives hint to the correct answer
     * @param answer The correct answer to the question
     * @param fakeAnswers Incorrect answers to the question
     */
    void createRadioQuestion(String question, String answer, String... fakeAnswers){

        //Create the base radio group
        RadioGroup radioGroup = new RadioGroup(getApplicationContext());

        //Add the question text to group
        radioGroup.addView(makeQuestionView(question), RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);

        //Create a divider between the question and answers
        radioGroup.addView(makeDivider());

        //Create an ArrayList of answers to add to radiogroup
        ArrayList<RadioButton> answers = new ArrayList<>();
        RadioButton trueAnswer = new RadioButton(getApplicationContext());
        trueAnswer.setText(answer);
        trueAnswer.setTag(1); //Label the correct answer

        //Turn the fakeAnswer strings into RadioButtons
        for (String fakeAnswer: fakeAnswers) {
            RadioButton falseAnswer = new RadioButton(getApplicationContext());
            falseAnswer.setText(fakeAnswer);
            falseAnswer.setTag(0); //Label these as false
            answers.add(falseAnswer);
        }

        //Add the correct answer in with the rest
        answers.add(trueAnswer);

        //Randomize the answers
        Collections.shuffle(answers);


        //Style the RadioButtons and add them into the RadioGroup
        for (RadioButton radioButton : answers) {
            radioButton.setTextSize(16);
            radioButton.setPadding(8,0,8,0);
            radioButton.setTextColor(getResources().getColor(R.color.primary_text));
            radioGroup.addView(radioButton, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        }

        //Style RadioGroup
        radioGroup.setPadding(32,16,32,16);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(16,16,16,16);

        //Create a unique ID for each of the RadioGroups
        radioGroup.setId(View.generateViewId());

        //Add the RadioGroup into the main view
        LinearLayout listView = findViewById(R.id.questionLayout);
        listView.addView(radioGroup,layoutParams);
        questionIDs.add(radioGroup.getId()); //Keep a record of unique questions
    }

    /**
     * This method is used to create a free response question. The answer is saved as a tag for the
     * EditText so comparing real answer to user answer is easy later on. Instead of using a
     * radiogroup like the first method, this method uses a linear layout that acts as a container
     * for the question and edittext
     * @param question the question!!
     * @param answer the answer!! (i assume these are pretty self-explanatory)
     */
    void createFreeResponse(String question, String answer){
        //Prepare the ViewGroup
        LinearLayout freeResponseLayout = new LinearLayout(getApplicationContext());
        freeResponseLayout.setOrientation(LinearLayout.VERTICAL);
        freeResponseLayout.setPadding(32,16,32,16);

        //Define layoutparams for use later
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //Create question and add to view group
        freeResponseLayout.addView(makeQuestionView(question), layoutParams);

        //Create a divider between the question and answers
        freeResponseLayout.addView(makeDivider());

        //Create answer EditText
        EditText answerBox = new EditText(this);
        answerBox.setHint("Enter answer here");
        answerBox.setTag(answer);
        freeResponseLayout.addView(answerBox, layoutParams);

        //Give it a unique ID so we can find it later!
        answerBox.setId(View.generateViewId());

        //Now add it to the main view
        LinearLayout listView = findViewById(R.id.questionLayout);
        layoutParams.setMargins(16,16,16,16);
        listView.addView(freeResponseLayout, layoutParams);
        questionIDs.add(answerBox.getId());
    }

    /**
     * This method takes in a question, multiple answers and multiple fakeanswers. It then creates
     * a LinearLayout (which acts as a container) and then fills the layout with checkboxes using
     * the answers provided. Correct answers are marked with a tag 1 if they are correct while
     * incorrect answers have a tag of value 0
     * @param question a single string question
     * @param answers a List of true string answers
     * @param fakeAnswers a List of false string answers
     */
    void createCheckBox(String question, List<String> answers, List<String> fakeAnswers){
        LinearLayout checkBoxLayout = new LinearLayout(getApplicationContext());
        checkBoxLayout.setOrientation(LinearLayout.VERTICAL);
        checkBoxLayout.setPadding(32,16,32,16);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //Create question
        checkBoxLayout.addView(makeQuestionView(question), layoutParams);

        //Create a divider between the question and answers
        checkBoxLayout.addView(makeDivider());

        ArrayList<CheckBox> answerList = new ArrayList<>();

        //Add the real answers
        for (String answer: answers) {
            CheckBox checkBox = new CheckBox(getApplicationContext());
            checkBox.setText(answer);
            checkBox.setTag(1);
            answerList.add(checkBox);
        }

        //Add the fake answers
        for (String answer: fakeAnswers){
            CheckBox checkBox = new CheckBox(getApplicationContext());
            checkBox.setText(answer);
            checkBox.setTag(0);
            answerList.add(checkBox);
        }

        //Mix it up
        Collections.shuffle(answerList);

        //Style each check box and add it to the view group
        for (CheckBox checkBox : answerList) {
            checkBox.setTextSize(16);
            checkBox.setPadding(8,0,8,0);
            checkBox.setTextColor(getResources().getColor(R.color.primary_text));
            checkBoxLayout.addView(checkBox, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        }

        //Style RadioGroup
        checkBoxLayout.setPadding(32,16,32,16);
        layoutParams.setMargins(16,16,16,16);

        //Create a unique ID for each of the RadioGroups
        checkBoxLayout.setId(View.generateViewId());

        //Add the checkBoxLayout into the main view
        LinearLayout listView = findViewById(R.id.questionLayout);
        listView.addView(checkBoxLayout,layoutParams);
        questionIDs.add(checkBoxLayout.getId()); //Keep a record of unique questions
    }

    /**
     * Takes in the string of the question and creates a textview to insert in the main view.
     * Pretty simple, but useful because it works for all three different kinds of quiz questions.
     * @param question The prompt for the quiz question
     * @return Returns a textview usable for the question
     */
    TextView makeQuestionView(String question){
        TextView newQuestion = new TextView(getApplicationContext());
        newQuestion.setText(questionIDs.size() + 1 + ". " + question);
        newQuestion.setTextSize(24);
        newQuestion.setTextColor(getResources().getColor(R.color.primary_text));
        newQuestion.setPadding(8,0,8,0);
        return newQuestion;
    }

    /**
     * Just creates a new view to act as a divider, again, this is used for all three question types
     * @return the divider
     */
    View makeDivider(){
        View divider = new View(getApplicationContext());
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT,2);
        dividerParams.setMargins(16,32,16,32);
        divider.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        divider.setLayoutParams(dividerParams);
        return divider;
    }


    /**
     * Called when the user presses the submit button to check how many answers the user got right.
     * @return Returns number of correct answers
     */
    int checkAnswers(){
        int counter = 0; // Counts frequency of correct answers
        for (int id:questionIDs) {
            View view = findViewById(id);

            //This if statement determines which kind of view is selected
            if (view instanceof RadioGroup) {
                //RadioGroup has a nice method which returns the checked RadioButton ID so checking is easy
                RadioGroup radioGroup = (RadioGroup) view;

                try {
                    //Wow, just look at the next line for a minute and ponder life
                    if (Integer.parseInt(String.valueOf(findViewById(radioGroup.getCheckedRadioButtonId()).getTag())) == 1) counter++;
                } catch (NullPointerException ignored){}

            } else if (view instanceof EditText){
                //All we need to do is compare the user answer and true answer (in the tag of the EditText)
                EditText editText = (EditText) view;
                if (editText.getText().toString().equalsIgnoreCase(editText.getTag().toString())){
                    counter++;
                }

            } else if (view instanceof LinearLayout){//!!!!!!! If you add another kind of question that requires a LinearLayout wrapper, you need to make this more specific for each kind
                //Change the type of the view to ViewGroup so we can access the children
                ViewGroup viewGroup = (ViewGroup) view;
                //This will be useful to mark if all answers are right
                boolean isCorrect = true;
                for (int i = 0; i < viewGroup.getChildCount(); i++){
                    //Get the children of the view, make sure they are type CheckBox (because there is the question text and the divider)
                    if (viewGroup.getChildAt(i) instanceof CheckBox){
                        CheckBox checkBox = (CheckBox) viewGroup.getChildAt(i);
                        //Mark the question incorrect if a user has checked a false box or if a user did not check a true box
                        if ((checkBox.isChecked() && (checkBox.getTag().equals(0)) || (!checkBox.isChecked() && (checkBox.getTag().equals(1))))){
                            isCorrect = false;
                            break;
                        }
                    }
                }
                //If all the boxes are correctly checked, add one to the users score
                if (isCorrect) counter++;
            }
        }
        return counter;
    }

    /**
     * Clears the checked RadioButtons in each of the RadioGroups
     */
    void clearCheckedAnswers(){
        for (int id:questionIDs) {
            //Similar to the check answers method in determining type of question
            View view = findViewById(id);
            if (view instanceof RadioGroup){
                //radiogroup clearcheck() method makes this too easy
                RadioGroup radioGroup = (RadioGroup) view;
                radioGroup.clearCheck();
            } else if (view instanceof EditText){
                //Making the text of the edittext blank is too easy
                EditText editText = (EditText) view;
                editText.setText("");
            } else if (view instanceof LinearLayout){
                //why
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++){
                    if (viewGroup.getChildAt(i) instanceof CheckBox){
                        CheckBox checkBox = (CheckBox) viewGroup.getChildAt(i);
                        checkBox.setChecked(false);
                    }
                }
            }
        }
    }
}
