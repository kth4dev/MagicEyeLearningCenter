package com.magiceye.learningcenter.helper

object FetchDriveLink {

    fun fetch(url: String): String {
        if(url.contains("drive.google.com")){
            val parts = url.split("/")
            if (parts.contains("d")) {
                val position = (parts.indexOf("d")) + 1
                val id = parts[position]
                return "https://drive.google.com/u/0/uc?id=$id&export=download"
            }
        }
        return ""
    }

}