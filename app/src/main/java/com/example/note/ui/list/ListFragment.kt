package com.example.note.ui.list

import com.example.note.data.NotesSource
import com.example.note.Navigation
import com.example.note.MainActivity
import android.os.Bundle
import com.example.note.R
import com.example.note.data.NoteSourceFirebaseImpl
import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note.ui.detail.DetailFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.note.ui.add.NoteFragment
import com.example.note.data.entity.NoteData
import android.view.ContextMenu.ContextMenuInfo
import androidx.fragment.app.Fragment
import com.example.note.observe.Publisher
import com.example.note.ui.bottomSheet.DeleteBottomSheetFragment
import com.example.note.ui.bottomSheet.interfaces.OnDeleteDialogListener

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {
    private var source: NotesSource? = null
    private lateinit var adapter: ListAdapter
    private lateinit var navigation: Navigation
    private lateinit var publisher: Publisher
    private lateinit var activity: MainActivity
    private var isMoveToFirstPosition = false

    override fun onAttach(context: Context) {
        super.onAttach(context)

        activity = context as MainActivity
        navigation = activity.navigation
        publisher = activity.publisher
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        initFloatingBtn(view)
        initNoteList(view)
        setHasOptionsMenu(true)

        source = NoteSourceFirebaseImpl().init { adapter.notifyDataSetChanged() }
        adapter.setDataSource(source)

        return view
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initNoteList(listView: View) {
        val recyclerView: RecyclerView = listView.findViewById(R.id.recycler_view_lines)

        // Эта установка служит для повышения производительности системы
        // Указывает, что элементы одинаковые по размеру
        recyclerView.setHasFixedSize(true)

        // Будем работать со встроенным менеджером
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        // Установим адаптер
        adapter = ListAdapter(this)

        recyclerView.adapter = adapter
        if (isMoveToFirstPosition && source!!.size > 0) {
            recyclerView.scrollToPosition(0)
            isMoveToFirstPosition = false
        }

        adapter.setOnItemClickListener { view: View, position: Int ->
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

            navigation.addFragment(
                DetailFragment.newInstance(source!!.getNoteData(position)),
                true
            )
        }
    }

    private fun initFloatingBtn(view: View) {
        val floatingBtn: FloatingActionButton = view.findViewById(R.id.fab)

        if (floatingBtn.visibility == View.INVISIBLE) {
            floatingBtn.visibility = View.VISIBLE
        }

        floatingBtn.setOnClickListener { onClickAddNote() }
    }

    private fun onClickAddNote() {
        navigation.addFragment(NoteFragment(), true)

        publisher.subscribe { noteData ->
            source!!.addNoteData(noteData)
            adapter.notifyItemInserted(source!!.size - 1)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.activity_main_menu, menu)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenuInfo?,
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = requireActivity().menuInflater
        inflater.inflate(R.menu.card_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return onItemSelected(item.itemId) || super.onOptionsItemSelected(item)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return onItemSelected(item.itemId) || super.onContextItemSelected(item)
    }

    private fun onItemSelected(menuItemId: Int): Boolean {
        return when (menuItemId) {
            R.id.action_card_menu_item_update -> {
                val updatePosition = adapter.menuPosition

                navigation.addFragment(
                    NoteFragment.newInstance(source!!.getNoteData(updatePosition)),
                    true
                )

                publisher.subscribe { noteData: NoteData? ->
                    source!!.updateNoteData(updatePosition, noteData)
                    adapter.notifyItemChanged(updatePosition)
                }

                true
            }

            R.id.action_card_menu_item_delete -> {
                val deleteBottomSheetFragment = DeleteBottomSheetFragment.newInstance()

                deleteBottomSheetFragment.setOnDialogListener(dialogListener)
                activity.initDialog(deleteBottomSheetFragment)

                true
            }

            R.id.action_main_menu_clear_all -> {
                source!!.clearNoteData()
                adapter.notifyDataSetChanged()

                true
            }

            else -> false
        }
    }

    // Слушатель диалога, сюда попадает управление при выборе пользователем кнопки в диалоге
    private val dialogListener: OnDeleteDialogListener = object : OnDeleteDialogListener {
        override fun onDialogYes() {
            val deletePosition = adapter.menuPosition

            source!!.deleteNoteData(deletePosition)
            adapter.notifyItemRemoved(deletePosition)
        }

        override fun onDialogNo() {}
    }
}