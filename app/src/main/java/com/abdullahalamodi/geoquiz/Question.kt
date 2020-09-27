package com.abdullahalamodi.geoquiz

import androidx.annotation.StringRes

data class Question(
    @StringRes val textResId: Int,
    val answer: Boolean,
    var isAnswered: Boolean = false
) {

    companion object {
        private const val EASY_Q_POINT = 2;
        private const val NORMAL_Q_POINT = 4;
        private const val HARD_Q_POINT = 6;
        const val LEVELS_NUM = 3;
        var answeredQuestions: Int = 0;
        fun checkAnsweredQuestions(allQuestions: Int): Boolean {
            if (allQuestions == answeredQuestions)
                return true;
            return false;
        }

        fun getQuestionPoint(index: Int): Int {
            when (index) {
                in 0..1 -> return EASY_Q_POINT;
                in 2..3 -> return NORMAL_Q_POINT;
                in 4..6 -> return HARD_Q_POINT;
            }
            return 0;
        }

        fun getQuestionLevel(index: Int): Int {
            when (index) {
                in 0..1 -> return 1;
                in 2..3 -> return 2;
                in 4..6 -> return 3;
            }
            return 1;
        }
    }
}