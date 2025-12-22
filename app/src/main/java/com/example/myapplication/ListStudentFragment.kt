package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentListStudentBinding

class ListStudentFragment : Fragment() {

    private lateinit var binding: FragmentListStudentBinding
    private val viewModel: StudentViewModel by activityViewModels()
    private lateinit var adapter: StudentRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeStudents()
        setupToolbar()
    }

    private fun setupRecyclerView() {
        adapter = StudentRecyclerAdapter(
            emptyList(),
            onItemClick = { student, position ->
                viewModel.setCurrentStudent(student, position)
                findNavController().navigate(R.id.action_listStudentFragment_to_editStudentFragment)
            },
            onDeleteClick = { position ->
                confirmDelete(position)
            }
        )
        binding.rvStudents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStudents.adapter = adapter
    }

    private fun observeStudents() {
        viewModel.students.observe(viewLifecycleOwner) { students ->
            adapter.updateList(students)
            binding.emptyState.visibility = if (students.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_add_student -> {
                    viewModel.clearCurrentStudent()
                    findNavController().navigate(R.id.action_listStudentFragment_to_addStudentFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun confirmDelete(position: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Xóa sinh viên")
            .setMessage("Bạn có chắc muốn xóa sinh viên này?")
            .setPositiveButton("Xóa") { _, _ ->
                viewModel.deleteStudent(position)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}
