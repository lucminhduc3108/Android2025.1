package com.example.filemanagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FileAdapter(
    private val files: List<File>,
    private val onItemClick: (File) -> Unit,
    private val onItemLongClick: (File, View) -> Unit
) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val sizeTextView: TextView = itemView.findViewById(R.id.sizeTextView)
        val menuButton: TextView = itemView.findViewById(R.id.menuButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = files[position]
        
        holder.nameTextView.text = file.name
        
        if (file.isDirectory) {
            holder.sizeTextView.text = "${file.listFiles()?.size ?: 0} items"
        } else {
            holder.sizeTextView.text = formatFileSize(file.length())
        }

        holder.itemView.setOnClickListener {
            onItemClick(file)
        }

        holder.menuButton.setOnClickListener {
            onItemLongClick(file, it)
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClick(file, it)
            true
        }
    }

    override fun getItemCount(): Int = files.size

    private fun formatFileSize(size: Long): String {
        val kb = 1024.0
        val mb = kb * 1024
        val gb = mb * 1024

        return when {
            size >= gb -> String.format("%.2f GB", size / gb)
            size >= mb -> String.format("%.2f MB", size / mb)
            size >= kb -> String.format("%.2f KB", size / kb)
            else -> "$size bytes"
        }
    }
}
