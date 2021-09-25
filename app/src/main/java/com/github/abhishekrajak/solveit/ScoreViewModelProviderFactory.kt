package com.github.abhishekrajak.solveit

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.abhishekrajak.solveit.viewModel.ScoreViewModel


class ScoreViewModelProviderFactory(
    private val app: Application,
    private val sharedPreferences: SharedPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ScoreViewModel(app, sharedPreferences) as T
    }
}