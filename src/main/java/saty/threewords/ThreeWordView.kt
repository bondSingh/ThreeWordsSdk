package saty.threewords

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.what3words.androidwrapper.What3WordsV3
import com.what3words.androidwrapper.helpers.AutosuggestHelper
import com.what3words.javawrapper.request.AutosuggestOptions
import com.what3words.javawrapper.request.Coordinates
import com.what3words.javawrapper.response.Suggestion
import com.what3words.javawrapper.response.SuggestionWithCoordinates


class ThreeWordView: LinearLayout, CustomAdapter.OnSuggestionItemSelectedListner {
    private val TAG: String = "ThreeWordView"
    private var what3WordResultListener: OnWhat3WordResultListener? = null
    lateinit var newView: View
    lateinit var editText: EditText
    lateinit var searchText: String
    private lateinit var customAdapter: CustomAdapter
    private lateinit var what3words: What3WordsV3
    private lateinit var autosuggestOptions: AutosuggestOptions
    private lateinit var autosuggestHelper: AutosuggestHelper

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.ColorOptionsView, 0, 0
        )
        val titleText = a.getString(R.styleable.ColorOptionsView_titleText)
        val holoBlueLight: Int = resources.getColor(android.R.color.holo_blue_light)
        val valueColor = a.getColor(
            R.styleable.ColorOptionsView_valueColor,
            holoBlueLight
        )
        a.recycle()

        orientation = HORIZONTAL
        //gravity = Gravity.CENTER_VERTICAL

        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        newView = inflater.inflate(R.layout.three_word_view, this, true)
        //minimumHeight = newView.height

        /*val title = getChildAt(0) as TextView
        title.text = titleText

        mValue = getChildAt(1)
        mValue.setBackgroundColor(valueColor)

        mImage = getChildAt(2) as ImageView*/
        setupView(newView)
        setupThreeWord()
    }

    constructor(context: Context) : super(context) {
        ThreeWordView(context, null)
    }

    fun setValueColor(color: Int) {
        //mValue.setBackgroundColor(color)
    }

    fun setImageVisible(visible: Boolean) {
        //mImage.setVisibility(if (visible) VISIBLE else GONE)
    }

    fun setOnWhat3WordResultListener(listener: OnWhat3WordResultListener) {
        what3WordResultListener = listener
    }

    private fun setupView(view: View) {
        val editText: EditText = view.findViewById(R.id.edit_text)
        val suggestionsListView: ScrollView = view.findViewById(R.id.suggestion_list_c)
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.d(TAG, "onTextChanged, count: $count")
                searchText = ""
                if (s.isBlank()) return
                val sBuilder: StringBuilder = java.lang.StringBuilder(s)
                searchText = sBuilder.toString()
                if (!Utils.isValidSearchText(searchText)) {
                    suggestionsListView.visibility = View.GONE
                    return
                }
                suggestionsListView.visibility = View.VISIBLE
                //homeViewModel.setEditText(text)
                fetchSuggestions(searchText)
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        val suggestionsList: RecyclerView = view.findViewById(R.id.suggestion_list)
        suggestionsList.layoutManager = LinearLayoutManager(context)
        customAdapter = CustomAdapter(this)
        suggestionsList.adapter = customAdapter
    }

    private fun setupThreeWord() {
        what3words = What3WordsV3("YIYC22PB", context)
        autosuggestOptions = AutosuggestOptions().apply {
            // apply all clippings here (focus, clipToCountry, clipToCircle, etc.)
            focus = Coordinates(51.5209433, -0.1962334)
        }

        autosuggestHelper = AutosuggestHelper(what3words).options(autosuggestOptions)
    }

    private fun fetchSuggestions(text: String) {
        // Get suggestions

        //customAdapter.setData(createDummyData(text.length))

        autosuggestHelper.options(autosuggestOptions).update(
            text.toString(),
            onSuccessListener = { suggestionResults ->
                var dataList: ArrayList<Suggestion> = ArrayList()
                suggestionResults.forEach { suggestion ->
                    //Add suggestion to existing RecyclerView adapter
                    dataList.add(suggestion)

                    Log.i(TAG, "suggestion: " + suggestion.words)
                }
                //notify adapter that there's changes on the data.
                customAdapter.setData(dataList)
            },
            onFailureListener = {
                //log any errors returned by what3words API.
                Log.e(TAG, it.message)
                customAdapter.clearData()
            }
        )
    }

    private fun getCoordinates(data: Suggestion) {
        autosuggestHelper.selectedWithCoordinates(
            searchText,
            data,
            onSuccessListener = { result ->
                //Toast.makeText(this,"suggestion selected from what3words, ${w3wWithCoordinates.words}, ${w3wWithCoordinates.coordinates.lat} ${w3wWithCoordinates.coordinates.lng}", Toast.LENGTH_LONG).show()
                Log.d("saty", "" + "suggestion selected from what3words, ${result.words}, ${result.coordinates.lat} ${result.coordinates.lng}")
                what3WordResultListener?.onWhat3WordResult(WhatThreeWordsResult(data, result))
                //return@selectedWithCoordinates
            },
            onFailureListener = {
                Log.e("MainActivity", it.message)
                what3WordResultListener?.onWhat3WordResult(WhatThreeWordsResult(data, SuggestionWithCoordinates(data)))
            }
        )
    }

    override fun onSuggestionItemSelected(data: Suggestion) {
        getCoordinates(data)
    }

    interface OnWhat3WordResultListener {
        fun onWhat3WordResult(result: WhatThreeWordsResult)
    }

}