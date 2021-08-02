package com.example.solveit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.solveit.repository.ScoreRepository


class ScoreViewModelProviderFactory(
    val app: Application,
    val scoreRepository: ScoreRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ScoreViewModel(app, scoreRepository) as T
    }
}