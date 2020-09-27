package com.abdullahalamodi.geoquiz

import androidx.lifecycle.ViewModel
import kotlin.random.Random

class QuizViewModel : ViewModel() {

    var currentIndex = 0;
    var isCheater = false;
    var score = 0;
    var level = 1;
    private val numOfQuestions = 5;  //start from 0
    private val easyQ = listOf(
        Question(R.string.easy_q_1, true),
        Question(R.string.easy_q_2, true),
        Question(R.string.easy_q_3, false),
        Question(R.string.easy_q_4, false),
        Question(R.string.easy_q_5, true),
        Question(R.string.easy_q_6, true)
    )
    private val normalQ = listOf(
        Question(R.string.normal_q_1, true),
        Question(R.string.normal_q_2, false),
        Question(R.string.normal_q_3, true),
        Question(R.string.normal_q_4, false),
        Question(R.string.normal_q_5, true),
        Question(R.string.normal_q_6, false)
    )
    private val hardQ = listOf(
        Question(R.string.hard_q_1, true),
        Question(R.string.hard_q_2, false),
        Question(R.string.hard_q_3, false),
        Question(R.string.hard_q_4, false),
        Question(R.string.hard_q_5, true),
        Question(R.string.hard_q_6, true)
    )
    private val questionBank = arrayListOf<Question>();

    val question: Question
        get() = questionBank[currentIndex];

    val questionBankSize: Int
        get() = questionBank.size;

    fun generateRandomQuestions() {
//        generate random positions
        var random = intArrayOf(5, 2, 1, 3, 4, 0);
        random.shuffle();
        if (questionBank.isEmpty()) {
            for (i in 0..numOfQuestions) {
                when (i) {
                    in 0..1 -> {
                        questionBank.add(easyQ[random[i]])
                    }
                    1 -> random.shuffle(); //after fill easyQs we reuse the random array
                    in 2..3 ->
                        questionBank.add(normalQ[random[i]])
                    3 -> random.shuffle(); //after fill normalQs we reuse the random array
                    else -> questionBank.add(hardQ[random[i]])
                }
            }
        }
    }

    fun moveNext() {
        currentIndex = (currentIndex + 1) % questionBank.size;
    }

    fun backPrevious() {
        currentIndex = (questionBank.size + (currentIndex - 1)) % questionBank.size;
    }

}