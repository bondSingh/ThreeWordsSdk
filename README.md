# ThreeWordsSdk
Custom UI to handle What3Words basic functionality 

In order to use this SDK, please follow the following steps:
1) inside your application terminal clone this project as a submodule
    git submodule add https://github.com/bondSingh/ThreeWordsSdk.git 

2) Add the following dependency nside the gradle to use this submodule inside your application:
    implementation project(path: ':ThreeWordsSdk')

3) For the sake of simplicity what3words objects are used as it is. In order to recieve the response in same format include the following dependency:
    implementation 'com.what3words:w3w-android-wrapper:3.1.15'
    
4) The custom view is ready to use. Include the view inside the layout file as per the requirement:
    <saty.threewords.ThreeWordView
        android:id="@+id/custom_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="@dimen/padding_max"
      />
      
5) Implement the following listener:
    ThreeWordView.OnWhat3WordResultListener
    
6) Attach the listener before onResume so that user clicks are captured properly.
    val customView: ThreeWordView = findViewById(R.id.custom_view)
    customView.setOnWhat3WordResultListener(this)
    
7) Override the following method to get the result of the user selected item. This will return the selected items and the coordinates of the location.

    override fun onWhat3WordResult(result: WhatThreeWordsResult) {
        val data: SuggestionWithCoordinates = result.suggestionWithCoordinates
        if (data.coordinates == null) binding.resultText.text = "No coordinates for: ${data.words}"
        else binding.resultText.text =
            "Result->\n" +
                    "${data.words}\n" +
                    "Lat:${data.coordinates.lat}\n" +
                    "Lng:${data.coordinates.lng}"
    }
    
