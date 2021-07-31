package com.magiceye.learningcenter.model

import java.io.Serializable

data class Pdf(var id:String?, val name:String?, val visible:Boolean?,val url:String?):
    Serializable {
    constructor():this(null,null,null)
    constructor(name: String?,visible: Boolean?,url: String?):this(null,name,visible,url)
}