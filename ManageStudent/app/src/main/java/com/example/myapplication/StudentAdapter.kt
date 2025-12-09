package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class StudentAdapter(
    private val context: Context,
    private val students: MutableList<Student>
) : BaseAdapter() {

    override fun getCount(): Int = students.size

    override fun getItem(position: Int): Any = students[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.student_item, parent, false)
            holder = ViewHolder(
                view.findViewById(R.id.tvStudentName),
                view.findViewById(R.id.tvStudentId)
            )
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val student = students[position]
        holder.tvStudentName.text = student.name
        holder.tvStudentId.text = student.studentId

        return view
    }

    private data class ViewHolder(
        val tvStudentName: TextView,
        val tvStudentId: TextView
    )
}
