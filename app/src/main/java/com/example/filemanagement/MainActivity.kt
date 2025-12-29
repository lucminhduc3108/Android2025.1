package com.example.filemanagement

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FileAdapter
    private var currentDirectory: File? = null
    private val requestCodePermissions = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (checkPermissions()) {
            initializeFileSystem()
        } else {
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = android.net.Uri.parse(String.format("package:%s", packageName))
                startActivityForResult(intent, requestCodePermissions)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivityForResult(intent, requestCodePermissions)
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                requestCodePermissions
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodePermissions) {
            if (checkPermissions()) {
                initializeFileSystem()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodePermissions) {
            if (checkPermissions()) {
                initializeFileSystem()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initializeFileSystem() {
        val externalStorage = Environment.getExternalStorageDirectory()
        currentDirectory = externalStorage
        loadFiles(externalStorage)
    }

    private fun loadFiles(directory: File) {
        currentDirectory = directory
        supportActionBar?.title = directory.name
        supportActionBar?.setDisplayHomeAsUpEnabled(directory.parent != null)

        val files = directory.listFiles()?.sortedWith(compareBy({ !it.isDirectory }, { it.name })) ?: emptyList()

        adapter = FileAdapter(
            files,
            onItemClick = { file ->
                if (file.isDirectory) {
                    loadFiles(file)
                } else {
                    openFile(file)
                }
            },
            onItemLongClick = { file, view ->
                showContextMenu(file, view)
            }
        )
        recyclerView.adapter = adapter
    }

    private fun openFile(file: File) {
        val fileName = file.name.lowercase()
        when {
            fileName.endsWith(".txt") -> {
                val intent = Intent(this, TextViewerActivity::class.java)
                intent.putExtra("file_path", file.absolutePath)
                startActivity(intent)
            }
            fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
            fileName.endsWith(".png") || fileName.endsWith(".bmp") -> {
                val intent = Intent(this, ImageViewerActivity::class.java)
                intent.putExtra("file_path", file.absolutePath)
                startActivity(intent)
            }
            else -> {
                Toast.makeText(this, "File type not supported", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showContextMenu(file: File, view: View) {
        val popupMenu = PopupMenu(this, view)
        val menuRes = if (file.isDirectory) R.menu.context_menu_folder else R.menu.context_menu_file
        popupMenu.menuInflater.inflate(menuRes, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.rename_folder, R.id.rename_file -> {
                    renameFile(file)
                    true
                }
                R.id.delete_folder, R.id.delete_file -> {
                    deleteFile(file)
                    true
                }
                R.id.copy_file -> {
                    copyFile(file)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun renameFile(file: File) {
        val input = EditText(this)
        input.setText(file.name)

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.rename))
            .setView(input)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty()) {
                    val newFile = File(file.parent, newName)
                    if (file.renameTo(newFile)) {
                        Toast.makeText(this, "Renamed successfully", Toast.LENGTH_SHORT).show()
                        currentDirectory?.let { loadFiles(it) }
                    } else {
                        Toast.makeText(this, "Failed to rename", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun deleteFile(file: File) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirm_delete))
            .setMessage(getString(R.string.delete_message))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                if (file.deleteRecursively()) {
                    Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show()
                    currentDirectory?.let { loadFiles(it) }
                } else {
                    Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    private fun copyFile(file: File) {
        val currentDir = currentDirectory ?: Environment.getExternalStorageDirectory()
        val directories = mutableListOf<File>()
        
        // Add current directory and parent if exists
        directories.add(currentDir)
        currentDir.parent?.let { parent ->
            directories.add(File(parent))
        }
        
        // Add immediate subdirectories
        currentDir.listFiles()?.forEach { f ->
            if (f.isDirectory && f.canRead()) {
                directories.add(f)
            }
        }
        
        val directoryNames = directories.map { 
            if (it == currentDir) "${it.name} (Current)" 
            else it.name 
        }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.select_destination))
            .setItems(directoryNames) { _, which ->
                val destinationDir = directories[which]
                val destinationFile = File(destinationDir, file.name)
                
                // Check if file already exists
                if (destinationFile.exists()) {
                    AlertDialog.Builder(this)
                        .setTitle("File exists")
                        .setMessage("File already exists. Overwrite?")
                        .setPositiveButton(getString(R.string.yes)) { _, _ ->
                            performCopy(file, destinationFile)
                        }
                        .setNegativeButton(getString(R.string.no), null)
                        .show()
                } else {
                    performCopy(file, destinationFile)
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
    
    private fun performCopy(sourceFile: File, destinationFile: File) {
        try {
            FileInputStream(sourceFile).use { input ->
                FileOutputStream(destinationFile).use { output ->
                    input.copyTo(output)
                }
            }
            Toast.makeText(this, "File copied successfully", Toast.LENGTH_SHORT).show()
            currentDirectory?.let { loadFiles(it) }
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to copy file: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.create_folder -> {
                createFolder()
                true
            }
            R.id.create_file -> {
                createTextFile()
                true
            }
            android.R.id.home -> {
                currentDirectory?.parent?.let { parent ->
                    loadFiles(File(parent))
                    true
                } ?: false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createFolder() {
        val input = EditText(this)
        input.hint = getString(R.string.folder_name)

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.create_folder))
            .setView(input)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                val folderName = input.text.toString().trim()
                if (folderName.isNotEmpty()) {
                    val newFolder = File(currentDirectory, folderName)
                    if (newFolder.mkdirs()) {
                        Toast.makeText(this, "Folder created successfully", Toast.LENGTH_SHORT).show()
                        currentDirectory?.let { loadFiles(it) }
                    } else {
                        Toast.makeText(this, "Failed to create folder", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun createTextFile() {
        val input = EditText(this)
        input.hint = getString(R.string.file_name)

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.create_file))
            .setView(input)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                val fileName = input.text.toString().trim()
                if (fileName.isNotEmpty()) {
                    val fileNameWithExt = if (!fileName.endsWith(".txt")) "$fileName.txt" else fileName
                    val newFile = File(currentDirectory, fileNameWithExt)
                    try {
                        newFile.createNewFile()
                        Toast.makeText(this, "File created successfully", Toast.LENGTH_SHORT).show()
                        currentDirectory?.let { loadFiles(it) }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Failed to create file: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
}