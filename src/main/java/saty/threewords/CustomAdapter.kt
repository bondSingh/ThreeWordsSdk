package saty.threewords

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.what3words.javawrapper.response.Suggestion

class CustomAdapter (
    private var itemListner: OnSuggestionItemSelectedListner
        ) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    private var dataSet: ArrayList<Suggestion> = ArrayList()


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val headerText: TextView = view.findViewById(R.id.header_text)
        val detailsText: TextView
        val distanceText: TextView
        val fullView: View
        //val  imageView: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            detailsText = view.findViewById(R.id.details_text)
            distanceText = view.findViewById(R.id.distance_text)
            //imageView = view.findViewById(R.id.imageView)
            fullView = view
        }

    }

    fun setData(data: ArrayList<Suggestion>) {
        if (data == null || data.isEmpty()) return
        dataSet = data
        notifyDataSetChanged()
        Log.d("saty", "setData")
    }

    fun clearData() {
        dataSet.clear()
        notifyDataSetChanged()
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_item, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val data: Suggestion = dataSet[position]

        viewHolder.headerText.text = data.words
        viewHolder.detailsText.text = "near ${data.nearestPlace}"
        viewHolder.distanceText.text = "${data.distanceToFocusKm} km"
        viewHolder.itemView.tag = position
        viewHolder.fullView.setOnClickListener {
            Log.d("saty", "View selected : ${position}")
            itemListner.onSuggestionItemSelected(data)
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    interface OnSuggestionItemSelectedListner {
        fun onSuggestionItemSelected(data: Suggestion)
    }
}