package com.vptarasov.autosearch.util

import android.graphics.*
import com.googlecode.tesseract.android.TessBaseAPI
import com.vptarasov.autosearch.App
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class OCR @Throws(IOException::class)
constructor(bitmap: Bitmap) {

    private val datapath: String
    private var recognizedText: String? = null


    init {

        var imageWithBG = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        imageWithBG.eraseColor(Color.WHITE)
        val canvas = Canvas(imageWithBG)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        imageWithBG = bitmapResizer(imageWithBG, imageWithBG.width * 2, imageWithBG.height * 2)
        imageWithBG = removeNoise(imageWithBG)


        datapath = App.instance?.filesDir.toString() + "/tesseract/"

        val baseApi = TessBaseAPI()

        checkFile(File(datapath + "tessdata/"))


        baseApi.init(datapath, "eng")

        baseApi.setImage(imageWithBG)

        val res = baseApi.utF8Text


        recognizedText = if (res != null && res.contains("(")) {
            res.substring(res.indexOf('('))
        } else
            res

    }

    private fun removeNoise(bmap: Bitmap): Bitmap {
        for (x in 0 until bmap.width) {
            for (y in 0 until bmap.height) {
                val pixel = bmap.getPixel(x, y)
                val red = Color.red(pixel)
                val green = Color.green(pixel)
                val blue = Color.blue(pixel)
                if (red > 162 && green > 162 && blue > 162)
                    bmap.setPixel(x, y, Color.BLACK)
            }
        }
        for (x in 0 until bmap.width) {
            for (y in 0 until bmap.height) {
                val pixel = bmap.getPixel(x, y)
                val red = Color.red(pixel)
                val green = Color.green(pixel)
                val blue = Color.blue(pixel)
                if (red > 162 && green > 162 && blue > 162)
                    bmap.setPixel(x, y, Color.WHITE)
            }
        }
        return bmap
    }

    private fun bitmapResizer(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)

        val ratioX = newWidth / bitmap.width.toFloat()
        val ratioY = newHeight / bitmap.height.toFloat()
        val middleX = newWidth / 2.0f
        val middleY = newHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas(scaledBitmap)
        canvas.matrix = scaleMatrix
        canvas.drawBitmap(
            bitmap,
            middleX - bitmap.width / 2,
            middleY - bitmap.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )

        return scaledBitmap

    }

    private fun checkFile(dir: File) {
        if (!dir.exists() && dir.mkdirs()) {
            copyFiles()
        }
        if (dir.exists()) {
            val datafilepath = "$datapath/tessdata/eng.traineddata"
            val datafile = File(datafilepath)

            if (!datafile.exists() || datafile.length() <1) {
                datafile.delete()
                copyFiles()
            }
        }
    }

    private fun copyFiles() = try {
        val filepath = "$datapath/tessdata/eng.traineddata"
        val assetManager = App.instance?.assets

        val instream = assetManager?.open("tessdata/eng.traineddata")
        val outstream = FileOutputStream(filepath)

        val buffer = ByteArray(1024)
        while (instream?.read(buffer)!! > 0) {
            outstream.write(buffer)
        }


        outstream.flush()
        outstream.close()
        instream.close()

        val file = File(filepath)
        if (file.exists()) {
        } else {
            throw FileNotFoundException()
        }
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    fun recognizedText(): String? {
        return recognizedText
    }
}
