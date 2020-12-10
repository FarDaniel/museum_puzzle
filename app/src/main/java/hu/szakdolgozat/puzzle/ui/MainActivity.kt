package hu.szakdolgozat.puzzle.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import hu.szakdolgozat.puzzle.R
import hu.szakdolgozat.puzzle.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    }

}