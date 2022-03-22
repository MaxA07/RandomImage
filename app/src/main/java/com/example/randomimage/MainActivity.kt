package com.example.randomimage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.randomimage.databinding.ActivityMainBinding
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var useKeyword: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.RandomImageButton.setOnClickListener {
            onGetRandomImagePressed()
        }

        binding.setText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                return@setOnEditorActionListener onGetRandomImagePressed()
            }
            return@setOnEditorActionListener false
        }

        binding.keyWordCheckBox.setOnClickListener {
            this.useKeyword = binding.keyWordCheckBox.isChecked
            updateUi()
        }
        updateUi()

    }

    private fun onGetRandomImagePressed(): Boolean {

        val keyword = binding.setText.text.toString()
        if (useKeyword && keyword.isBlank()) {
            binding.setText.error = "KeyWord is empty"
            return true
        }

        val encodedKeyWord = URLEncoder.encode(keyword, StandardCharsets.UTF_8.name())
        Glide.with(this)
            .load("https://source.unsplash.com/random/800x600?${if (useKeyword) "?$encodedKeyWord" else ""}")
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(binding.testImageView)
        return false
    }

    private fun updateUi() = with(binding) {
        keyWordCheckBox.isChecked = useKeyword
        if (useKeyword) {
            setText.visibility = View.VISIBLE
        }
        else {
            setText.visibility = View.GONE
        }
    }


}