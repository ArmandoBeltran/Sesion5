package com.example.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

class MainActivity: AppCompatActivity() {
    private val CAMERA_PERMISSION_CODE = 100

    //Almacenamiento Interno
    val nombreArchivoAI = "miarchivo.txt"
    val datosAI = "Contenido del archivo para la app de Armando (Almacenamiento interno)"

    //Almacenamiento Externo
    val nombreArchivoAE = "miarchivoAE.txt"
    val datosAE = "Contenido del archivo para la app de Armando (Almacenamiento Externo)"

    //Almacenamiento Caché
    val clave = "Clave"
    val valor = "Mi valor de cache"

    //Almacenamiento SQLite
    val databaseHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        requestCameraPermission()
        //Almancenamiento interno
        //escribirDatosAlmacenamientoInterno(nombreArchivoAI, datosAI)

        //Almacenamiento Externo
       // escribirDatosAlmacenamientoExterno()

        //Almancenamiento Cache
        //escribirDatosAlmacenamientoCache(this, clave, valor)

        //Almacenamiento SQLite
        databaseHelper.addContact("Armando", "4773997048")

        //val fragment = ExampleFragment()
        //supportFragmentManager.beginTransaction()
        //    .add(R.id.fragmentContainer, fragment)
        //    .commit()
        val viewPage : ViewPager = findViewById(R.id.viewPager)
        val adapter = ViewPageAdapter(supportFragmentManager)
        viewPage.adapter = adapter

    }

    override fun onResume() {
        super.onResume()

        //Almacenamiento Interno
        //val contenido = leerDatosAlmacenamientoInterno(nombreArchivoAI)
        //Toast.makeText(this, contenido, Toast.LENGTH_LONG).show()

        //Almacenamiento Externo
        //val contenido = leerDatosAlmacenamientoExterno()
        //Toast.makeText(this, contenido, Toast.LENGTH_LONG).show()

        //Almacenamiento Cache
        //val contenido = leerDatosAlmacenamientoCache(this, clave)
        //Toast.makeText(this, contenido, Toast.LENGTH_LONG).show()

        //Almacenamiento SQLite
        val contenido = databaseHelper.getAllContacts()
        for (contact in contenido){
            Toast.makeText(this, "ID: ${contact.id}, Name: ${contact.name}, Phone: ${contact.phone}", Toast.LENGTH_LONG).show()
        }
    }

    private fun requestCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            //Permiso otorgado
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode){
            CAMERA_PERMISSION_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permiso
                }else{
                    // no permiso
                }
            }
        }
    }

    fun escribirDatosAlmacenamientoInterno(nombreArchivo: String, datos: String){
        val archivo = File(this.filesDir, nombreArchivo)
        archivo.writeText(datos)
    }

    fun leerDatosAlmacenamientoInterno(nombreArchivo: String): String {
        val archivo = File(this.filesDir, nombreArchivo)
        return archivo.readText()
    }

    private fun escribirDatosAlmacenamientoExterno(){
        val estado = isExternalStorageWritable()
        if(estado) {
            val directorio = getExternalFilesDir(null)
            val archivo = File(directorio, nombreArchivoAE)
            try {
                FileOutputStream(archivo).use {
                    it.write(datosAE.toByteArray())
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun leerDatosAlmacenamientoExterno(): String {
        val estado = isExternalStorageReadable()
        if (estado){
            val directorio = getExternalFilesDir(null)
            val archivo = File(directorio, nombreArchivoAE)
            var fileInputStream = FileInputStream(archivo)
            var inputStreamReader : InputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null
            while({text = bufferedReader.readLine(); text}() != null){
                stringBuilder.append(text)
            }
            fileInputStream.close()

            return stringBuilder.toString()
        }
        return ""
    }

    private fun isExternalStorageWritable(): Boolean {
        val estado = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == estado
    }

    private fun isExternalStorageReadable(): Boolean {
        val estado = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == estado || Environment.MEDIA_MOUNTED_READ_ONLY == estado
    }

    fun escribirDatosAlmacenamientoCache(context: Context, clave: String, valor: String) {
        val sharedPreferences = context.getSharedPreferences("cache", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(clave, valor)
        editor.apply()
    }

    fun leerDatosAlmacenamientoCache(context: Context, clave: String): String? {
        val sharedPreferences = context.getSharedPreferences("cache", Context.MODE_PRIVATE)
        return sharedPreferences.getString(clave, null)
    }
}