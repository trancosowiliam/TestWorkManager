package com.dalcim.testworkmanager.database

import android.os.Environment
import java.io.*

internal class FileManager {

    private val root by lazy { File(Environment.getExternalStorageDirectory(), DOCUMENTS_PATH) }

    private fun createAndSaveFile(fileName: String): File {
        return File(root, fileName).apply {
            if (!root.exists()) {
                root.mkdir()
            }

            if (root.exists() && this.exists().not()) {
                this.createNewFile()
            }
        }
    }

    fun writeToFile(data: String, fileName: String, append: Boolean = false) {
        val file = createAndSaveFile(fileName)

        var fileWriter: FileWriter? = null
        try {
            fileWriter = FileWriter(file, append)
            fileWriter.write(data)
            fileWriter.flush()
        } catch (e: Exception) {
        } finally {
            fileWriter?.close()
        }
    }

    fun readFromFile(fileName: String): String {
        val file = File(root, fileName)

        var textRead = ""
        try {
            val inputStream: InputStream = FileInputStream(file)
            inputStream.apply {
                textRead = inputStream.bufferedReader().use(BufferedReader::readText)
                inputStream.close()
            }
        } catch (e: FileNotFoundException) {
            createAndSaveFile(file.name)
        }
        return textRead
    }

    companion object {
        private const val DOCUMENTS_PATH = "documents"
    }
}