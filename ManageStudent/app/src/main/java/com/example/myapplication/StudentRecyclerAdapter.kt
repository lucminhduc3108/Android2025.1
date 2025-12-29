package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.StudentItemBinding

class StudentRecyclerAdapter(
    private var students: List<Student>,
    private val onItemClick: (Student, Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<StudentRecyclerAdapter.StudentViewHolder>() {

    fun updateList(newList: List<Student>) {
        students = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = StudentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(students[position], position)
    }

    override fun getItemCount(): Int = students.size

    inner class StudentViewHolder(private val binding: StudentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(student: Student, position: Int) {
            binding.tvStudentName.text = student.name
            binding.tvStudentId.text = student.studentId
            binding.tvPhoneNumber.text = student.phoneNumber
            binding.tvAddress.text = student.address

            binding.root.setOnClickListener {
                onItemClick(student, position)
            }

            binding.btnDelete.setOnClickListener {
                onDeleteClick(position)
            }
        }
    }
}
