package com.example.android.unscramble.ui.game
import android.util.Log
import androidx.lifecycle.ViewModel

/**
 * Here is where my calculations, so when the user interacts with the app,
 * it will be accurate
 */

class GameViewModel: ViewModel() {

    // This block of code is run when the object instance is first created and initialized.
    // this code allows me to create an immutable
    // Meaning, it is only in this ViewModel i can change it, out side in the UI
    // The contents will never change, unless i add code to the fragment class ViewModel
    private var _score = 0
    val score: Int
        get() = _score

    private var _currentWordCount = 0
    val currentWordCount : Int
        get() = _currentWordCount

    private lateinit var _currentScrambledWord: String
    val currentScrambledWord: String
        get() = _currentScrambledWord

    private fun increaseScore(){
        _score += SCORE_INCREASE
    }

    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    // This block of code is initialized when the ViewModel is destroyed by a change
    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    // This block of code is initialized when the ViewModel is destroyed by a change
    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed")
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
            _currentScrambledWord = String(tempWord)
            ++_currentWordCount
            wordsList.add(currentWord)
        }

    }


    //Helper method, for generating the next word
    fun nextWord(): Boolean {
        return if (_currentWordCount < MAX_NO_OF_WORDS) {
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
        _score = 0
        _currentWordCount = 0
        wordsList.clear()
        getNextWord()
    }


}



