package com.abdullahalamodi.geoquiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

private const val REQUEST_CODE_CHEAT = 0;
private const val FULL_SCORE = 24; //summation of all questions score

class MainActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar;
    private lateinit var levelView: TextView;
    private lateinit var questionView: TextView;
    private lateinit var gradView: TextView;
    private lateinit var trueBtn: Button;
    private lateinit var falseBtn: Button;
    private lateinit var cheatBtn: Button;
    private lateinit var nextBtn: ImageButton;
    private lateinit var previousBtn: ImageButton;
    private lateinit var resetBtn: ImageButton;

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progressBar);
        levelView = findViewById(R.id.level);
        questionView = findViewById(R.id.question_v);
        gradView = findViewById(R.id.grade);
        trueBtn = findViewById(R.id.true_btn);
        falseBtn = findViewById(R.id.false_btn);
        cheatBtn = findViewById(R.id.cheat_button);
        nextBtn = findViewById(R.id.next_btn);
        previousBtn = findViewById(R.id.previous_btn);
        resetBtn = findViewById(R.id.reset_btn);
        quizViewModel.generateRandomQuestions();
        updateQuestion();

        trueBtn.setOnClickListener {
            checkAnswer(true);
        }

        falseBtn.setOnClickListener {
            checkAnswer(false);
        }

        cheatBtn.setOnClickListener {
            val answerIsTrue = quizViewModel.question.answer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT);
        }

        nextBtn.setOnClickListener {
            quizViewModel.moveNext()
            updateQuestion();
        }

        previousBtn.setOnClickListener {
            quizViewModel.backPrevious()
            updateQuestion();
        }

        resetBtn.setOnClickListener {
            resetQuestions();
        }

        questionView.setOnClickListener {
            quizViewModel.moveNext()
            updateQuestion();
        }


    }

    private fun updateQuestion() {
        setQProgress();
        val questionText = quizViewModel.question.textResId;
        questionView.setText(questionText);
        checkIfAnswering();
        //clear isCheater variable
        quizViewModel.isCheater = false;
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.question.answer;
        val messageResId =
            when {
                quizViewModel.isCheater -> R.string.judgment_toast;
                //correct answer
                correctAnswer == userAnswer -> {
                    quizViewModel.score += Question.getQuestionPoint(quizViewModel.currentIndex);
                    gradView.text = quizViewModel.score.toString();
                    R.string.correct_toast;
                }
                else -> R.string.incorrect_toast;
            }

        val toast = Toast.makeText(
            this, messageResId,
            Toast.LENGTH_SHORT
        );
        toast.show();
        Question.answeredQuestions++;
        // set question state to answered
        quizViewModel.question.isAnswered = true;
        setAnswerBtnsState(false);
        //to set answer face
        checkIfAnswering();
        //check if all questions is answered display grade
        if (Question.checkAnsweredQuestions(quizViewModel.questionBankSize)) {
            Toast.makeText(
                this,
                "You git: ${quizViewModel.score} from: $FULL_SCORE",
                Toast.LENGTH_SHORT
            ).show();
            //show reset button
            resetBtn.visibility = View.VISIBLE;
        }

    }

    private fun setAnswerBtnsState(enabled: Boolean) {
        trueBtn.isEnabled = enabled;
        falseBtn.isEnabled = enabled;
        cheatBtn.isEnabled = enabled;
    }

    private fun checkIfAnswering() {
        if (quizViewModel.question.isAnswered) {
            if (quizViewModel.question.answer) {
                questionView.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, getDrawable(R.drawable.true_24), null
                );
            } else {
                questionView.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, getDrawable(R.drawable.false_24), null
                );
            }
            setAnswerBtnsState(false);
        } else {
            setAnswerBtnsState(true);
            questionView.setCompoundDrawablesWithIntrinsicBounds(
                null, null, null, null
            );
        }
    }

    private fun setQProgress() {
        quizViewModel.level = Question.getQuestionLevel(quizViewModel.currentIndex);
        levelView.text = "${quizViewModel.level} / ${Question.LEVELS_NUM}"
        progressBar.progress = (quizViewModel.level *
                (quizViewModel.questionBankSize / Question.LEVELS_NUM) * (102 / quizViewModel.questionBankSize));
    }


    private fun resetQuestions() {
        quizViewModel.currentIndex = 0;
        quizViewModel.level = 1;
        quizViewModel.isCheater = false;
        quizViewModel.score = 0;
        quizViewModel.generateRandomQuestions();
        gradView.text = "0";
        updateQuestion();
        resetBtn.visibility = View.INVISIBLE;
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK)
            return

        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false;
        }
    }


}