package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentEditStudentBinding

class EditStudentFragment : Fragment() {

    private var _binding: FragmentEditStudentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StudentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeCurrentStudent()
        setupClickListeners()
    }

    private fun observeCurrentStudent() {
        viewModel.currentStudent.observe(viewLifecycleOwner) { student ->
            student?.let {
                binding.edtStudentId.setText(it.studentId)
                binding.edtName.setText(it.name)
                binding.edtPhoneNumber.setText(it.phoneNumber)
                binding.edtAddress.setText(it.address)
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnUpdate.setOnClickListener {
            updateStudent()
        }

        binding.btnDelete.setOnClickListener {
            confirmDelete()
        }

        binding.btnCancel.setOnClickListener {
            viewModel.clearCurrentStudent()
            findNavController().popBackStack()
        }
    }

    private fun updateStudent() {
        val studentId = binding.edtStudentId.text.toString().trim()
        val name = binding.edtName.text.toString().trim()
        val phoneNumber = binding.edtPhoneNumber.text.toString().trim()
        val address = binding.edtAddress.text.toString().trim()

        // Validate inputs
        if (name.isEmpty()) {
            binding.edtName.error = "Vui lòng nhập họ tên"
            return
        }

        if (phoneNumber.isEmpty()) {
            binding.edtPhoneNumber.error = "Vui lòng nhập số điện thoại"
            return
        }

        if (address.isEmpty()) {
            binding.edtAddress.error = "Vui lòng nhập địa chỉ"
            return
        }

        val updatedStudent = Student(studentId, name, phoneNumber, address)
        viewModel.updateStudent(updatedStudent)
        viewModel.clearCurrentStudent()
        findNavController().popBackStack()
    }

    private fun confirmDelete() {
        val currentStudent = viewModel.currentStudent.value
        if (currentStudent == null) return

        AlertDialog.Builder(requireContext())
            .setTitle("Xóa sinh viên")
            .setMessage("Bạn có chắc muốn xóa sinh viên này?")
            .setPositiveButton("Xóa") { _, _ ->
                viewModel.deleteStudent(currentStudent.studentId)
                viewModel.clearCurrentStudent()
                findNavController().popBackStack()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
