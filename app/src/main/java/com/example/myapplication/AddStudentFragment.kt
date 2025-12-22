package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentAddStudentBinding

class AddStudentFragment : Fragment() {

    private var _binding: FragmentAddStudentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StudentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnSave.setOnClickListener {
            saveStudent()
        }

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun saveStudent() {
        val studentId = binding.edtStudentId.text.toString().trim()
        val name = binding.edtName.text.toString().trim()
        val phoneNumber = binding.edtPhoneNumber.text.toString().trim()
        val address = binding.edtAddress.text.toString().trim()

        // Validate inputs
        if (studentId.isEmpty()) {
            binding.edtStudentId.error = "Vui lòng nhập MSSV"
            return
        }

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

        val student = Student(studentId, name, phoneNumber, address)
        viewModel.addStudent(student)
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
