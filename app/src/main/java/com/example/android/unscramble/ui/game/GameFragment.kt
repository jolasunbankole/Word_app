package com.example.android.unscramble.ui.game

/**
 * This is what people that use the app, will see and interact with
 */


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment where the game is played, contains the game logic.
 */


class GameFragment : Fragment() {

    //Attaching ViewModel to the Fragment(UI)
    private val viewModel: GameViewModel by viewModels()


        // Binding object instance with access to the views in the game_fragment.xml layout
        private lateinit var binding: GameFragmentBinding

        // Create a ViewModel the first time the fragment is created.
        // If the fragment is re-created, it receives the same GameViewModel instance created by the
        // first fragment

        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View {
            // Inflate the layout XML file and return a binding object instance
            // Verify the ViewModel preserves data with Logcat
            binding = GameFragmentBinding.inflate(inflater, container, false)
            Log.d("GameFragment", "GameFragment created/re-created!")
            Log.d("GameFragment", "Word: ${viewModel.currentScrambledWord} " +
                    "Score: ${viewModel.score} WordCount: ${viewModel.currentWordCount}")
            return binding.root

        }

    override fun onDetach() {
        super.onDetach()
        Log.d("GameFragment", "GameFragment destroyed!")
    }



        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            // Setup a click listener for the Submit and Skip buttons.
            binding.submit.setOnClickListener { onSubmitWord() }
            binding.skip.setOnClickListener { onSkipWord() }
            // Update the UI
            updateNextWordOnScreen()
            binding.score.text = getString(R.string.score, 0)
            binding.wordCount.text = getString(
                    R.string.word_count, 0, MAX_NO_OF_WORDS)
        }

        /*
        * Checks the user's word, and updates the score accordingly.
        * Displays the next scrambled word.
        * validate the user's guess by checking against the original word
        * If the word is correct, then go to the next word (or show the dialog if the game has ended)
        * If the word is incorrect, show an error on the text field and stay on the current word.
        */
        private fun onSubmitWord() {
            val playerWord = binding.textInputEditText.text.toString()
        // Setting the error messages on the screen
            if (viewModel.isUserWordCorrect(playerWord)) {
                setErrorTextField((false))
                if (viewModel.nextWord()) {
                    updateNextWordOnScreen()
                } else {
                    showFinalScoreDialog()
                }
            }else{
                setErrorTextField(true)
            }
        }

        /*
        * Skips the current word without changing the score.
        * Increases the word count.
        */
        private fun onSkipWord() {
            if (viewModel.nextWord()) {
                setErrorTextField(false)
                updateNextWordOnScreen()
            }else{
                showFinalScoreDialog()
            }
        }

        /*
        * Creates and shows an AlertDialog with the final score.
        */
        private fun showFinalScoreDialog(){
            MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.congratulations))
                    .setMessage(getString(R.string.you_scored, viewModel.score))
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.exit)){_, _ ->
                        exitGame()
                    }
                    .setPositiveButton(getString(R.string.play_again)){ _, _ ->
                        restartGame()
                    }
                    .show()
        }

        /*
        * Gets a random word for the list of words and shuffles the letters in it.
        */
        private fun getNextScrambledWord(): String {
            val tempWord = allWordsList.random().toCharArray()
            tempWord.shuffle()
            return String(tempWord)
        }

        /*
        * Re-initializes the data in the ViewModel and updates the views with the new data, to
        * restart the game.
        */
        private fun restartGame() {
            viewModel.reinitializeData()
            setErrorTextField(false)
            updateNextWordOnScreen()
        }

        /*
        * Exits the game.
        */
        private fun exitGame() {
            activity?.finish()
        }

        /*
        * Sets and resets the text field error status.
        */
        private fun setErrorTextField(error: Boolean) {
            if (error) {
                binding.textField.isErrorEnabled = true
                binding.textField.error = getString(R.string.try_again)
            } else {
                binding.textField.isErrorEnabled = false
                binding.textInputEditText.text = null
            }
        }

        /*
        * Displays the next scrambled word on screen.
        */
        private fun updateNextWordOnScreen() {
            binding.textViewUnscrambledWord.text = viewModel.currentScrambledWord

        }

}