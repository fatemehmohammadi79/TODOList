package com.example.todolist.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.adapter.ChoreListAdapter
import com.example.todolist.datalayers.ChoreDatabaseHandler
import com.example.todolist.models.Chore
import kotlinx.android.synthetic.main.activity_chore_list.*
import kotlinx.android.synthetic.main.popup.view.*

class ChoreListActivity : AppCompatActivity() {

    private var adapter: ChoreListAdapter? = null
    private var dbHandler: ChoreDatabaseHandler? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    var choreList: ArrayList<Chore>? = null
    private var alertDialogBuilder: AlertDialog.Builder? = null
    private var dialogAlert: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chore_list)

        choreList = ArrayList()
        layoutManager = LinearLayoutManager(this)
        dbHandler = ChoreDatabaseHandler(this)
        getAllDataFromDatabase()
    }

    private fun getAllDataFromDatabase() {
        choreList = dbHandler!!.readChore()
        choreList?.reverse()
        adapter = ChoreListAdapter(this, choreList!!)
        recyclerview.layoutManager = layoutManager
        recyclerview.adapter = adapter
        adapter!!.notifyDataSetChanged()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_menu_button) {
            createPopupDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createPopupDialog() {
        var view = layoutInflater.inflate(R.layout.popup, null)
        var popChoreName = view.poptvChore
        var popAssignedBy = view.poptvAssignedBy
        var popAssignedTo = view.poptvAssignedTo
        var popSaveButton = view.popbtn_SaveChore

        alertDialogBuilder = AlertDialog.Builder(this).setView(view)
        dialogAlert = alertDialogBuilder!!.create()
        dialogAlert!!.show()

        popSaveButton.setOnClickListener {
            var name = popChoreName.text.toString().trim()
            var assignedBy = popAssignedBy.text.toString().trim()
            var assignedTo = popAssignedTo.text.toString().trim()

            if (
                !TextUtils.isEmpty(name) &&
                !TextUtils.isEmpty(assignedBy) &&
                !TextUtils.isEmpty(assignedTo)
            ) {
                var chore = Chore()
                chore.choreName = name
                chore.assignedBy = assignedBy
                chore.assignedTo = assignedTo

                dbHandler!!.createChore(chore)
                dialogAlert!!.dismiss()
                getAllDataFromDatabase()
//                startActivity(Intent(this, ChoreListActivity::class.java))
//                finish()
            } else {
                Toast.makeText(this, "please enter a chore !!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}