package com.example.faithportal
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.faithportal.Model.PrayerEntry
import com.example.faithportal.Model.ViewModel.PrayerJournalViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PrayerJournalFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var prayerJournalViewModel: PrayerJournalViewModel
    private lateinit var editTextPrayer: EditText
    private lateinit var buttonAddPrayer: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PrayerEntryAdapter

    companion object {
        private const val TAG = "ThirdFragment"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PrayerJournalFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_prayer_journal, container, false)

        editTextPrayer = view.findViewById(R.id.edit_text_prayer)
        buttonAddPrayer = view.findViewById(R.id.button_add_prayer)
        recyclerView = view.findViewById(R.id.recycler_view_prayers)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = PrayerEntryAdapter()
        recyclerView.adapter = adapter

        prayerJournalViewModel = ViewModelProvider(this).get(PrayerJournalViewModel::class.java)

        prayerJournalViewModel.allPrayerEntries.observe(viewLifecycleOwner, Observer { prayerEntries ->
            // Update RecyclerView
            adapter.setPrayerEntries(prayerEntries)
        })

        buttonAddPrayer.setOnClickListener {
            val prayerContent = editTextPrayer.text.toString()
            if (prayerContent.isNotEmpty()) {
                val prayerEntry = PrayerEntry("10/14/2024", prayerContent, 1)
                prayerJournalViewModel.insert(prayerEntry)
                editTextPrayer.setText("") // Clear input
            }
        }

        return view
    }
}

class PrayerEntryAdapter : RecyclerView.Adapter<PrayerEntryAdapter.PrayerEntryHolder>() {

    private var prayerEntries: List<PrayerEntry> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrayerEntryHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prayer_entry, parent, false)
        return PrayerEntryHolder(itemView)
    }

    override fun onBindViewHolder(holder: PrayerEntryHolder, position: Int) {
        val currentPrayerEntry = prayerEntries[position]
        holder.textViewPrayer.text = currentPrayerEntry.content
        holder.textViewDate.text = currentPrayerEntry.date
    }

    override fun getItemCount(): Int {
        return prayerEntries.size
    }

    fun setPrayerEntries(prayerEntries: List<PrayerEntry>) {
        this.prayerEntries = prayerEntries
        notifyDataSetChanged()
    }

    inner class PrayerEntryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewPrayer: TextView = itemView.findViewById(R.id.text_view_prayer)
        val textViewDate: TextView = itemView.findViewById(R.id.text_view_date)
    }


}