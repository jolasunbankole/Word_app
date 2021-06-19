package com.example.android.unscramble.ui.game
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

/**
 * Here is where my calculations, so when the user interacts with the app,
 * it will be accurate
 */

class GameViewModel: ViewModel() {

    // List of words used in the game
    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    // This block of code is run when the object instance is first created and initialized.
    // this code allows me to create an immutable
    // Meaning, it is only in this ViewModel i can change it, out side in the UI
    // The contents will never change, unless i add code to the fragment class ViewModel
    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private var _currentWordCount= MutableLiveData(0)
    val currentWordCount : LiveData<Int>
        get() = _currentWordCount

    private val _currentScrambledWord = MutableLiveData<String>()
        val currentScrambledWord: LiveData<Spannable> = Transformations.map(_currentScrambledWord) {
            if (it == null) {
                SpannableString("")
            } else {
                val scrambledWord = it.toString()
                val spannable: Spannable = SpannableString(scrambledWord)
                spannable.setSpan(
                    TtsSpan.VerbatimBuilder(scrambledWord).build(),
                    0,
                    scrambledWord.length,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
                spannable
            }
        }

    private fun increaseScore(){
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }



    // This block of code is initialized when the ViewModel is destroyed by a change
    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }



    /*
    * Updates currentWord and currentScrambledWord with the next word.
    */
    private fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()
    //This code makes sure the word in the app, is always scrambled
        while (tempWord.toString().equals(currentWord, false)) {
            tempWord.shuffle()
        }
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord.value = String(tempWord)
            _currentWordCount.value = _currentWordCount.value?.inc()
            wordsList.add(currentWord)
        }

    }


    //Helper method, for generating the next word
    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false


    }

    //validate the player's word and increase the score if the guess is correct.
    // This will update the final score in your alert dialog.
    fun isUserWordCorrect(playerWord:String): Boolean{
        if (playerWord.equals(currentWord, true)){
            increaseScore()
            return true
        }
        return false
    }


    /*
    * Re-initializes the game data to restart the game.
    */
    fun reinitializeData(){
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
    }


}



