package com.example.note.ui.detail

import com.example.note.data.entity.NoteData
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.note.R
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat

class DetailFragment : Fragment() {
    private var noteData: NoteData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noteData = requireArguments().getParcelable(ARG_NOTE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_detail,
            container,
            false
        )

        if (noteData == null) return view

        view.findViewById<TextView>(R.id.detailTitle).text = noteData!!.title

        view.findViewById<TextView>(R.id.detailDescription).text = noteData!!.description

        view.findViewById<TextView>(R.id.detailDate).text = SimpleDateFormat("dd.MM.yyyy H:m:s").format(noteData!!.date)

        return view
    }

    companion object {
        const val ARG_NOTE = "Note"
        fun newInstance(noteData: NoteData?): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()

            args.putParcelable(ARG_NOTE, noteData)
            fragment.arguments = args

            return fragment
        }
    }
}