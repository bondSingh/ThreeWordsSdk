package saty.threewords

import com.what3words.javawrapper.response.Suggestion
import com.what3words.javawrapper.response.SuggestionWithCoordinates

class WhatThreeWordsResult (data: Suggestion, coordinates: SuggestionWithCoordinates) {
    val suggestion: Suggestion
    val suggestionWithCoordinates: SuggestionWithCoordinates

    init {
        suggestion = data
        suggestionWithCoordinates = coordinates
    }
}